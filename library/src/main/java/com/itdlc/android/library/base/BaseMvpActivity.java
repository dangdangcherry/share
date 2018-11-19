package com.itdlc.android.library.base;

public abstract class BaseMvpActivity<T extends IContract.IPresenter> extends BaseActivity {
    protected T mPresenter;


    @Override
    protected void initialView() {
        super.initialView();
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
