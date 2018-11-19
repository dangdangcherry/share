package com.itdlc.android.library.components.order;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.itdlc.android.library.R;
import com.itdlc.android.library.adapter.MyFragmentPagerAdapter;
import com.itdlc.android.library.base.BaseActivity;
import com.itdlc.android.library.utils.UpdateTabWidth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjs on 2018/5/11.
 */

public abstract class BaseViewPageListActivity extends BaseActivity {
    protected View mLine;
    protected TabLayout mTab;
    protected ViewPager mVp;
    protected List<Fragment> mFragments = new ArrayList<>();
    protected List<String> mTabs = new ArrayList<>();
    protected String[] statusArr = {"0:全部", "1:已支付", "2:已完成", "3:已取消"};
    private MyFragmentPagerAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_viewpager_list;
    }

    @Override
    protected void initialView() {
        super.initialView();
        mLine = findViewById(R.id.line);
        mTab = findViewById(R.id.tab);
        mVp = findViewById(R.id.vp);
        createFragment();
    }

    @Override
    protected void initialEvent() {
        super.initialEvent();
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTabs);
        mVp.setAdapter(mAdapter);
        mTab.setTabMode(TabLayout.MODE_FIXED);
        mTab.setupWithViewPager(mVp);
        UpdateTabWidth.reflex(mTab);
        if (mFragments.size() > 3)
            mVp.setOffscreenPageLimit(mFragments.size());
    }

    protected abstract void createFragment();
}
