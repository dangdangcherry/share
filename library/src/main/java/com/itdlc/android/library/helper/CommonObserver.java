package com.itdlc.android.library.helper;

import com.itdlc.android.library.BuildConfig;
import com.itdlc.android.library.Const;
import com.itdlc.android.library.base.BaseResp;
import com.itdlc.android.library.base.IContract;

import org.simple.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public abstract class CommonObserver<T extends BaseResp> implements Observer<T> {
    protected IContract.IView mView;
    protected boolean force; // 强制刷新，显示loading
    private boolean silent; // 静默，不提示
    protected boolean hasHttpStatus;//给DecorView添加http请求状态。默认展示

    public CommonObserver(IContract.IView iView) {
        this(iView, true, true, false);
    }

    public CommonObserver(IContract.IView iView, boolean force) {
        this(iView, force, true, false);
    }

    public CommonObserver(IContract.IView iView, boolean force, boolean hasHttpStatus) {
        this(iView, force, hasHttpStatus, false);
    }

    public CommonObserver(IContract.IView iView, boolean force, boolean hasHttpStatus, boolean silent) {
        mView = iView;
        this.hasHttpStatus = hasHttpStatus;
        this.force = force;
        this.silent = silent;
        init();
        if (this.force) {
            showLoading();
        }
    }

    protected void init() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    protected abstract void onAccept(@NonNull T t);

    @Override
    public void onNext(T t) {
        if (force) {
            dismissLoading();
        }
        if (t.code == 1) { // 成功
            onAccept(t);
        } else {

            if (t.code == 403 || t.code == 10001 || t.code == 603) { // 登录失效
                EventBus.getDefault().post(0, Const.Event.TAG_TOKEN_EXPIRE);
            }
            onError(t.code, t.msg);
        }
    }

    protected void onError(int status, String msg) {
        showMsg(msg);
    }

    @Override
    public void onError(Throwable e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        }
        if (force) {
            dismissLoading();
        }
        onComplete();
        int code = 1;
        if (e instanceof HttpException) {
            code = ((HttpException) e).code(); // 状态码 404 500 502
        }
        onError(code, "网络请求失败" + code);

    }

    @Override
    public void onComplete() {

    }

    protected void showLoading() {
        if (mView != null) {
            mView.showLoadPw();
        }
    }

    protected void dismissLoading() {
        if (mView != null) {
            mView.dismissLoadPw();
        }
    }

    protected void showMsg(String msg) {
        if (!silent && mView != null) {
            if (msg != null && msg.trim().length() > 1) {
                mView.showShortToast(msg);
            }
        }
    }
}
