package com.itdlc.android.library.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contrarywind.view.WheelView;
import com.itdlc.android.library.R;
import com.itdlc.android.library.utils.DeviceUtils;

import java.util.ArrayList;

/**
 * Created by felear on 2018/3/1.
 */

public class TimerPicker extends LinearLayout {

    private WheelView wvH;
    private WheelView wvM;
    private WheelView wvS;
    private LayoutParams params;

    public TimerPicker(Context context) {
        super(context);
        init();
    }

    public TimerPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        // 初始化布局
        int dp_16 = DeviceUtils.dip2px(16);
        setPadding(dp_16, dp_16, dp_16, dp_16);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // 小时
        ArrayList<String> mLstH = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                mLstH.add("0" + i);
            } else {
                mLstH.add("" + i);
            }
        }
        wvH = new WheelView(getContext());
        wvH.setAdapter(new WheelStringAdapter(mLstH));
        wvH.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        wvH.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        wvH.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        wvH.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        wvH.setCyclic(false);
        params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        wvH.setLayoutParams(params);

        // 时分
        ArrayList<String> mLst60 = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                mLst60.add("0" + i);
            } else {
                mLst60.add("" + i);
            }
        }

        wvM = new WheelView(getContext());
        wvM.setAdapter(new WheelStringAdapter(mLst60));
        wvM.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        wvM.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        wvM.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        wvM.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        wvM.setCyclic(false);
        wvM.setLayoutParams(params);

        wvS = new WheelView(getContext());
        wvS.setAdapter(new WheelStringAdapter(mLst60));
        wvS.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        wvS.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        wvS.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        wvS.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        wvS.setCyclic(false);
        wvS.setLayoutParams(params);

        addView(wvH);
        TextView textView = new TextView(getContext());
        textView.setText("小时   :");
        textView.setTextColor(0xffffffff);
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(textView);
        addView(wvM);
        TextView textView2 = new TextView(getContext());
        textView2.setText("分钟   :");
        textView2.setTextColor(0xffffffff);
        textView2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(textView2);
        addView(wvS);

        TextView textView3 = new TextView(getContext());
        textView3.setText("秒");
        textView3.setTextColor(0xffffffff);
        textView3.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(textView3);

    }

    public String getCheckTime() {
        return wvH.getCurrentItem() + ":" + wvM.getCurrentItem() + "-" + wvS.getCurrentItem();
    }

    public boolean checkTime() {
        return !(wvH.getCurrentItem() != 0 && wvM.getCurrentItem() != 0 && wvS.getCurrentItem() != 0);
    }
}
