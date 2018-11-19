package com.itdlc.android.library.hook.imp;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.itdlc.android.library.annotation.NeedLogin;
import com.itdlc.android.library.hook.base.BaseClassHandler;
import com.itdlc.android.library.hook.base.BaseHook;
import com.itdlc.android.library.hook.base.BaseMethodHandler;
import com.itdlc.android.library.hook.base.BaseProxyHook;
import com.itdlc.android.library.sp.UserSp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by felear on 2018/4/25.
 */

public class IActivityManagerHandler extends BaseClassHandler {

    private static final String TAG = "IActivityManagerHandler";
    private ComponentName mComponentName;

    public void init(ComponentName componentName) {
        mComponentName = componentName;
    }

    // 传入classloader
    @Override
    public void hook(BaseHook baseHook, ClassLoader classLoader) throws Throwable {

        if (!(baseHook instanceof BaseProxyHook)) {
            Log.e(TAG, "BaseProxyHook");
            return;
        }

        BaseProxyHook baseProxyHook = (BaseProxyHook) baseHook;
        Field gDefault = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Class<?> amClass = Class.forName("android.app.ActivityManager");
            gDefault = amClass.getDeclaredField("IActivityManagerSingleton");
        } else {
            Class<?> amClass = Class.forName("android.app.ActivityManagerNative");
            gDefault = amClass.getDeclaredField("gDefault");
        }
        gDefault.setAccessible(true);
        Object iAmSingleton = gDefault.get(null);
        Log.e(TAG, "initHook: " + iAmSingleton);

        Class<?> singleTonClass = Class.forName("android.util.Singleton");
        Field mInstance = singleTonClass.getDeclaredField("mInstance");
        mInstance.setAccessible(true);

        Object ams = mInstance.get(iAmSingleton);

        // 获取IActivityManager类
        Class<?> iActivityManagerClass = Class.forName("android.app.IActivityManager");
        baseProxyHook.setRealObject(ams);
        Object mAms = Proxy.newProxyInstance(classLoader
                , new Class[]{iActivityManagerClass}
                , baseProxyHook);

        mInstance.set(iAmSingleton, mAms);

        Log.e(TAG, "initHook: " + ams);


    }


    @Override
    protected void initMethod() {
        addMethod("startActivity", new startActivityHandler());
    }

    class startActivityHandler extends BaseMethodHandler {

        @Override
        protected boolean beforeHood(Object realObject, Method method, Object[] args) {
            // 找出intent
            if (UserSp.getInstance().getUserBean() == null) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Intent) {
                        Intent arg = (Intent) args[i];
                        // 判断是否在名单中
                        ComponentName component = arg.getComponent();
                        //通过Uri调取外部地址时获取不到包信息，所以判断为null时return
                        if (component == null)
                            return false;
                        try {
                            Class<?> tClass = Class.forName(component.getClassName());
                            NeedLogin annotation = tClass.getAnnotation(NeedLogin.class);
                            if (annotation != null) {
                                Intent intent = new Intent();
                                intent.putExtra("intent", arg);
                                intent.setComponent(mComponentName);
                                args[i] = intent;
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return false;
        }

        @Override
        protected void afterHood(Object realObject, Method method, Object[] args) {

        }
    }
}
