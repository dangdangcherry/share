package com.itdlc.android.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itdlc.android.library.R;
import com.itdlc.android.library.utils.DeviceUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 消息对话框
 */

public class MessageDialog extends Dialog {
    private static final long DEFAULT_DELAY = 2000;
    private String msg;
    private long delay = DEFAULT_DELAY;
    private boolean isSuccess = true;
    private int drawableId;//顶部图片
    private String checkMsg;//按钮文本
    private CardView mCardCheck;
    private View.OnClickListener listener;//按钮点击监听
    private ImageView mIvLogom;

    public MessageDialog(@NonNull Context context, String msg, boolean isSuccess, long delay) {
        super(context, R.style.Dialog_Base);
        this.msg = msg;
        this.delay = delay;
        this.isSuccess = isSuccess;
    }

    public MessageDialog(@NonNull Context context, String msg, boolean isSuccess) {
        this(context, msg, isSuccess, DEFAULT_DELAY);
    }

    public MessageDialog(@NonNull Context context, String msg, long delay) {
        super(context, R.style.Dialog_Base);
        this.msg = msg;
        this.delay = delay;
    }

    public MessageDialog(@NonNull Context context, String msg) {
        this(context, msg, DEFAULT_DELAY);
    }

    public MessageDialog(@NonNull Context context, @StringRes int strRes) {
        this(context, context.getResources().getString(strRes), DEFAULT_DELAY);
    }

    public MessageDialog(@NonNull Context context, @StringRes int strRes, long delay) {
        this(context, context.getResources().getString(strRes), delay);
    }

    public MessageDialog(@NonNull Context context, @DrawableRes int drawableId, String msg, String checkMsg) {
        super(context, R.style.Dialog_Base);
        this.msg = msg;
        this.delay = 0;
        this.drawableId = drawableId;
        this.checkMsg = checkMsg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_message);

        TextView tvMsg = findViewById(R.id.tv_msg);
        mIvLogom = findViewById(R.id.iv_logo);
        mCardCheck = findViewById(R.id.card_check);
        TextView tvCheck = findViewById(R.id.tv_check);

        if (msg == null || msg.length() == 0) {
        } else {
            tvMsg.setText(msg);
        }

        if (drawableId != 0) {
            mIvLogom.setImageResource(drawableId);
        } else if (isSuccess) {
            mIvLogom.setImageResource(R.mipmap.icon_success);
        } else {
            mIvLogom.setImageResource(R.mipmap.icon_fail);
        }

        if (checkMsg == null || checkMsg.length() == 0) {
            mCardCheck.setVisibility(View.GONE);
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvMsg.getLayoutParams();
            params.setMargins(0, DeviceUtils.dip2px(15), 0, 0);
            tvMsg.setLayoutParams(params);
            tvCheck.setText(checkMsg);
            mCardCheck.setVisibility(View.VISIBLE);
            mCardCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(v);
                    }
                    MessageDialog.this.dismiss();
                }
            });
        }
        if (delay > 0) {
            AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, delay, TimeUnit.MILLISECONDS);
        }
    }

    public MessageDialog setCheckListener(View.OnClickListener listener) {
        this.listener = listener;
        return this;
    }


}
