package com.itdlc.android.library.sp;

import android.content.Context;

public class LocSp extends CommonSp {
    private static final String SP_NAME = "loc_p"; // FILE_NAME
    private static LocSp instance;

    /* known key */
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String CHECK_CITY_BEAN = "checkcitybean";

    public static LocSp getInstance(Context context) {
        if (instance == null) {
            synchronized (LocSp.class) {
                if (instance == null) {
                    instance = new LocSp(context);
                }
            }
        }
        return instance;
    }

    private LocSp(Context context) {
        super(context, SP_NAME);
    }

    public String getLat(String defaultValue) {
        return getValue(KEY_LAT, defaultValue);
    }

    public void setLat(String value) {
        setValue(KEY_LAT, value);
    }

    public String getLng(String defaultValue) {
        return getValue(KEY_LNG, defaultValue);
    }

    public void setLng(String value) {
        setValue(KEY_LNG, value);
    }

    public String getCheckCityBean(String defaultValue) {
        return getValue(CHECK_CITY_BEAN, defaultValue);
    }

    public void setCheckCityBean(String value) {
        setValue(CHECK_CITY_BEAN, value);
    }
}
