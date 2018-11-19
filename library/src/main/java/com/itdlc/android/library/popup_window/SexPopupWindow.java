package com.itdlc.android.library.popup_window;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.itdlc.android.library.R;
import com.itdlc.android.library.utils.SystemUtil;

import java.util.Locale;

/**
 * Created by Administrator on 2017/9/9 0009.
 */

public class SexPopupWindow extends PopupWindow implements View.OnClickListener {

    private static final String TAG = "SexPopupWindow";

    public interface SexPickerListener {
        void onManClick();

        void onWomanClick();
    }

    private final Activity mActivity;
    private final TextView mTvMan;
    private final TextView mTvWoman;

    private SexPickerListener sexPickerListener;

    public void setSexPickerListener(SexPickerListener sexPickerListener) {
        this.sexPickerListener = sexPickerListener;
    }

    public SexPopupWindow(final Activity activity, final SexPickerListener sexPickerListener) {
        super(LayoutInflater.from(activity).inflate(R.layout.pw_confirm_dialog, null, false)
                , ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        setTouchable(true);
        // 设置背景颜色
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setAnimationStyle(R.style.pw_slide);

        this.sexPickerListener = sexPickerListener;
        mActivity = activity;

        mTvMan = (TextView) getContentView().findViewById(R.id.tv_pw_title);
        mTvWoman = (TextView) getContentView().findViewById(R.id.tv_pw_confirm);
        TextView tvCancel = (TextView) getContentView().findViewById(R.id.tv_pw_cancel);
        String locale = SystemUtil.getSharedString("locale");
        String man = "男";
        if (Locale.ENGLISH.toString().equals(locale)) {
            man = "Male";
        }
        mTvMan.setText(man);
        String woman = "女";
        if (Locale.ENGLISH.toString().equals(locale)) {
            woman = "Female";
        }
        mTvWoman.setText(woman);
        String canl = "取消";
        if (Locale.ENGLISH.toString().equals(locale)) {
            canl = "Cancel";
        }
        tvCancel.setText(canl);

        mTvMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SexPopupWindow.this.sexPickerListener != null) {
                    SexPopupWindow.this.sexPickerListener.onManClick();
                }
                dismiss();
            }
        });
        mTvWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SexPopupWindow.this.sexPickerListener != null) {
                    SexPopupWindow.this.sexPickerListener.onWomanClick();
                }
                dismiss();
            }
        });
        tvCancel.setOnClickListener(this);

        //  弹出窗监听
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
    public void onClick(View v) {
        dismiss();
    }

    public void show() {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.6f;
        mActivity.getWindow().setAttributes(params);
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }
}
