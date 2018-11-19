package com.itdlc.android.library.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by ZACK on 2018/1/10.
 */

public class MetaDataUtil {
    /**
     * 根据key从Application中返回的Bundle中获取value
     *
     * @param key
     * @param defValue
     * @return
     */
    public static String getMetaDataStringApplication(Application application, String key, String defValue) {
        try {
            ApplicationInfo info = application.getPackageManager().getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(key, defValue);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return defValue;
        }
    }

    /**
     * 根据key从Application中返回的Bundle中获取value
     *
     * @param key
     * @param defValue
     * @return
     */
    public static Integer getMetaDataStringApplication(Application application, String key, int defValue) {
        try {
            ApplicationInfo info = application.getPackageManager().getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getInt(key, defValue);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return defValue;
        }
    }

    /**
     * 获取Application中的meta-data.
     *
     * @param packageManager
     * @param packageName
     * @return
     */
    private static Bundle getAppMetaDataBundle(PackageManager packageManager,
                                               String packageName) {
        Bundle bundle = null;
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA);
            bundle = ai.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("getMetaDataBundle", e.getMessage(), e);
        }
        return bundle;
    }

    /**
     * 根据key从Activity中返回的Bundle中获取value
     *
     * @param key
     * @param defValue
     * @return
     */
    public static String getMetaDataStringFromActivity(Activity activity, String key, String defValue) {
        Bundle bundle = getActivityMetaDataBundle(activity.getPackageManager(), activity.getComponentName());
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.getString(key);
        }
        return defValue;
    }

    /**
     * 获取Activity中的meta-data.
     *
     * @param packageManager
     * @param component
     * @return
     */
    private static Bundle getActivityMetaDataBundle(PackageManager packageManager, ComponentName component) {
        Bundle bundle = null;
        try {
            ActivityInfo ai = packageManager.getActivityInfo(component,
                    PackageManager.GET_META_DATA);
            bundle = ai.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("getMetaDataBundle", e.getMessage(), e);
        }

        return bundle;
    }

}
