package com.scsa.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnAlarm, btnMouseCatch, btnNews, btnTodo, btnIoT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAlarm = findViewById(R.id.btnAlarm);
        btnMouseCatch = findViewById(R.id.btnMouseCatch);
        btnNews = findViewById(R.id.btnNews);
        btnTodo = findViewById(R.id.btnTodo);

        btnTodo.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TodoActivity.class)));
        btnAlarm.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AlarmMainActivity.class)));
        btnMouseCatch.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ClimbingGameActivity.class)));
        btnNews.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ParserMainActivity.class)));

    }
}
