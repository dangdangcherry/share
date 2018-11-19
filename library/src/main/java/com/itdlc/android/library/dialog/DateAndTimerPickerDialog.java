package com.itdlc.android.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.itdlc.android.library.R;
import com.itdlc.android.library.widget.DateAndTimerPicker;

public class DateAndTimerPickerDialog extends Dialog implements View.OnClickListener {


    private DateAndTimerPicker timerPicker;

    public DateAndTimerPickerDialog(@NonNull Context context) {
        super(context, R.style.Dialog_Base);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_data_timer_picker);

        findViewById(R.id.tv_confirm).setOnClickListener(this);
        timerPicker = findViewById(R.id.date_picker);

    }

    @Override
    public void onClick(View v) {
        if (onConfirmClickListener != null) {
            onConfirmClickListener.onConfirm(timerPicker);
        }
        dismiss();
    }

    public static interface OnConfirmClickListener {
        void onConfirm(DateAndTimerPicker fsDatePicker);
    }

    private OnConfirmClickListener onConfirmClickListener;

    public void setOnConfirmClickListener(OnConfirmClickListener listener) {
        onConfirmClickListener = listener;
    }

    public DateAndTimerPicker getTimerPicker() {
        return timerPicker;
    }
}
