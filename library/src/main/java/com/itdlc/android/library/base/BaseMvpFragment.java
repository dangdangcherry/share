package com.itdlc.android.library.base;

import android.view.View;

public abstract class BaseMvpFragment<T extends IContract.IPresenter> extends BaseFragment {
    protected T mPresenter;


    @Override
    protected void initialView(View view) {
        super.initialView(view);
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    protected void initialEvent() {
        super.initialEvent();
        if (mPresenter != null) {
            mPresenter.bind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    protected abstract T createPresenter();
}
