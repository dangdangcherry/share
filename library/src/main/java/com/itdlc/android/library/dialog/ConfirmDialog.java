package com.itdlc.android.library.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.itdlc.android.library.R;
import com.itdlc.android.library.utils.SystemUtil;

import java.util.Locale;

public class ConfirmDialog extends Dialog {
    private CharSequence content;
    private TextView tv_content;
    private TextView tv_cancel;
    private boolean hasCancel = true;
    private TextView mTv_confirm;
    private Activity mActivity;

    public ConfirmDialog(@NonNull Activity context, CharSequence content) {
        super(context, R.style.Dialog_Base);
        this.mActivity = context;
        this.content = content;
    }

    private String confirm, cancal;

    public ConfirmDialog(@NonNull Activity context, CharSequence content, String confirm, String cancal) {
        super(context, R.style.Dialog_Base);
        this.mActivity = context;
        this.content = content;
        this.confirm = confirm;
        this.cancal = cancal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);

        tv_content = findViewById(R.id.tv_content);
        tv_content.setText(content);
        mTv_confirm = findViewById(R.id.tv_confirm);
        if (confirm != null) {
            setTextconfirm(confirm);
        }
        mTv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnConfirmClickListener != null) {
                    mOnConfirmClickListener.onConfirm();
                }
                dismiss();
            }
        });

        tv_cancel = findViewById(R.id.tv_cancel);
        if (cancal != null) {
            setTextCancel(cancal);
        } else {
            String locale = SystemUtil.getSharedString("locale");
            String canl = "取消";
            if (Locale.SIMPLIFIED_CHINESE.toString().equals(locale)) {
                canl = "取消";
            } else if (Locale.TRADITIONAL_CHINESE.toString().equals(locale)) {
                canl = "取消";
            } else if (Locale.ENGLISH.toString().equals(locale)) {
                canl = "Cancel";
            }
            setTextCancel(canl);
        }
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tv_cancel.setVisibility(hasCancel ? View.VISIBLE : View.GONE);
    }

    public void setTextCancel(String cancel) {
        if (tv_cancel != null) {
            tv_cancel.setText(cancel);
        }
    }

    public void setTextconfirm(String confirm) {
        if (mTv_confirm != null) {
            mTv_confirm.setText(confirm);
        }
    }

    public ConfirmDialog hasCancelBtn(boolean hasCancel) {
        this.hasCancel = hasCancel;
        if (tv_cancel != null) {
            tv_cancel.setVisibility(hasCancel ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public static interface OnConfirmClickListener {
        void onConfirm();
    }

    private OnConfirmClickListener mOnConfirmClickListener;

    public ConfirmDialog setOnConfirmClickListener(OnConfirmClickListener listener) {
        mOnConfirmClickListener = listener;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
        if (tv_content != null) {
            tv_content.setText(content);
        }
    }

    @Override
    public void show() {
        if (mActivity != null && !mActivity.isFinishing()) {
            super.show();
        }
    }
}
