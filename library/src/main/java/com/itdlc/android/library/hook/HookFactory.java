package com.itdlc.android.library.hook;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import com.itdlc.android.library.hook.base.BaseHook;
import com.itdlc.android.library.hook.base.BaseProxyHook;
import com.itdlc.android.library.hook.imp.IActivityManagerHandler;

import java.util.List;

/**
 * Created by felear on 2018/4/25.
 */

public class HookFactory {

    private static final String TAG = "HookFactory";

    public static void hookIActivityManager(ClassLoader classLoader, ComponentName componentName) {
        IActivityManagerHandler iActivityManagerHandler = new IActivityManagerHandler();
        BaseHook iActivityManagerHook = new BaseProxyHook(iActivityManagerHandler);

        iActivityManagerHandler.init(componentName);
        try {
            iActivityManagerHook.hook(classLoader);
        } catch (Throwable throwable) {
            Log.e(TAG, "hookIActivityManager: " + throwable);
        }
    }

}
