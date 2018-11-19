package com.itdlc.android.library.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.itdlc.android.library.annotation.NeedPermisson;
import com.itdlc.android.library.http.RxSchedulers;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.Method;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * Created by felear on 2018/4/8.
 */

public class PermissionsUtils {

    private static ArrayList<Method> lstMethod = new ArrayList<>();
    private static Object tAct;

    public static <T extends Activity> void request(T t) {
        registerPermissions(t, null);
    }

    public static <T extends Activity> void request(T t, String tag) {
        registerPermissions(t, tag);
    }

    public static <T extends Fragment> void request(T t) {
        registerPermissions(t, null);
    }

    public static <T extends Fragment> void request(T t, String tag) {
        registerPermissions(t, tag);
    }

    private static <T> void registerPermissions(T t, String tTag) {
        PermissionsUtils.tAct = t;
        Class tClass = t.getClass();
        Method[] declaredMethods = tClass.getDeclaredMethods();
        lstMethod.clear();
        ArrayList<String> lstPermission = new ArrayList<>();
        for (int i = 0; i < declaredMethods.length; i++) {
            Method declaredMethod = declaredMethods[i];
            declaredMethod.setAccessible(true);
            NeedPermisson annotation = declaredMethod.getAnnotation(NeedPermisson.class);
            if (annotation != null) {
                String[] value = annotation.value();
                if (tTag == null || annotation.tag().equals(tTag)) {
                    //判断相同tag，调取对应方法、并申请对应权限；无tag时调取所有
                    for (int j = 0; j < value.length; j++) {
                        if (!lstPermission.contains(value[j])) {
                            lstPermission.add(value[j]);
                        }
                    }
                    lstMethod.add(declaredMethod);
                }
            }
        }

        if (lstPermission.size() == 0) {
            throw new RuntimeException("请选择需要确认的权限");
        }

        Log.e("PermissionsUtils", "request: " + lstPermission);
        String[] permissionArr = new String[lstPermission.size()];
        for (int i = 0; i < lstPermission.size(); i++) {
            permissionArr[i] = lstPermission.get(i);
        }
        Activity act = t instanceof Activity ? (Activity) t : ((Fragment) t).getActivity();
        new RxPermissions(act)
                .request(permissionArr)
                .compose(RxSchedulers.<Boolean>io_main())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            for (Method method : lstMethod) {
                                method.invoke(tAct);
                            }
                        } else {

                        }
                    }
                });
    }


}
