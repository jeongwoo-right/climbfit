package com.scsa.myproject;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;

public class ResultDialog {

    public interface OnResultEntered {
        void onEntered(String result);
    }

    public ResultDialog(Context context, OnResultEntered listener) {
        EditText input = new EditText(context);
        input.setHint("Enter today's result");

        // AlertDialog 생성과 동시에 바로 show() 호출
        new AlertDialog.Builder(context)
                .setTitle("Completion Result")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    String result = input.getText().toString();
                    listener.onEntered(result);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
