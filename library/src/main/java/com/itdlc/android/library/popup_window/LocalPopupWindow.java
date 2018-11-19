package com.itdlc.android.library.popup_window;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.itdlc.android.library.R;
import com.itdlc.android.library.bean.CityBean;
import com.itdlc.android.library.utils.KeyboardUtil;
import com.itdlc.android.library.utils.WindowUtils;

import java.util.List;

/**
 * Created by felear on 2017/8/25 0025.
 */

public class LocalPopupWindow extends PopupWindow {


    private final Activity mActivity;
    private final LocalPicker mLocalPicker;

    public LocalPopupWindow(Activity activity) {
        super(new LocalPicker(activity)
                , ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        mActivity = activity;

        mLocalPicker = (LocalPicker) getContentView();

        setTouchable(true);
        // 设置背景颜色
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setAnimationStyle(R.style.pw_slide);

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
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
//        mLocalPicker.animate().translationY(330).setDuration(2000).start();
    }

    public void show() {
        KeyboardUtil.closeKeyBoard(mActivity);
        WindowUtils.setWindowAlpha(mActivity, 0.6f);
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    public void addHeaderView(View view) {
        mLocalPicker.addView(view, 0);
    }

    public String getLocalName(String join) {
        return mLocalPicker.getLocalName(join);
    }

    public String getProvince() {
        return mLocalPicker.getProvince();
    }

    public String getCity() {
        return mLocalPicker.getCity();
    }

    public String getCountry() {
        return mLocalPicker.getCountry();
    }

    public List<CityBean> getLocalBean() {

        return mLocalPicker.getLocalBean();
    }

    public void setData(List<CityBean> data, int provinceId, int cityId, int countId) {
        mLocalPicker.setData(data, provinceId, cityId, countId);
    }

    public void setData(List<CityBean> data, String provinceId, String cityId, String countId) {
        mLocalPicker.setData(data, Integer.parseInt(provinceId), Integer.parseInt(cityId), Integer.parseInt(countId));
    }

    public void setData(List<CityBean> data) {
        mLocalPicker.setData(data);
    }

    public void setCurrentItem(int provinceId, int cityId, int countId) {
        mLocalPicker.setCurrentItem(provinceId, cityId, countId);
    }

    public void setCurrentItem(String provinceId, String cityId, String countId) {
        if (TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(cityId) || TextUtils.isEmpty(countId)) {
            return;
        }
        mLocalPicker.setCurrentItem(Integer.parseInt(provinceId), Integer.parseInt(cityId), Integer.parseInt(countId));
    }


}
