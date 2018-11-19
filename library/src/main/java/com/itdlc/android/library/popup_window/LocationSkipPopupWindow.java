package com.itdlc.android.library.popup_window;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.itdlc.android.library.R;
import com.itdlc.android.library.base.BaseActivity;

import java.net.URISyntaxException;

/**
 * Created by felear on 2018/3/9.
 */

public class LocationSkipPopupWindow extends PopupWindow implements View.OnClickListener {

    private final BaseActivity mActivity;
    private String mStrLat;
    private String mStLon;
    private String mStrAddress;
    private static final String TAG = "LocationSkipPopupWindow";

    public LocationSkipPopupWindow(final BaseActivity activity) {
        super(LayoutInflater.from(activity).inflate(R.layout.pw_location_skip, null, false)
                , ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        mActivity = activity;

        getContentView().findViewById(R.id.tv_baidu).setOnClickListener(this);
        getContentView().findViewById(R.id.tv_tengxun).setOnClickListener(this);
        getContentView().findViewById(R.id.tv_gaode).setOnClickListener(this);
        getContentView().setOnClickListener(this);
        getContentView().findViewById(R.id.tv_cancel).setOnClickListener(this);

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
    public void onClick(View view) {
        if (view.getId() == R.id.tv_baidu) {
            if (checkApkExist("com.baidu.BaiduMap")) {
                Intent intent = null;
                try {
                    intent = Intent.getIntent("intent://map/direction?destination=latlng:"
                            + mStrLat + "," + mStLon
                            + "|name:" + mStrAddress + "&mode=driving&src=某某公司#Intent;" + "scheme=bdapp;package=com.baidu.BaiduMap;end");
                } catch (URISyntaxException e) {
                    Log.e(TAG, "onViewClicked: " + e);
                }
                mActivity.startActivity(intent);
            } else {
                mActivity.showLongToast("您未安装百度地图");
            }
        } else if (view.getId() == R.id.tv_tengxun) {

            if (checkApkExist("com.tencent.map")) {
                // 这个Uri可以跳转，但是没找到官方的文档
                Uri uri = Uri.parse("qqmap://map/routeplan?"
                        // 应用名
                        + "referer=" + getVersionName()
                        // 路线规划方式参数：公交：bus 驾车：drive 步行：walk（仅适用移动端）
                        + "&type=drive"
//                        // 起点名称
//                        + "&from=" + startTipBean.getName()
//                        // 起点坐标
//                        + "&fromcoord=" + startTipBean.getPoint().getLatitude() + "," + startTipBean.getPoint().getLongitude()
//                        // 终点名称
                        + "&to=" + mStrAddress
                        // 终点坐标
                        + "&tocoord=" + mStrLat + "," + mStLon
                        // 0：较快捷  1：无高速  2：距离  默认为0
                        + "&policy=0");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mActivity.startActivity(intent);
            } else {
                mActivity.showLongToast("您未安装腾讯地图");
            }

        } else if (view.getId() == R.id.tv_gaode) {

            if (checkApkExist("com.autonavi.minimap")) {
                StringBuffer stringBuffer = new StringBuffer("androidamap://route?sourceApplication=").append("amap");

                stringBuffer.append("&dlat=").append(mStrLat)
                        .append("&dlon=").append(mStLon)
                        .append("&dname=").append(mStrAddress)
                        .append("&dev=").append(0)
                        .append("&t=").append(0);

                Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
                intent.setPackage("com.autonavi.minimap");
                mActivity.startActivity(intent);
            } else {
                mActivity.showLongToast("您未安装高德地图");
            }

        }
        dismiss();
    }

    /**
     * 获取版本名字
     * <p>
     * 对应build.gradle中的versionName      <br>
     *
     * @return String
     */
    public String getVersionName() {
        String versionName = "";
        try {
            PackageManager packageManager = mActivity.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(mActivity.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }


    //com.autonavi.minimap/com.tencent.map/com.baidu.BaiduMap
    private boolean checkApkExist(String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = mActivity.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public void show(String lat, String lon, String address) {

        mStrLat = lat;
        mStLon = lon;
        mStrAddress = address;

        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.6f;
        mActivity.getWindow().setAttributes(params);
    }

}
