package com.itdlc.android.library.pay;

import android.app.Activity;
import android.content.Context;

import com.alipay.sdk.app.PayTask;
import com.itdlc.android.library.Const;
import com.itdlc.android.library.utils.MD5;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 说明：微信or支付宝 支付基础类
 * Created by jjs on 2018/7/18.
 */

public class PayUtils {

    public void aliPay(final Activity activity, final BasePayEntity basePayEntity) {
        Observable.just(basePayEntity.getAliEntity().aliInfo)
                .subscribeOn(Schedulers.newThread())
                .map(new Function<String, PayResult>() {
                    @Override
                    public PayResult apply(String s) throws Exception {
                        PayTask alipay = new PayTask(activity);
                        Map<String, String> result = alipay.payV2(basePayEntity.getAliEntity().aliInfo, true);
                        return new PayResult(result);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PayResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PayResult payResult) {
                        if (payResult.isSuccess()) {
                            EventBus.getDefault().post(0, Const.Event.PAY_SUCCESS);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void wxPay(Context context, BasePayEntity mBasePayEntity) {
        BasePayEntity.WxEntity payEntity = mBasePayEntity.getWxEntity();
        IWXAPI wxApi = WXAPIFactory.createWXAPI(context, null);
        wxApi.registerApp(payEntity.appid);
        PayReq request = new PayReq();
        request.appId = payEntity.appid;
        request.nonceStr = payEntity.nonceStr;
        request.packageValue = "Sign=WXPay";
        request.partnerId = payEntity.partnerId;
        request.prepayId = payEntity.prepayId;
        request.timeStamp = payEntity.timeStamp;
        request.sign = payEntity.sign;
        wxApi.sendReq(request);
    }

    /**
     * 生成签名,应对服务器加签失败的情况
     * 先生成sign，再调用wxPay传入
     */
    public static String genWxAppSign(String appid, String partnerId, String prepayId, String nonceStr, String timeStamp, String partnerkey) {

        ArrayList<NameValuePair> list = new ArrayList<>();
        list.add(new NameValuePair("appid", appid));
        list.add(new NameValuePair("noncestr", nonceStr));
        list.add(new NameValuePair("package", "Sign=WXPay"));
        list.add(new NameValuePair("partnerid", partnerId));
        list.add(new NameValuePair("prepayid", prepayId));
        list.add(new NameValuePair("timestamp", timeStamp));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getName());
            sb.append('=');
            sb.append(list.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(partnerkey);
        String appSign = MD5.md5(sb.toString()).toUpperCase();
        return appSign;
    }

    public static class NameValuePair {

        private String name;
        private String value;

        public NameValuePair(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
