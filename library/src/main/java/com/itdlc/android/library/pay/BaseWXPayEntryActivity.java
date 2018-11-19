package com.itdlc.android.library.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.itdlc.android.library.Const;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.simple.eventbus.EventBus;
   /**
    * AndroidManifest.xml需要配置app的gradle中
    * <activity
           android:name=".wxapi.WXPayEntryActivity"
           android:exported="true"
           android:launchMode="singleTop"
           android:screenOrientation="portrait">
           <intent-filter>
           <action android:name="android.intent.action.VIEW"/>

           <category android:name="android.intent.category.DEFAULT"/>

           <data android:scheme="you appid"/>
           </intent-filter>
           </activity>
    */
/**
 * 说明：
 *
 * Created by jjs on 2018/9/18.
 */

public class BaseWXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    protected String mWxAppId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mWxAppId == null) {
            throw new NullPointerException("WeiXinPay field mWxAppId is null");
        }
        api = WXAPIFactory.createWXAPI(this, mWxAppId, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        PayResult payResult = new PayResult(resp);
        if (payResult.isSuccess()) {
            EventBus.getDefault().post(0, Const.Event.PAY_SUCCESS);
        }
        finish();
    }
}