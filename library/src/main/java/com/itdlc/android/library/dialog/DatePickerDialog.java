package com.itdlc.android.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.itdlc.android.library.R;
import com.itdlc.android.library.widget.FsDatePicker;

public class DatePickerDialog extends Dialog implements View.OnClickListener {


    private FsDatePicker fsDatePicker;

    public DatePickerDialog(@NonNull Context context) {
        super(context, R.style.Dialog_Base);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_data_picker);

        findViewById(R.id.tv_confirm).setOnClickListener(this);
        fsDatePicker = findViewById(R.id.fs_date_picker);

    }

    public FsDatePicker getFsDatePicker() {
        return fsDatePicker;
    }

    @Override
    public void onClick(View v) {
        if (onConfirmClickListener != null) {
            onConfirmClickListener.onConfirm(fsDatePicker);
        }
        dismiss();
    }

    public static interface OnConfirmClickListener {
        void onConfirm(FsDatePicker fsDatePicker);
    }

    private OnConfirmClickListener onConfirmClickListener;

    public void setOnConfirmClickListener(OnConfirmClickListener listener) {
        onConfirmClickListener = listener;
    }

}
