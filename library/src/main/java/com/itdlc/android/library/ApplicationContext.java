package com.itdlc.android.library;

import android.app.Application;

/**
 * Created by ni on 2018/1/24.
 */

public class ApplicationContext {
    private static Application sInstance;

    public static void set(Application application) {
        sInstance = application;
    }

    public static Application get() {
        return sInstance;
    }

    public static <T extends Application> T get(Class<T> clazz) {
        return (T) sInstance;
    }
}
