package com.itdlc.android.library.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.contrarywind.view.WheelView;
import com.itdlc.android.library.R;

import java.util.ArrayList;

/**
 * Created by felear on 2018/2/28.
 */

public class TimerSelectView extends LinearLayout {

    WheelView timerStartH;
    WheelView timerStartM;
    WheelView timerEndH;
    WheelView timerEndM;
    private LinearLayout layoutWeek;

    public TimerSelectView(Context context) {
        super(context);
        init();
    }

    public TimerSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.layout_timer_select, this, false);

        timerStartH = header.findViewById(R.id.timer_start_h);
        timerStartM = header.findViewById(R.id.timer_start_m);
        timerEndH = header.findViewById(R.id.timer_end_h);
        timerEndM = header.findViewById(R.id.timer_end_m);
        layoutWeek = header.findViewById(R.id.layout_week);

        ArrayList<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hours.add("0" + i);
            } else {
                hours.add(i + "");
            }
        }

        ArrayList<String> minues = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minues.add("0" + i);
            } else {
                minues.add(i + "");
            }
        }

        timerStartH.setAdapter(new WheelStringAdapter(hours));
        timerStartH.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        timerStartH.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        timerStartH.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        timerStartH.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        timerStartH.setCyclic(false);

        timerStartM.setAdapter(new WheelStringAdapter(minues));
        timerStartM.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        timerStartM.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        timerStartM.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        timerStartM.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        timerStartM.setCyclic(false);

        timerEndH.setAdapter(new WheelStringAdapter(hours));
        timerEndH.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        timerEndH.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        timerEndH.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        timerEndH.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        timerEndH.setCyclic(false);

        timerEndM.setAdapter(new WheelStringAdapter(minues));
        timerEndM.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        timerEndM.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        timerEndM.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        timerEndM.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        timerEndM.setCyclic(false);

        addView(header);

    }

    public String getWeek() {

        String week = "";
        for (int i = 0; i < layoutWeek.getChildCount(); i++) {
            CheckBox childAt = (CheckBox) layoutWeek.getChildAt(i);
            if (childAt.isChecked()) {
                if (TextUtils.isEmpty(week)) {
                    week = i + "";
                } else {
                    week += "," + i;
                }
            }

        }

        return week;
    }

    public String getCTime() {
        return timerStartH.getCurrentItem() + ":" + timerStartM.getCurrentItem();
    }

    public String getETime() {
        return timerEndH.getCurrentItem() + ":" + timerEndM.getCurrentItem();
    }

    public boolean checkTime() {

        if (timerStartH.getCurrentItem() < timerEndH.getCurrentItem() ||
                (timerStartH.getCurrentItem() == timerEndH.getCurrentItem()
                        && timerStartM.getCurrentItem() < timerEndM.getCurrentItem())) {
            return true;
        }

        return false;

    }

}