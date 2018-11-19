package com.itdlc.android.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.contrarywind.view.WheelView;
import com.itdlc.android.library.R;
import com.itdlc.android.library.widget.WheelStringAdapter;

import java.util.ArrayList;
import java.util.List;

public class SingleWheelDialog<T> extends Dialog {
    private List<T> data;
    private String title;
    private TextView tvTitle;
    private WheelView vWheel;
    private ArrayList<String> lstDataStr = new ArrayList<>();


    public SingleWheelDialog(@NonNull Context context, String title, List<T> data) {
        super(context, R.style.Dialog_Base);
        this.title = title;
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_single_call);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(title);
        vWheel = findViewById(R.id.v_wheel);

        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        // 一定要重新设置, 才能生效
        window.setAttributes(attributes);


        lstDataStr.clear();
        for (int i = 0; i < data.size(); i++) {
            lstDataStr.add(data.get(i).toString());
        }
        vWheel.setAdapter(new WheelStringAdapter(lstDataStr));
        vWheel.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        vWheel.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        vWheel.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        vWheel.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        vWheel.setCyclic(false);

//        Window dialogWindow = vWheel.getWindow();
//        dialogWindow.setGravity( Gravity.BOTTOM);

        TextView tv_confirm = findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnConfirmClickListener != null) {
                    mOnConfirmClickListener.onConfirm(data.get(vWheel.getCurrentItem()));
                }
                dismiss();
            }
        });

        TextView tv_left = findViewById(R.id.tv_left);
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setData(List<T> data) {
        this.data = data;
        lstDataStr.clear();
        for (int i = 0; i < data.size(); i++) {
            lstDataStr.add(data.get(i).toString());
        }
        vWheel.setCurrentItem(0);
    }

    public static interface OnConfirmClickListener<T> {
        void onConfirm(T data);
    }

    private OnConfirmClickListener<T> mOnConfirmClickListener;

    public void setOnConfirmClickListener(OnConfirmClickListener<T> listener) {
        mOnConfirmClickListener = listener;
    }

    public void setContent(String title) {
        this.title = title;
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }
}
