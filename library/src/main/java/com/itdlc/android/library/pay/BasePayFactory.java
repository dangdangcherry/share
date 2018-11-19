package com.itdlc.android.library.pay;

import android.text.TextUtils;

import com.itdlc.android.library.Const;
import com.itdlc.android.library.base.BaseActivity;
import com.itdlc.android.library.helper.CommonObserver;

import org.simple.eventbus.EventBus;

import io.reactivex.Observable;


/**
 * 说明：
 * Created by jjs on 2018/9/18.
 */

public class BasePayFactory {
    protected BaseActivity mBaseActivity;
    protected PayUtils mPayUtils;


    public BasePayFactory(BaseActivity activity) {
        mBaseActivity = activity;
        mPayUtils = new PayUtils();
    }

    public <T extends BasePayEntity> void start(final PayType mPayType, Observable<T> observable) {
        observable
                .subscribe(new CommonObserver<T>(mBaseActivity, true) {
                    @Override
                    protected void onAccept(T t) {
                        if (mPayType == PayType.Ali) {
                            mPayUtils.aliPay(mBaseActivity, t);
                        } else if (mPayType == PayType.WeiXin) {
                            //支付金额为0时。将不会返回支付参数，而直接成功
                            if (TextUtils.isEmpty(t.getWxEntity().appid)) {
                                EventBus.getDefault().post(0, Const.Event.PAY_SUCCESS);
                            } else {
                                mPayUtils.wxPay(mBaseActivity, t);
                            }
                        } else if (mPayType == PayType.Balance) {
                            EventBus.getDefault().post(0, Const.Event.PAY_SUCCESS);
                        } else if (mPayType == PayType.Bank) {
                            EventBus.getDefault().post(0, Const.Event.PAY_SUCCESS);
                        }
                    }
                });
    }

}
