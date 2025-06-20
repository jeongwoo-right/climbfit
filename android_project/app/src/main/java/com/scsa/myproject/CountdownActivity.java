package com.scsa.myproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CountdownActivity extends AppCompatActivity {

    public static final String EXTRA_SECONDS = "seconds";
    private int seconds;

    private TextView tvCountdown;
    private Button btnBackTimer, btnBackHome;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        tvCountdown = findViewById(R.id.tvCountdown);
        btnBackTimer = findViewById(R.id.btnBackTimer);
        btnBackHome = findViewById(R.id.btnBackHome);

        // 버튼 처음엔 숨김
        btnBackTimer.setVisibility(View.INVISIBLE);
        btnBackHome.setVisibility(View.INVISIBLE);

        seconds = getIntent().getIntExtra(EXTRA_SECONDS, 5);

        startCountdown();
    }

    private void startCountdown() {
        new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int sec = (int) (millisUntilFinished / 1000) + 1;
                tvCountdown.setText(String.valueOf(sec));
            }

            @Override
            public void onFinish() {
                tvCountdown.setText("GO!");

                // 알람 소리 울리기
                mediaPlayer = MediaPlayer.create(CountdownActivity.this, R.raw.sound1);
                mediaPlayer.start();

                // 버튼 보여주기
                btnBackTimer.setVisibility(View.VISIBLE);
                btnBackHome.setVisibility(View.VISIBLE);

                btnBackTimer.setOnClickListener(v -> {
                    Intent intent = new Intent(CountdownActivity.this, AlarmMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                });

                btnBackHome.setOnClickListener(v -> {
                    Intent intent = new Intent(CountdownActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                });
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
    }
}
