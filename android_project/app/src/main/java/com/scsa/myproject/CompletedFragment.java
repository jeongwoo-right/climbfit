package com.scsa.myproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CompletedFragment extends Fragment {

    public static ArrayList<ClimbingPlan> completedList = new ArrayList<>();
    private PlanAdapter adapter;

    public CompletedFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_completed_fragment, container, false);

        ListView listView = view.findViewById(R.id.completedListView);
        adapter = new PlanAdapter(getContext(), completedList, false, null);
        listView.setAdapter(adapter);
        return view;
    }
}
