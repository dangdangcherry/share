package com.itdlc.android.library.http.entity;
import com.google.gson.annotations.SerializedName;
import com.itdlc.android.library.base.BaseResp;

/**
 * Created by felear on 2018/1/26.
 */

public class WechatPayInfo extends BaseResp {

    /**
     * appid : wxabd7592995d710f9
     * appsecret : 32e7b5da7443febdc50a621a85167ed1
     * noncestr : faf4647ccaf09bab474cdf6b4
     * outTradeNo : RE699018187202
     * package : Sign=WXPay
     * partnerid : 1508980121
     * partnerkey : fdsjalfkjsakjf123146546316967952
     * prepayid : wx19143447076832073bc1b98d2340755738
     * sign : A32FAF00486D9F300997181742D12B91
     * timestamp : 1531982078
     */

    public DataEntity data;

    public static class DataEntity {
        public String appid;
        public String appsecret;
        public String noncestr;
        public String outTradeNo;
        @SerializedName("package")
        public String packageX;
        public String partnerid;
        public String partnerkey;
        public String prepayid;
        public String sign;
        public String timestamp;
    }
}
