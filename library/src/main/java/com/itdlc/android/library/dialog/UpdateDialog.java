package com.itdlc.android.library.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.itdlc.android.library.R;

/**
 * 更新弹窗
 * <p>
 * Created by Administrator on 2017-08-17.
 */

public class UpdateDialog {

    private AppCompatImageView mIvClose;
    private TextView mTvTitle;
    private TextView mTvInfo;
    private TextView mTvVersion;
    private TextView mTvSize;
    private Button mBtUpdate;
    private Context context;
    private OnCallBack onCallBack;


    public UpdateDialog(Context context) {
        this.context = context;
        DialogInit();
    }

    private void DialogInit() {
        final AlertDialog.Builder Builder = new AlertDialog.Builder(context, R.style.DialogHint);
        View view = LayoutInflater.from(context).inflate(R.layout.updatadialog, null);
        Builder.setView(view);

        mIvClose = view.findViewById(R.id.iv_close);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvInfo = view.findViewById(R.id.tv_info);
        mTvVersion = view.findViewById(R.id.tv_version);
        mTvSize = view.findViewById(R.id.tv_size);
        mBtUpdate = view.findViewById(R.id.bt_update);

        final AlertDialog dialog = Builder.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager manager = ((Activity) context).getWindowManager();
        Display d = manager.getDefaultDisplay(); // 获取屏幕宽、高度
        params.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialog.getWindow().setAttributes(params);
        mBtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCallBack != null) {
                    onCallBack.update();
                }
                dialog.dismiss();
            }
        });

        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }


    public void setOnCallBackListener(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public interface OnCallBack {
        void update();
    }
}
