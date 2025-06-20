package com.scsa.myproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class PendingFragment extends Fragment {

    private static final int ADD_REQUEST = 1;

    private ArrayList<ClimbingPlan> pendingList = new ArrayList<>();
    private ArrayList<ClimbingPlan> completedList = CompletedFragment.completedList;
    private PlanAdapter adapter;

    public PendingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pending_fragment, container, false);

        ListView listView = view.findViewById(R.id.pendingListView);
        adapter = new PlanAdapter(getContext(), pendingList, true, plan -> {
            pendingList.remove(plan);
            plan.setCompleted(true);
            completedList.add(plan);
            adapter.notifyDataSetChanged();
        });
        listView.setAdapter(adapter);

        view.findViewById(R.id.fabAdd).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddPlanActivity.class);
            startActivityForResult(intent, ADD_REQUEST);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQUEST && resultCode == Activity.RESULT_OK) {
            ClimbingPlan plan = (ClimbingPlan) data.getSerializableExtra("plan");
            pendingList.add(plan);
            adapter.notifyDataSetChanged();
        }
    }
}