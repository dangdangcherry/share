package com.itdlc.android.library.sp;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.itdlc.android.library.http.entity.LoginEntity;
import com.itdlc.android.library.utils.SystemUtil;

public class UserSp extends CommonSp {
    private static final String SP_NAME = "user_info_p"; // FILE_NAME
    private static UserSp instance;

    /* known key */
    private static final String KEY_ACCOUNT = "account";
    private static final String KEY_PASSWORD = "password";
    private static final String OPEN_ID = "open_id";
    private static final String KEY_INFO = "info";
    private static final String TOKEN = "token";
    private static final String ISLOGIN = "islogin";
    private static final String WRITER = "writer";
    public static void init(Context context) {
        if (instance == null) {
            synchronized (UserSp.class) {
                if (instance == null) {
                    instance = new UserSp(context);
                }
            }
        }
    }

    public static UserSp getInstance() {
        return instance;
    }

    private UserSp(Context context) {
        super(context, SP_NAME);
    }

    public String getAccount(String defaultValue) {
        return getValue(KEY_ACCOUNT, defaultValue);
    }

    public void setAccount(String value) {
        setValue(KEY_ACCOUNT, value);
    }

    public String getPassword(String defaultValue) {
        return getValue(KEY_PASSWORD, defaultValue);
    }

    public void setPassword(String value) {
        setValue(KEY_PASSWORD, value);
    }

    public String getOpenId(String defaultValue) {
        return getValue(OPEN_ID, defaultValue);
    }

    public void setOpenId(String value) {
        setValue(OPEN_ID, value);
    }

    public LoginEntity.DataEntity.UserEntity getUserBean() {
        String info = getInfo(null);
        if (TextUtils.isEmpty(info)) {
            return null;
        }

        return new Gson().fromJson(info, LoginEntity.DataEntity.UserEntity.class);
    }

    public void setUserBean(LoginEntity.DataEntity.UserEntity userEntity) {
        setInfo(new Gson().toJson(userEntity));
    }

    private void setInfo(String value) {
        setValue(KEY_INFO, value);
    }

    private String getInfo(String defaultValue) {
        return getValue(KEY_INFO, defaultValue);
    }

    public void setToken(String value) {
        setValue(TOKEN, value);
    }

    public String getToken(String defaultValue) {
        return getValue(TOKEN, defaultValue);
    }

    public void setLogin(boolean mLogin){
        setValue(ISLOGIN,mLogin);
    }

    public boolean getLogin(){
        return getValue(ISLOGIN,false);
    }

    public void setWriter(String mWriter){
        setValue(WRITER,mWriter);
    }

    public String getWriter(){
        return getValue(WRITER,"");
    }

    /**
     * 退出登录
     */
    public void clearUser() {
        setLogin(false);
        setPassword(null);
        setInfo(null);
        setOpenId(null);
        setToken(null);
        setWriter(null);
        SystemUtil.setSharedInt("threshold", 0);
    }
}
