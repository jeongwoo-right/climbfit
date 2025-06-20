package com.scsa.myproject;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ClimbingGameActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    FrameLayout.LayoutParams params;
    int clickedCorrectHoldCount = 0;
    int totalTargetHold = 5;

    String[] colors = {"red", "blue", "yellow"};
    String targetColor;

    ImageView[] imageViews;
    Random random = new Random();

    int myWidth, myHeight;
    int imgWidth = 150, imgHeight = 150;

    TextView counterView;

    boolean waitingForTopHold = false;

    long startTime;
    long endTime;

    NfcAdapter nfcAdapter;

    TextView timerText;
    Handler timerHandler = new Handler();
    boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_climbing_game);

        counterView = findViewById(R.id.counter);
        counterView.setText("0 / 5");

        timerText = findViewById(R.id.timerText);
        timerText.setText("0.000");

        frameLayout = findViewById(R.id.frame);
        params = new FrameLayout.LayoutParams(1, 1);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        myWidth = metrics.widthPixels;
        myHeight = metrics.heightPixels;

        targetColor = colors[random.nextInt(colors.length)];

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        new AlertDialog.Builder(this)
                .setTitle("Start Climbing!")
                .setMessage("Select only the " + targetColor + " holds!")
                .setPositiveButton("Start", (d, w) -> {
                    startTime = System.currentTimeMillis();
                    startTimer();  // 타이머가 추가된 경우 이 부분도 호출
                    init(15);
                })
                .show();
    }

    public void init(int totalHoldCount) {
        clickedCorrectHoldCount = 0;
        frameLayout.removeAllViews();
        imageViews = new ImageView[totalHoldCount];

        String[] colorPool = new String[totalHoldCount];
        for (int i = 0; i < 5; i++) {
            colorPool[i] = targetColor;
        }
        for (int i = 5; i < totalHoldCount; i++) {
            colorPool[i] = colors[random.nextInt(colors.length)];
        }
        List<String> shuffled = Arrays.asList(colorPool);
        Collections.shuffle(shuffled);

        Handler handler = new Handler();
        for (int i = 0; i < totalHoldCount; i++) {
            final int index = i;
            handler.postDelayed(() -> addOneHold(index, shuffled.get(index)), index * 500);
        }
    }

    public void addOneHold(int index, String color) {
        ImageView iv = new ImageView(this);
        int resId = getResources().getIdentifier("hold_" + color, "drawable", getPackageName());
        iv.setTag(color);
        iv.setImageResource(resId);
        iv.setOnClickListener(holdClickListener);

        int x = random.nextInt(myWidth - imgWidth);
        int y = random.nextInt(myHeight - imgHeight);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(imgWidth, imgHeight);
        lp.leftMargin = x;
        lp.topMargin = y;

        frameLayout.addView(iv, lp);
        imageViews[index] = iv;

        iv.setScaleX(0f);
        iv.setScaleY(0f);
        iv.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
    }

    View.OnClickListener holdClickListener = v -> {
        String holdColor = (String) v.getTag();
        if (holdColor.equals(targetColor)) {
            v.setVisibility(View.INVISIBLE);
            clickedCorrectHoldCount++;
            counterView.setText(clickedCorrectHoldCount + " / " + totalTargetHold);
            if (clickedCorrectHoldCount == totalTargetHold) {
                // 기존 AlertDialog 제거
                Intent intent = new Intent(ClimbingGameActivity.this, TopHoldActivity.class);
                intent.putExtra("startTime", startTime);
                startActivity(intent);
                finish();
            }

        } else {
            Toast.makeText(this, 	"Wrong color!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d("NFC_DEBUG", "onNewIntent() 호출됨");
        Log.d("NFC_DEBUG", "Action: " + intent.getAction());
        Log.d("NFC_DEBUG", "waitingForTopHold: " + waitingForTopHold);

        if (intent != null
                && (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction()))) {
            if (waitingForTopHold) {
                endTime = System.currentTimeMillis();
                long duration = (endTime - startTime) / 1000;
                waitingForTopHold = false;

                new AlertDialog.Builder(this)
                        .setTitle("Clear!")
                        .setMessage("Time: " + duration + " seconds\nCongratulations!")
                        .setPositiveButton("Retry", (d, w) -> recreate())
                        .setNegativeButton("Exit", (d, w) -> finish())
                        .show();
            } else {
                Toast.makeText(this, "Not ready for TOP hold yet!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    private void startTimer() {
        isTimerRunning = true;
        timerHandler.post(timerRunnable);
    }

    private void stopTimer() {
        isTimerRunning = false;
    }

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isTimerRunning) return;

            long now = System.currentTimeMillis();
            long elapsed = now - startTime;

            long seconds = elapsed / 1000;
            long milliseconds = elapsed % 1000;

            timerText.setText(seconds + "." + String.format("%03d", milliseconds));

            timerHandler.postDelayed(this, 50);  // 50ms 주기로 업데이트
        }
    };

}
