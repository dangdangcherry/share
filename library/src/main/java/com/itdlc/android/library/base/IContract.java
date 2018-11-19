package com.itdlc.android.library.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;

/**
 * Created by zackzhou on 2018/1/4.
 */

public interface IContract {

    interface IView {

        void showShortToast(@StringRes int strId);

        void showShortToast(String str);

        void showLongToast(@StringRes int strId);

        void showLongToast(String str);

        void showLoadPw();

        void dismissLoadPw();

        void close();

        void showHttpStatus(@LayoutRes int layoutId);

        void hideHttpStatus();
    }

    interface IPresenter {

        void subscribe();

        void unSubscribe();

        void bind();
    }
}
