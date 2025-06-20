package com.scsa.myproject;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TopHoldActivity extends AppCompatActivity {

    private static final String TAG = "TopHoldActivity_NFC";

    NfcAdapter nfcAdapter;
    long startTime;

    TextView instructionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_hold);

        instructionText = findViewById(R.id.instructionText);
        instructionText.setText("TOP 홀드(NFC)에 태깅하세요!");

        startTime = getIntent().getLongExtra("startTime", System.currentTimeMillis());

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent 호출됨");
        Log.d(TAG, "Action: " + intent.getAction());

        if (intent != null &&
                (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())
                        || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())
                        || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction()))) {

            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime) / 1000;

            new AlertDialog.Builder(this)
                    .setTitle("클리어!")
                    .setMessage("소요 시간: " + duration + "초\n축하합니다!")
                    .setCancelable(false)
                    .setPositiveButton("다시하기", (d, w) -> {
                        Intent restartIntent = new Intent(this, ClimbingGameActivity.class);
                        startActivity(restartIntent);
                        finish();
                    })
                    .setNegativeButton("종료", (d, w) -> finish())
                    .show();
        }
    }
}
