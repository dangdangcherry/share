package com.dlc.charger.xisheng;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.dlc.charger.xisheng.util.BitMapUtil;
import com.itdlc.android.library.base.BaseActivity;
import com.itdlc.android.library.utils.DeviceUtils;
import com.umeng.qq.tencent.Constants;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_qq_login)
    Button mBtnQqLogin;
    @BindView(R.id.btn_weixin_login)
    Button mBtnWeixinLogin;
    @BindView(R.id.btn_qqweixin_share)
    Button mBtnQqWeixinShare;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    private FragmentManager mManager;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialView() {
        super.initialView();
        BlankFragment blankFragment = new BlankFragment();
        mManager = getSupportFragmentManager();
        mManager.beginTransaction().add(R.id.fl_content,blankFragment).commit();
    }

    @OnClick({R.id.btn_qq_login, R.id.btn_weixin_login, R.id.btn_qqweixin_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_qq_login:
                UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.QQ, authListener);
                break;
            case R.id.btn_weixin_login:
                UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.WEIXIN.toSnsPlatform().mPlatform, authListener);
                break;
            case R.id.btn_qqweixin_share:
                share();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN) {
            UMShareAPI.get(getApplication()).onActivityResult(requestCode, resultCode, data);//调用友盟的回调，主要是拿到uid
        }
    }

    /**
     * 分享图片
     *
     * @return
     */
    UMImage getUMImage() {
        UMImage umImage = null;
        Bitmap bitmap = CodeUtils.createImage("", DeviceUtils.dip2px(150), DeviceUtils.dip2px(150), null);
        if (bitmap != null) {
            umImage = new UMImage(this, BitMapUtil.ImageCompress(bitmap));
        }
        return umImage;
    }

    /**
     * 分享
     */
    private void share() {
        Config.DEBUG = true;
        UMWeb web = new UMWeb("http://5b0988e595225.cdn.sohucs.com/images/20170906/58cdb24be3624488ad3e8d3d00b4585f.jpeg");
        web.setTitle("IT美女");//标题
        web.setDescription("美芳家的宝贝");//描述
        new ShareAction(this)
                .withMedia(web)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        Log.e(TAG, "onStart: " + share_media);
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        Log.e(TAG, "onResult: " + share_media);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        Log.e(TAG, "onError: " + share_media + " " + throwable);
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        Log.e(TAG, "onCancel: " + share_media);
                    }
                })
                .share();
    }


    private String mStrOpenId;
    /**
     * 第三方登录
     */
    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            showLoadPw();
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            dismissLoadPw();
            //授权登录成功
            if (action == 0) {
                UMShareAPI.get(MainActivity.this)
                        .getPlatformInfo(MainActivity.this, platform, authListener);
            } else if (action == 2) {//获取用户数据
                Log.e("tag", data.toString());
                if (SHARE_MEDIA.WEIXIN.equals(platform)) {
                    mStrOpenId = data.get("unionid");
                } else {
                    mStrOpenId = data.get("openid");
                }
//                mStrHeaderUrl = data.get("profile_image_url");
//                mStrName = data.get("screen_name");
            }

            Log.e(TAG, "avatar ===  " + data);
            showLongToast("登录成功！：" + data.toString());
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            dismissLoadPw();
            showLongToast("失败：" + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            dismissLoadPw();
            showLongToast("您已取消了登录");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
