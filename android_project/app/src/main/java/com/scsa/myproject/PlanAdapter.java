package com.scsa.myproject;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_climb, parent, false);
            holder = new ViewHolder();
            holder.textInfo = convertView.findViewById(R.id.textInfo);
            holder.btnComplete = convertView.findViewById(R.id.btnComplete);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            holder.btnEdit = convertView.findViewById(R.id.btnEdit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ClimbingPlan plan = planList.get(position);

        holder.textInfo.setText(
                plan.getDate() + " | " + plan.getPlace() + "\n"
                        + "Goal: " + plan.getGoal() + (plan.getRecord().isEmpty() ? "" : " | Record: " + plan.getRecord())
        );

        if (isPendingList) {
            holder.btnComplete.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnEdit.setVisibility(View.VISIBLE);

            holder.btnComplete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Record");

                final EditText input = new EditText(context);
                input.setHint("Enter today's climb note");
                builder.setView(input);

                builder.setPositiveButton("Save", (dialog, which) -> {
                    plan.setRecord(input.getText().toString());
                    listener.onComplete(plan);
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                builder.show();
            });

            holder.btnDelete.setOnClickListener(v -> {
                planList.remove(plan);
                notifyDataSetChanged();
            });

            holder.btnEdit.setOnClickListener(v -> {
                showEditDialog(plan);
            });

        } else { // Completed 상태
            holder.btnComplete.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnEdit.setVisibility(View.VISIBLE);

            holder.btnDelete.setOnClickListener(v -> {
                planList.remove(plan);
                notifyDataSetChanged();
            });

            holder.btnEdit.setOnClickListener(v -> {
                showEditDialog(plan);
            });
        }

        // 아이콘 이미지 설정 (Vector 리소스 기준)
        holder.btnComplete.setImageResource(R.drawable.ic_check);
        holder.btnDelete.setImageResource(R.drawable.ic_delete);
        holder.btnEdit.setImageResource(R.drawable.ic_edit);

        return convertView;
    }

    // 수정 다이얼로그 (Pending과 Completed 모두 처리 가능)
    private void showEditDialog(ClimbingPlan plan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Plan");

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        // Place
        TextView labelPlace = new TextView(context);
        labelPlace.setText("Gym:");
        layout.addView(labelPlace);

        EditText inputPlace = new EditText(context);
        inputPlace.setText(plan.getPlace());
        layout.addView(inputPlace);

        // Goal
        TextView labelGoal = new TextView(context);
        labelGoal.setText("Goal:");
        layout.addView(labelGoal);

        EditText inputGoal = new EditText(context);
        inputGoal.setText(plan.getGoal());
        layout.addView(inputGoal);

        // Partner
        TextView labelPartner = new TextView(context);
        labelPartner.setText("Companion:");
        layout.addView(labelPartner);

        EditText inputPartner = new EditText(context);
        inputPartner.setText(plan.getPartner());
        layout.addView(inputPartner);

        // Record (Completed일 때만)
        EditText inputRecord = null;
        if (!isPendingList) {
            TextView labelRecord = new TextView(context);
            labelRecord.setText("Today's Record:");
            layout.addView(labelRecord);

            inputRecord = new EditText(context);
            inputRecord.setText(plan.getRecord());
            layout.addView(inputRecord);
        }

        builder.setView(layout);

        final EditText finalInputRecord = inputRecord;
        builder.setPositiveButton("Save", (dialog, which) -> {
            plan.setPlace(inputPlace.getText().toString());
            plan.setGoal(inputGoal.getText().toString());
            plan.setPartner(inputPartner.getText().toString());
            if (!isPendingList && finalInputRecord != null) {
                plan.setRecord(finalInputRecord.getText().toString());
            }
            notifyDataSetChanged();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    static class ViewHolder {
        TextView textInfo;
        ImageButton btnComplete;
        ImageButton btnDelete;
        ImageButton btnEdit;
    }
}
