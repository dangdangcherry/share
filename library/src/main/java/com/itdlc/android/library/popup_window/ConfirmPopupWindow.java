package com.itdlc.android.library.popup_window;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.itdlc.android.library.R;

/**
 * Created by felear on 2018/3/9.
 */

public class ConfirmPopupWindow extends PopupWindow implements View.OnClickListener {

    private final TextView tvTitle;
    private final TextView tvConfirm;
    private final TextView tvCancel;
    private final Activity mActivity;

    public static interface OnConfirmListener {
        void onConfirm();
    }

    private OnConfirmListener onConfirmListener;

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public ConfirmPopupWindow(final Activity activity, String title, String content) {
        super(LayoutInflater.from(activity).inflate(R.layout.pw_confirm, null, false)
                , ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        mActivity = activity;

        tvTitle = getContentView().findViewById(R.id.tv_title);
        tvTitle.setText(title);
        getContentView().setOnClickListener(this);
        tvConfirm = getContentView().findViewById(R.id.tv_confirm);
        tvConfirm.setText(content);
        tvConfirm.setOnClickListener(this);
        tvCancel = getContentView().findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1;
                mActivity.getWindow().setAttributes(params);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_confirm) {
            if (onConfirmListener != null) {
                onConfirmListener.onConfirm();
            }
        }
        dismiss();
    }

    public void show(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.6f;
        mActivity.getWindow().setAttributes(params);
    }

    public void show() {
        show(null);
    }
}
