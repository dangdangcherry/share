package com.itdlc.android.library.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * 监听软键盘显示/关闭，支持windowSoftInputMode为adjustPan/adjustSize的Activity
 */

public class KeyboardWatcher {
    public interface KeyboardListener {
        void onKeyboardToggle(boolean isShow);
    }

    private View mRootView;
    private KeyboardListener mKeyBoardListener;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    private Boolean mLastKeyboardStatus;

    private KeyboardWatcher(Activity activity) {
        mRootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            private int mRootViewHeight = -1;

            @Override
            public void onGlobalLayout() {
                if (mKeyBoardListener == null) {
                    return;
                }
                if (mRootView.getHeight() == 0) {
                    return;
                } else {
                    if (mRootViewHeight == -1) {
                        mRootViewHeight = mRootView.getHeight(); // 只获取第一次的值
                    }
                }
                Rect r = new Rect();
                mRootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = mRootViewHeight - (r.bottom - r.top);
                boolean isActive = heightDiff > mRootView.getRootView().getHeight() / 4;
                if (mLastKeyboardStatus != null && isActive == mLastKeyboardStatus) {
                    return;
                }
                mKeyBoardListener.onKeyboardToggle(isActive);
                mLastKeyboardStatus = isActive;
            }
        };
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
    }

    private void setKeyBoardListener(KeyboardListener keyBoardListener) {
        if (keyBoardListener == null && mGlobalLayoutListener != null) {
            removeGlobalOnLayoutListener(mGlobalLayoutListener);
        }
        mKeyBoardListener = keyBoardListener;

    }

    public void unwatch() {
        removeGlobalOnLayoutListener(mGlobalLayoutListener);
        mRootView = null;
    }

    @SuppressWarnings("deprecation")
    private void removeGlobalOnLayoutListener(ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        } else {
            mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
    }

    public static KeyboardWatcher watch(Activity activity, KeyboardListener listener) {
        KeyboardWatcher watcher = new KeyboardWatcher(activity);
        watcher.setKeyBoardListener(listener);
        return watcher;
    }
}
