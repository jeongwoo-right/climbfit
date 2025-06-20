package com.scsa.myproject;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.btnComplete.setImageResource(R.drawable.ic_check);
        holder.btnDelete.setImageResource(R.drawable.ic_delete);

        ClimbingPlan plan = planList.get(position);
        holder.textInfo.setText(
                plan.getDate() + " | " + plan.getPlace() + "\n" +
                        "Goal: " + plan.getGoal() + (plan.getRecord().isEmpty() ? "" : " | Record: " + plan.getRecord())
        );

        if (isPendingList) {
            holder.btnComplete.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnComplete.setOnClickListener(v -> {
                // 팝업 다이얼로그 띄우기
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Today's Record");

                final EditText input = new EditText(context);
                input.setHint("Enter today's climb note");
                builder.setView(input);

                builder.setPositiveButton("Save", (dialog, which) -> {
                    plan.setRecord(input.getText().toString());
                    listener.onComplete(plan);  // 원래의 complete 로직 그대로 실행
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();
            });
            holder.btnDelete.setOnClickListener(v -> {
                planList.remove(plan);
                notifyDataSetChanged();
            });
        } else {
            holder.btnComplete.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> {
                planList.remove(plan);
                notifyDataSetChanged();
            });
        }
        return convertView;
    }

    // ViewHolder 클래스 정의
    static class ViewHolder {
        TextView textInfo;
        ImageButton btnComplete;
        ImageButton btnDelete;
    }

}