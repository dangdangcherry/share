package com.itdlc.android.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.itdlc.android.library.R;
import com.itdlc.android.library.popup_window.LocalPicker;
import com.itdlc.android.library.utils.LocalUtils;

public class LocalDialog extends Dialog implements View.OnClickListener {

    public LocalPicker localPicker;

    public LocalDialog(@NonNull Context context) {
        super(context, R.style.Dialog_Base);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_local);
        localPicker = findViewById(R.id.local_picker);
        localPicker.setData(LocalUtils.getLocalList());
        Button btn = findViewById(R.id.btn_send);
        btn.setOnClickListener(this);
        btn.setBackgroundResource(R.drawable.sel_btn_circle_fg_user);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_send) {
            if (mOnConfirmClickListener != null) {
                mOnConfirmClickListener.onConfirm(localPicker);
            }

        }
        dismiss();
    }

    public static interface OnConfirmClickListener {
        void onConfirm(LocalPicker localPicker);
    }

    private OnConfirmClickListener mOnConfirmClickListener;

    public void setOnConfirmClickListener(OnConfirmClickListener listener) {
        mOnConfirmClickListener = listener;
    }

}
