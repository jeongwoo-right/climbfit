package com.scsa.myproject;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import com.scsa.myproject.databinding.ActivityAlarmMainBinding;

public class AlarmMainActivity extends AppCompatActivity {

    AlarmManager manager;

    Calendar cal = Calendar.getInstance();
    int year = 0;
    int month = 0;
    int day = 0;
    int hour = 0;
    int min = 0;

    private static String pad(int c) {
        return String.format("%02d", c);
    }

    private ActivityAlarmMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkStartPermissionRequest();
    }

    // 다른앱위에 그리기 권한이 있어야 있어야 broadcast가 activity를 실행시킴.
    public void checkStartPermissionRequest() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1000);
            Toast.makeText(this, "권한을 허용해 주세요.", Toast.LENGTH_SHORT).show();
        } else {
            initEvent();
        }
    }

    private void initEvent() {
        binding.reg1.setOnClickListener(v -> {
            int after = Integer.parseInt(binding.elap.getText().toString());
            // 알람 등록 대신 화면 이동
            Intent intent = new Intent(AlarmMainActivity.this, CountdownActivity.class);
            intent.putExtra(CountdownActivity.EXTRA_SECONDS, after);
            startActivity(intent);
        });

        binding.cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AlarmMainActivity.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmMainActivity.this, 101, intent, PendingIntent.FLAG_IMMUTABLE);
                Toast.makeText(AlarmMainActivity.this, "알람 해제", Toast.LENGTH_SHORT).show();
                manager.cancel(pendingIntent);
            }
        });
    }
}
