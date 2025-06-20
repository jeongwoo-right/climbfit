package com.scsa.myproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class AddPlanActivity extends AppCompatActivity {

    EditText editPlace, editPartner, editGoal;
    DatePicker datePicker;
    Button btnSave;

    protected void attachBaseContext(Context newBase) {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(locale);
        super.attachBaseContext(newBase.createConfigurationContext(config));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        editPlace = findViewById(R.id.editPlace);
        editPartner = findViewById(R.id.editPartner);
        editGoal = findViewById(R.id.editGoal);
        datePicker = findViewById(R.id.datePicker);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String place = editPlace.getText().toString();
            String partner = editPartner.getText().toString();
            String goal = editGoal.getText().toString();

            int year = datePicker.getYear();
            int month = datePicker.getMonth() + 1;
            int day = datePicker.getDayOfMonth();
            String date = year + "-" + month + "-" + day;

            ClimbingPlan plan = new ClimbingPlan(place, date, partner, goal);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("plan", plan);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
