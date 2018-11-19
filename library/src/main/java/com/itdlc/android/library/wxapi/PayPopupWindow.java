package com.itdlc.android.library.wxapi;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.itdlc.android.library.Const;
import com.itdlc.android.library.R;
import com.itdlc.android.library.base.BaseResp;
import com.itdlc.android.library.base.IContract;
import com.itdlc.android.library.helper.CommonObserver;
import com.itdlc.android.library.http.LibApiClient;
import com.itdlc.android.library.http.entity.AliPayResult;
import com.itdlc.android.library.http.entity.WechatPayInfo;
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
import okhttp3.MultipartBody;

/**
 * Created by felear on 2018/1/24.
 */

public class PayPopupWindow extends PopupWindow {

    public String orderNo;
    // 支付方式
    public int payMode = MODE_PAY_ICON;
    // 充值类别 【0.共享币充值 1：押金】
    public int mRechargeType;

    private static final String TAG = "PayPopupWindow";

    public final static int MODE_PAY_BALANCE = 0;
    public final static int MODE_PAY_WECHAT = 1;
    public final static int MODE_PAY_ALIPAY = 2;
    public final static int MODE_PAY_ICON = 3;

    private int[] payIds = new int[]{R.id.rb_balance
            , R.id.rb_wechat
            , R.id.rb_ali
            , R.id.rb_icon};

    private String mstrOrderNo;
    private TextView mTvAmount;
    private Activity mActivity;
    private String mStrAmount;
    private final RadioGroup mRgPay;
    //    private String mBopriceSn;
    private String mUrl;//地址
    private MultipartBody.Builder mServiceBuilder;//参数
    private String mOptionId;
    private String mPayAmount;
    private String mAddressId;
    private String mInvoiceId;

    public PayPopupWindow(Activity activity) {
        super(LayoutInflater.from(activity).inflate(R.layout.pw_sel_pay, null, false)
                , ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        mActivity = activity;
        setTouchable(true);
        // 设置背景颜色
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setAnimationStyle(R.style.pw_slide);

        //  弹出窗监听
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1;
                mActivity.getWindow().setAttributes(params);
            }
        });

        mTvAmount = getContentView().findViewById(R.id.tv_pw_amount);

        mRgPay = getContentView().findViewById(R.id.rg_pay);
        mRgPay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < payIds.length; i++) {
                    if (checkedId == payIds[i]) {
                        payMode = i;
                        break;
                    }
                }
            }
        });

        getContentView().findViewById(R.id.tv_pw_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });

        getContentView().findViewById(R.id.tv_pay_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mServiceBuilder != null) {
                    switch (payMode) {
                        case MODE_PAY_ALIPAY:
                            aliService();
                            break;
                        case MODE_PAY_WECHAT:
                            wxService();
                            break;
                        case MODE_PAY_BALANCE:
                        case MODE_PAY_ICON:
                            balanceService();
                            break;
                    }
                } else if (mstrOrderNo != null) {
                    switch (payMode) {
                        case MODE_PAY_ALIPAY:
                            aliPay();
                            break;
                        case MODE_PAY_WECHAT:
                            weChatPay();
                            break;
                        case MODE_PAY_BALANCE:
                        case MODE_PAY_ICON:
                            balancePay();
                            break;
                    }
                } else {
                    switch (payMode) {
                        case MODE_PAY_ALIPAY:
                            aliPayRecharge();
                            break;
                        case MODE_PAY_WECHAT:
                            weChatRecharge();
                            break;
                    }
                }

                if (isShowing()) {
                    dismiss();
                }
            }
        });
    }

    public void setPayType(String payType) {
        // 判断是否显示
        boolean check = false;
        for (int i = 0; i < payIds.length; i++) {
            RadioButton rb = getContentView().findViewById(payIds[i]);
            if (!payType.contains("" + i)) {
                rb.setVisibility(View.GONE);
            } else if (!check) {
                rb.setChecked(true);
                check = true;
                payMode = i;
            }
        }
    }

    public void isRechargeMode(boolean isRecharge) {
        if (isRecharge) {
            setPayType(MODE_PAY_WECHAT + "," + MODE_PAY_ALIPAY);
        }
    }

    //    public void show(String amount, String orderNo ) {
    //        mstrOrderNo = orderNo;
    //        mTvAmount.setText(amount);
    //        mStrAmount = amount;
    //
    //        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    //
    //        // 修改透明度
    //        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
    //        params.alpha = 0.6f;
    //        mActivity.getWindow().setAttributes(params);
    //    }

    public void show(String amount, String orderNo, String addressId, String invoiceId) {
        mstrOrderNo = orderNo;
        mAddressId = addressId;
        mInvoiceId = invoiceId;
        mTvAmount.setText(amount);
        mPayAmount = amount;
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        // 修改透明度
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.6f;
        mActivity.getWindow().setAttributes(params);
    }

    // rechargeType 充值类别 【0.共享币充值 1：押金 2.充值余额 3.退押金 4.停车场包月充值】
    public void showRecharge(String amount, String optionId, String payAmount) {
        mOptionId = optionId;
        mPayAmount = payAmount;
        show(amount, null, null, null);
    }

    public void showOther(String amount, String url, MultipartBody.Builder builder) {
        mUrl = url;
        mServiceBuilder = builder;
        show(amount, null, null, null);
    }

    /**
     * 余额或共享币支付
     */
    private void balancePay() {
        LibApiClient.getApi()
                .payByBalance(mstrOrderNo, 1)
                .subscribe(new CommonObserver<BaseResp>((IContract.IView) mActivity, true) {
                    @Override
                    protected void onAccept(BaseResp result) {
                        EventBus.getDefault().post(0, Const.Event.PAY_SUCCESS);
                    }
                });
    }

    /**
     * 微信支付
     */
    private void weChatPay() {
        LibApiClient.getApi()
                .getWechatPayInfo(mstrOrderNo, mAddressId, mInvoiceId, 2)
                .subscribe(new CommonObserver<WechatPayInfo>((IContract.IView) mActivity, true) {
                    @Override
                    protected void onAccept(WechatPayInfo result) {
                        if (result.code >= 0) {
                            payByWeChat(result.data);
                        }
                    }
                });
    }

    private void weChatRecharge() {
        LibApiClient.getApi()
                .weChatRecharge(mOptionId, 2, mPayAmount == null ? null : Double.parseDouble((mPayAmount)))
                .subscribe(new CommonObserver<WechatPayInfo>((IContract.IView) mActivity, true) {
                    @Override
                    protected void onAccept(WechatPayInfo result) {
                        if (result.code >= 0) {
                            payByWeChat(result.data);
                        }
                    }
                });
    }

    private void payByWeChat(WechatPayInfo.DataEntity data) {
        if (null == data) {
            //判断是否为空。
            return;
        }

        IWXAPI api = WXAPIFactory.createWXAPI(mActivity, data.appid, false);
        api.registerApp(data.appid);
        PayReq req = new PayReq();
        req.appId = data.appid;
        req.partnerId = data.partnerid;
        req.prepayId = data.prepayid;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = data.noncestr;
        req.timeStamp = data.timestamp + "";
        req.sign = genAppSign(data);
        //发起请求
        api.sendReq(req);
    }

    /**
     * 生成签名
     */
    public static String genAppSign(WechatPayInfo.DataEntity data) {
        ArrayList<NameValuePair> list = new ArrayList<>();
        list.add(new NameValuePair("appid", data.appid));
        list.add(new NameValuePair("noncestr", data.noncestr));
        list.add(new NameValuePair("package", "Sign=WXPay"));
        list.add(new NameValuePair("partnerid", data.partnerid));
        list.add(new NameValuePair("prepayid", data.prepayid));
        list.add(new NameValuePair("timestamp", data.timestamp + ""));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getName());
            sb.append('=');
            sb.append(list.get(i).getValue());
            sb.append('&');
            Log.d(TAG, list.get(i).getName() + ":" + list.get(i).getValue());
        }
        sb.append("key=");
        sb.append(data.partnerkey);
        String appSign = MD5.md5(sb.toString()).toUpperCase();
        Log.d(TAG, sb.toString());
        return appSign;
    }

    private void aliService() {
        mServiceBuilder.addFormDataPart("payType", "3");
        LibApiClient.getApi()
                .aliService(mUrl, mServiceBuilder.build().parts())
                .subscribe(new CommonObserver<AliPayResult>((IContract.IView) mActivity, true) {
                    @Override
                    protected void onAccept(AliPayResult result) {
                        payByAli(result.data.aliData);
                    }
                });

    }

    private void balanceService() {
        mServiceBuilder.addFormDataPart("payType", "1");
        LibApiClient.getApi()
                .balanceService(mUrl, mServiceBuilder.build().parts())
                .subscribe(new CommonObserver<BaseResp>((IContract.IView) mActivity, true) {
                    @Override
                    protected void onAccept(BaseResp result) {
                        EventBus.getDefault().post(0, Const.Event.PAY_SUCCESS);
                    }
                });

    }

    private void wxService() {
        mServiceBuilder.addFormDataPart("payType", "2");
        LibApiClient.getApi()
                .weChatService(mUrl, mServiceBuilder.build().parts())
                .subscribe(new CommonObserver<WechatPayInfo>((IContract.IView) mActivity, true) {
                    @Override
                    protected void onAccept(WechatPayInfo result) {
                        payByWeChat(result.data);
                    }
                });

    }

    /**
     * 支付相关方法
     */

    private void aliPay() {
        LibApiClient.getApi()
                .getAliPayInfo(mstrOrderNo, mAddressId, mInvoiceId, 1)
                .subscribe(new CommonObserver<AliPayResult>((IContract.IView) mActivity, true) {
                    @Override
                    protected void onAccept(AliPayResult result) {
                        payByAli(result.data.aliData);
                    }
                });

    }

    private void aliPayRecharge() {
        LibApiClient.getApi()
                .aliPayRecharge(mOptionId, 3, mPayAmount == null ? null : Double.parseDouble((mPayAmount)))
                .subscribe(new CommonObserver<AliPayResult>((IContract.IView) mActivity, true) {
                    @Override
                    protected void onAccept(AliPayResult result) {
                        payByAli(result.data.aliData);
                    }
                });


    }

    private void payByAli(String userInfo) {
        Log.d(TAG, userInfo);
        Observable.just(userInfo)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, Map<String, String>>() {
                    @Override
                    public Map<String, String> apply(String string) throws Exception {

                        PayTask alipay = new PayTask(mActivity);
                        Map<String, String> result = alipay.payV2(string, true);
                        return result;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<String, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Map<String, String> value) {
                        Log.d(TAG, value + "");
                        if (TextUtils.equals(value.get("resultStatus"), "9000")) {
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

}
