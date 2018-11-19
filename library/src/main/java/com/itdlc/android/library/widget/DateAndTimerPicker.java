package com.itdlc.android.library.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.itdlc.android.library.R;
import com.itdlc.android.library.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class DateAndTimerPicker extends LinearLayout {

    private int START_YEAR = 1960;
    private int START_DAY = 1;
    private int START_MONTH = 0;
    private int END_YEAR = 2100;
    private int END_MONTH = 11;
    private int END_DAY = 31;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hour;
    private WheelView wv_minute;
    private int screenheight;
    private int year_current;
    private int month_current;
    private String[] months_big = new String[]{"1", "3", "5", "7", "8", "10", "12"};
    private String[] months_little = new String[]{"4", "6", "9", "11"};
    private List<String> list_big;
    private List<String> list_little;
    private int day_current;
    private ArrayList<String> mLstYear = new ArrayList<>();

    private ArrayList<String> mLstMonth = new ArrayList<>();

    private ArrayList<String> mLstDay = new ArrayList<>();
    private WheelStringAdapter<String> yearAdapter;
    private WheelStringAdapter<String> monthAdapter;
    private WheelStringAdapter<String> dayAdapter;

    public DateAndTimerPicker(Context context) {
        super(context);
        initDateTimePicker();
    }

    public DateAndTimerPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDateTimePicker();
    }

    /**
     * 设置最大日期
     *
     * @param year  最大年 2016为2016年
     * @param month 最大月 0为1月
     * @param day   最大日期 1为1号
     */
    public void setMaxDate(int year, int month, int day) {

        END_YEAR = year;
        END_MONTH = month;
        END_DAY = day;

        mLstYear.clear();
        for (int i = START_YEAR; i <= END_YEAR; i++) {
            mLstYear.add(i + "");
        }
        wv_year.setCurrentItem(year_current - START_YEAR);
        setMonthByYear();
        setDayByMonth();
    }

    /**
     * 设置最小日期
     *
     * @param year  最大年 2016为2016年
     * @param month 最大月 0为1月
     * @param day   最大日期 1为1号
     */
    public void setMinDate(int year, int month, int day) {
        START_YEAR = year;
        START_MONTH = month;
        START_DAY = day;

        mLstYear.clear();
        for (int i = START_YEAR; i <= END_YEAR; i++) {
            mLstYear.add(i + "");
        }
        wv_year.setCurrentItem(year_current - START_YEAR);
        setMonthByYear();
        setDayByMonth();
    }

    /**
     * 设置日历范围
     *
     * @param yearStart  开始年 2016为2016年
     * @param monthStart 开始月 0为1月
     * @param dayStart   开始日 1为1号
     * @param yearEnd    结束年 2016为2016年
     * @param monthEnd   结束月 0为1月
     * @param dayEnd     结束日 1为1号
     */
    public void setDateRange(int yearStart, int monthStart, int dayStart, int yearEnd, int monthEnd, int dayEnd) {

        START_YEAR = yearStart;
        START_MONTH = monthStart;
        START_DAY = dayStart;

        END_YEAR = yearEnd;
        END_MONTH = monthEnd;
        END_DAY = dayEnd;

        mLstYear.clear();
        for (int i = START_YEAR; i <= END_YEAR; i++) {
            mLstYear.add(i + "");
        }
        wv_year.setCurrentItem(year_current - START_YEAR);
        setMonthByYear();
        setDayByMonth();
    }

    public void setDate(int year, int month, int day) {

        wv_year.setCurrentItem(year - START_YEAR);// 设置年
        wv_month.setCurrentItem(month);// 设置月
        wv_day.setCurrentItem(day - 1);// 设置日

        setMonthByYear();
        setDayByMonth();
    }

    public void setTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }


    /**
     * @Description: 弹出日期时间选择器
     */
    public void initDateTimePicker() {
        screenheight = DeviceUtils.getScreenWdith();

        Calendar calendar = Calendar.getInstance();
        year_current = calendar.get(Calendar.YEAR);
        month_current = calendar.get(Calendar.MONTH);
        day_current = calendar.get(Calendar.DATE);
        // 添加大小月月份并将其转换为list,方便之后的判断

        list_big = Arrays.asList(months_big);
        list_little = Arrays.asList(months_little);

        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        // 年
        mLstYear.clear();
        for (int i = START_YEAR; i <= END_YEAR; i++) {
            mLstYear.add(i + "");
        }
        yearAdapter = new WheelStringAdapter<>(mLstYear);

        wv_year = new WheelView(getContext());
        wv_year.setLayoutParams(params);
        wv_year.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        wv_year.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        wv_year.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        wv_year.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        wv_year.setAdapter(yearAdapter);
        wv_year.setCyclic(false);
        wv_year.setCurrentItem(year_current - START_YEAR);// 初始化时显示的数据
        addView(wv_year);

        // 月
        monthAdapter = new WheelStringAdapter<>(mLstMonth);
        wv_month = new WheelView(getContext());
        wv_month.setLayoutParams(params);
        wv_month.setAdapter(monthAdapter);
        wv_month.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        wv_month.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        wv_month.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        wv_month.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        wv_month.setCyclic(false);
        setMonthByYear();
        //        wv_month.setSelection(month_current);// 初始化时显示的数据
        addView(wv_month);

        // 日
        dayAdapter = new WheelStringAdapter<>(mLstDay);
        wv_day = new WheelView(getContext());
        wv_day.setLayoutParams(params);
        wv_day.setAdapter(dayAdapter);
        wv_day.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        wv_day.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        wv_day.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        wv_day.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        wv_day.setCyclic(false);
        setDayByMonth();
        addView(wv_day);

        /// 小时
        ArrayList<String> mLstH = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                mLstH.add("0" + i);
            } else {
                mLstH.add("" + i);
            }
        }
        wv_hour = new WheelView(getContext());
        wv_hour.setAdapter(new WheelStringAdapter(mLstH));
        wv_hour.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        wv_hour.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        wv_hour.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        wv_hour.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        wv_hour.setCyclic(false);
        wv_hour.setLayoutParams(params);
        addView(wv_hour);

        // 时分
        ArrayList<String> mLst60 = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                mLst60.add("0" + i);
            } else {
                mLst60.add("" + i);
            }
        }

        wv_minute = new WheelView(getContext());
        wv_minute.setAdapter(new WheelStringAdapter(mLstH));
        wv_minute.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        wv_minute.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        wv_minute.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        wv_minute.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        wv_minute.setCyclic(false);
        wv_minute.setLayoutParams(params);
        addView(wv_minute);

        // 添加"年"监听
        wv_year.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                year_current = index + START_YEAR;
                setMonthByYear();
                setDayByMonth();
            }
        });

        // 添加"月"监听
        wv_month.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                // 设置当前月份日期
                if (year_current == START_YEAR) {
                    month_current = position + START_MONTH;
                } else {
                    month_current = position;
                }
                setDayByMonth();
            }
        });

        // 添加"日"监听
        wv_day.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                if (year_current == START_YEAR && month_current == START_MONTH) {
                    day_current = position + START_DAY;
                } else {
                    day_current = position + 1;
                }
            }
        });

    }

    private void setMonthByYear() {
        int month_start = 0;
        int month_end = 11;
        // 设置最大年月数
        if (year_current == END_YEAR) {
            month_end = END_MONTH;
            if (month_current > END_MONTH) {
                month_current = END_MONTH;
            }
        }
        if (year_current == START_YEAR) {// 设置最小年月数
            month_start = START_MONTH;
            if (month_current < START_MONTH) {
                month_current = START_MONTH;
            }
        }

        mLstMonth.clear();
        for (int i = month_start; i <= month_end; i++) {
            mLstMonth.add((i + 1) + "");
        }

        // 重置月份位置
        wv_month.setCurrentItem(month_current - month_start);

    }

    private void setDayByMonth() {
        // 判断大小月及是否闰年,用来确定"日"的数据
        int day_actual;
        int day_start = 1;
        if (list_big.contains(month_current + 1 + "")) {
            day_actual = 31;
        } else if (list_little.contains(month_current + 1 + "")) {
            day_actual = 30;
        } else {
            if ((year_current % 4 == 0 && year_current % 100 != 0)
                    || year_current % 400 == 0)
                day_actual = 29;
            else
                day_actual = 28;
        }

        // 判断当月日期最大值
        if (day_actual > END_DAY && month_current == END_MONTH && year_current == END_YEAR) {
            day_actual = END_DAY;
            if (day_current > day_actual) {
                day_current = day_actual;
            }
        }

        // 判断当月日期最小值
        if (day_start < START_DAY && month_current == START_MONTH && year_current == START_YEAR) {
            day_start = START_DAY;
            if (day_current < day_start) {
                day_current = day_start;
            }
        }

        mLstDay.clear();
        for (int i = day_start; i <= day_actual; i++) {
            mLstDay.add(i + "");
        }

        // 日期位置处理
        wv_day.setCurrentItem(day_current - day_start);

    }

    public String getTimeString(String joinDay, String joinTime) {
        return year_current + joinDay + (month_current + 1) + joinDay + day_current
                + " " + wv_hour.getCurrentItem() + joinTime + wv_minute.getCurrentItem();
    }


    public void setWeelHoldColor(@ColorInt int color) {
        wv_year.setDividerColor(color);
        wv_month.setDividerColor(color);
        wv_day.setDividerColor(color);

        wv_year.setTextColorCenter(color);
        wv_month.setTextColorCenter(color);
        wv_day.setTextColorCenter(color);
    }

    public long getTimeLong() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year_current, month_current, day_current
                , wv_hour.getCurrentItem()
                , wv_minute.getCurrentItem()
                , 0);
        return calendar.getTimeInMillis();
    }
}
