package com.itdlc.android.library.pay;

import com.itdlc.android.library.base.BaseResp;

/**
 * 说明：设想是由app中获取支付信息，通过方法返回一个payEntity，再给payUtils使用
 * Created by jjs on 2018/9/18.
 */

public abstract class BasePayEntity extends BaseResp {
    //构造ali
    protected abstract AliEntity getAliEntity();

    protected abstract WxEntity getWxEntity();

    //ali
    public class AliEntity {
        public AliEntity(String aliInfo) {
            this.aliInfo = aliInfo;
        }

        public String aliInfo;
    }


    //wx
    public class WxEntity {
        public WxEntity(String appid, String partnerId, String prepayId, String nonceStr, String timeStamp, String sign) {
            this.appid = appid;
            this.partnerId = partnerId;
            this.prepayId = prepayId;
            this.nonceStr = nonceStr;
            this.timeStamp = timeStamp;
            this.sign = sign;
        }

        public String appid;
        public String partnerId;
        public String prepayId;
        public String nonceStr;
        public String timeStamp;
        public String sign;
    }
}
