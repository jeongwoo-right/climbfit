package com.scsa.myproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class PlanAdapter extends ArrayAdapter<ClimbingPlan> {

    private Context context;
    private ArrayList<ClimbingPlan> planList;
    private boolean isPendingList;
    private OnCompleteListener listener;

    public interface OnCompleteListener {
        void onComplete(ClimbingPlan plan);
    }

    public PlanAdapter(Context context, ArrayList<ClimbingPlan> plans, boolean isPendingList, OnCompleteListener listener) {
        super(context, 0, plans);
        this.context = context;
        this.planList = plans;
        this.isPendingList = isPendingList;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_climb, parent, false);

        ClimbingPlan plan = planList.get(position);
        TextView textInfo = convertView.findViewById(R.id.textInfo);
        textInfo.setText(plan.getDate() + " | " + plan.getPlace() + " - " + plan.getGoal());

        Button btnComplete = convertView.findViewById(R.id.btnComplete);
        if (isPendingList) {
            btnComplete.setVisibility(View.VISIBLE);
            btnComplete.setOnClickListener(v -> listener.onComplete(plan));
        } else {
            btnComplete.setVisibility(View.GONE);
        }
        return convertView;
    }
}