package com.dlc.charger.xisheng;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.itdlc.android.library.BaseApplication;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.Locale;

/**
 * Created by dangdang on 2018/9/15.
 */

public class MyApplication extends BaseApplication {

    {
        MyApplication.loginAct = MainActivity.class;
    }

    /**
     * qq、微信
     */ {
        PlatformConfig.setWeixin("wxc5dc1fae48e0b872", " 4e38e094ec190b42ec5b1210d600b3bf");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);
    }
}
