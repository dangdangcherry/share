package com.itdlc.android.library.base;

/**
 * 后台返回
 */
public class BaseResp {


    /**
     * code : 1 成功
     * message : 提示信息
     * time : 时间戳
     * data : {}
     */

    public int code;
    public String msg;
    public long time;

    @Override
    public String toString() {
        return "BaseResp{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", time=" + time +
                '}';
    }
}
