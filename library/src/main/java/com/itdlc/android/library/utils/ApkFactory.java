package com.itdlc.android.library.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by felear on 2018/5/4.
 */

public class ApkFactory {

    private static final String TAG = "ApkFactory";
    static List<String> mLstApkPath = new ArrayList<>();

    public static void init() {
        startLoadInner();
    }

    private static boolean isExist(ApkListBean.DataEntity.ListEntity bean) {
        if (TextUtils.isEmpty(bean.path)) {
            bean.path = Environment.getExternalStorageDirectory()
                    + "/dlc_app/"
                    + bean.category_title + "/"
                    + bean.apk_title
                    + bean.apk_id
                    + ".apk";
        }
        return mLstApkPath.contains(bean.path);
    }

    public static void installAPk(String path, Context context) {
        if (!mLstApkPath.contains(path)) {
            mLstApkPath.add(path);
        }
        File apkFile = new File(path);
        Uri apkUri = null;

        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            try {
                apkUri = FileProvider.getUriForFile(context.getApplicationContext()
                        , "com.itdlc.android.library.dlcandroidmarket", apkFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (apkUri == null) {
            apkUri = Uri.fromFile(apkFile);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//这里加入flag
        context.startActivity(intent);
    }

    public static void getApkInfo(ApkListBean.DataEntity.ListEntity bean, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(bean.packageX, PackageManager.GET_ACTIVITIES);
            if (bean.version > packageInfo.versionCode) {
                bean.update = true;
                mLstApkPath.remove(bean.path);
            } else {
                bean.update = false;
            }
            bean.isInstall = true;
        } catch (PackageManager.NameNotFoundException e) {
            bean.update = false;
        }
        bean.isExist = isExist(bean);
    }

    private static void startLoadInner() {

        mLstApkPath.clear();
        new Thread("ApkScanner") {
            @Override
            public void run() {
                File file = new File(Environment.getExternalStorageDirectory(), "dlc_app");
                searchApkFile(file);
            }
        }.start();
    }

    private static void searchApkFile(File apk) {
        if (apk.isDirectory()) {
            File[] files = apk.listFiles();
            for (File f : files) {
                searchApkFile(f);
            }
        } else {
            if (apk.exists() && apk.getPath().toLowerCase().endsWith(".apk")) {
                mLstApkPath.add(apk.getAbsolutePath());
            }
        }
    }

}
