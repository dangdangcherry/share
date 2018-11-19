package com.itdlc.android.library.widget.loopview;

import android.support.v4.view.ViewPager;

/**
 * Created by Administrator on 2017/9/9 0009.
 */

public interface PageIndicator extends ViewPager.OnPageChangeListener {
    void setViewPager(ViewPager var1);

    void setViewPager(ViewPager var1, int var2);

    void setCurrentItem(int var1);

    void setOnPageChangeListener(ViewPager.OnPageChangeListener var1);

    void notifyDataSetChanged();
}
