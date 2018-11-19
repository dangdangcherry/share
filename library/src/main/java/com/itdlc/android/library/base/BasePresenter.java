package com.itdlc.android.library.base;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zakczhou on 2018/1/4.
 */

public abstract class BasePresenter<T extends IContract.IView> implements IContract.IPresenter {
    protected T mView;
    protected CompositeDisposable mDisposables = new CompositeDisposable();

    public BasePresenter(T view) {
        mView = view;
    }

    @Override
    public void subscribe() {
        start();
    }

    protected abstract void start();

    protected boolean checkActive() {
        return mView != null;
    }

    @Override
    public void unSubscribe() {
        mDisposables.dispose();
        mView = null;
    }
}
