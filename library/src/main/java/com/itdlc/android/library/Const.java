package com.itdlc.android.library;

/**
 * 应用常量
 */
public final class Const {
    public static String API_CLIENT_HOST = "https://api2.hunter-charge.com/";  //http://192.168.1.200:9103/
    public static String API_CLIENT_HOST_SHORT = "https://api2.hunter-charge.com";   //http://printer.dlc-sz.com/

    public final class Event {
        public static final String TAG_TOKEN_EXPIRE = "token_expire"; // token session过期
        public static final String TAG_PASSWORD_CHANGE = "password_change"; // 密码变更
        public static final String TAG_USER_INFO_CHANGE = "user_info_change"; // 用户信息更变
        public static final String PAY_SUCCESS = "pay_success"; // 支付成功
        public static final String GO_TO_RETURN = "go_to_return"; // 去归还
        public static final String WEB_VIEW_BOTTOM_CLICK = "web_view_bottom_click"; // webview底部按钮点击
    }
}
