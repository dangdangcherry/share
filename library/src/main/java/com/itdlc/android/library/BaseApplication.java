package com.itdlc.android.library;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.bumptech.glide.manager.LifecycleListener;
import com.itdlc.android.library.hook.HookFactory;
import com.itdlc.android.library.sp.UserSp;
import com.itdlc.android.library.utils.DeviceUtils;
import com.itdlc.android.library.utils.LocalUtils;
import com.itdlc.android.library.utils.SystemUtil;

public class BaseApplication extends Application {

    public static Class loginAct;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationContext.set(this);
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }

        LocalUtils.setLocalList(this, "area.json");
        DeviceUtils.setContext(this);
        SystemUtil.setContext(this);
        UserSp.init(this);

        if (loginAct != null) {
            // hook 登录跳转
            ComponentName componentName = new ComponentName(getPackageName(), loginAct.getName());
            HookFactory.hookIActivityManager(Thread.currentThread().getContextClassLoader()
                    , componentName);
        }

    }
}
