package com.scsa.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class TodoActivity extends AppCompatActivity {

    private PendingFragment pendingFragment;
    private CompletedFragment completedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        TabLayout tabLayout = findViewById(R.id.tabLayout);


//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(R.string.app_name);

        pendingFragment = new PendingFragment();
        completedFragment = new CompletedFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, pendingFragment).commit();

        tabLayout.addTab(tabLayout.newTab().setText("Planned"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, pendingFragment).commit();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, completedFragment).commit();
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}