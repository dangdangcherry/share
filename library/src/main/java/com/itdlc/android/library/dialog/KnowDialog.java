package com.itdlc.android.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.itdlc.android.library.R;

public class KnowDialog extends Dialog {
    private OnConfirmClickListener mOnConfirmClickListener1;
    private String content;
    private TextView tv_content;

    public KnowDialog(@NonNull Context context, String content, OnConfirmClickListener onConfirmClickListener) {
        super(context, R.style.Dialog_Base);
        this.content = content;
        mOnConfirmClickListener1 = onConfirmClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_know);

        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        tv_content = findViewById(R.id.tv_content);
        tv_content.setText(content);
        TextView tv_confirm = findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnConfirmClickListener1 != null) {
                    mOnConfirmClickListener1.onConfirm();
                }
                dismiss();
            }
        });
    }

    public static interface OnConfirmClickListener {
        void onConfirm();
    }

    private OnConfirmClickListener mOnConfirmClickListener;

    public void setOnConfirmClickListener(OnConfirmClickListener listener) {
        mOnConfirmClickListener = listener;
    }

    public void setContent(String content) {
        this.content = content;
        if (tv_content != null) {
            tv_content.setText(content);
        }
    }
}
