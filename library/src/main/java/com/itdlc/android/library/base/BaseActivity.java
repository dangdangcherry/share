package com.itdlc.android.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.itdlc.android.library.BaseApplication;
import com.itdlc.android.library.Const;
import com.itdlc.android.library.R;
import com.itdlc.android.library.sp.UserSp;
import com.itdlc.android.library.utils.DeviceUtils;
import com.itdlc.android.library.utils.KeyboardUtil;
import com.itdlc.android.library.utils.StatusBarUtil;
import com.itdlc.android.library.utils.ToastUtil;
import com.itdlc.android.library.utils.WindowUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements IContract.IView {
    private long mStart;
    protected boolean mDoubleClick2Exit = false;
    protected boolean mShowBack = true;
    protected boolean mTranslucentStatus = true;
    protected String TAG = this.getClass().getSimpleName();
    protected boolean mImmersionBarStatus = true;

    private PopupWindow mPwLoad;
    private boolean isShowLoad;
    private Unbinder mUnBinder;

    View mStatusBarHolder;
    public TextView mTitle;
    protected boolean bCloseKeyBoard;
    private FrameLayout contentParent;

    /**
     * 获取布局ID
     */
    protected abstract int getLayoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getLayoutRes());
        mUnBinder = ButterKnife.bind(this);
        EventBus.getDefault().registerSticky(this);
        if (mImmersionBarStatus) {
            ImmersionBar(R.color.white, true);
        }
        initialView();
        initialEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mUnBinder.unbind();
        if (mImmersionBar != null) {
            mImmersionBar.with(this).destroy();
        }
    }

    @Override
    public void showHttpStatus(int layoutId) {
        if (contentParent == null) {
            View decorView = getWindow().getDecorView();
            contentParent = decorView.findViewById(android.R.id.content);
        }
        if (contentParent != null) {
            View mLoadingLayout = View.inflate(this, layoutId, null);
            FrameLayout.MarginLayoutParams lp = new FrameLayout.MarginLayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            lp.topMargin = DeviceUtils.dip2px(48);
            mLoadingLayout.setLayoutParams(lp);
            contentParent.addView(mLoadingLayout);
        }
    }

    @Override
    public void hideHttpStatus() {
        if (contentParent != null) {
            for (int i = contentParent.getChildCount() - 1; i > 0; i--) {
                contentParent.removeViewAt(i);
            }
        }
    }

    @UiThread
    @CallSuper
    protected void beforeSetContentView() {
        if (mTranslucentStatus) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        StatusBarUtil.setLightMode(this);
    }


    @UiThread
    @CallSuper
    protected void initialView() {

        mStatusBarHolder = findViewById(R.id.status_bar_placeholder);
        mTitle = findViewById(R.id.tv_title);
        if (mStatusBarHolder != null) {
            mStatusBarHolder.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(this);
        }
        if (mShowBack) {
            View back = findViewById(R.id.iv_left);
            if (back != null) {
                back.setVisibility(View.VISIBLE);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
        setTitle(getTitle());
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (mTitle != null) {
            mTitle.setText(title);
            super.setTitle(null);
        }
    }

    @UiThread
    protected void initialEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mStart = 0;
    }

    @Override
    public void onBackPressed() {
        if (!mDoubleClick2Exit) {
            finish();
        } else {
            long mend = System.currentTimeMillis();
            if (mend - mStart < 2000) {
                finish();
            } else {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mStart = mend;
            }
        }
    }

    @Override
    public void showShortToast(int strId) {
        ToastUtil.showOne(this, getString(strId), Toast.LENGTH_SHORT);
    }

    @Override
    public void showShortToast(String str) {
        ToastUtil.showOne(this, str, Toast.LENGTH_SHORT);
    }

    @Override
    public void showLongToast(int strId) {
        ToastUtil.showOne(this, getString(strId), Toast.LENGTH_LONG);
    }

    @Override
    public void showLongToast(String str) {
        ToastUtil.showOne(this, str, Toast.LENGTH_LONG);
    }

    /**
     * startActivity with bundle
     */
    public void readyGo(Class<?> clazz, Bundle bundle) {
        if (clazz != null) {
            Intent intent = new Intent(this, clazz);
            if (null != bundle) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
        }
    }

    /**
     * startActivity
     */
    public void readyGo(Class<?> clazz) {
        readyGo(clazz, null);
    }

    public void readyGo(Class<?> clazz, int requestCode) {
        readyGo(clazz, null, requestCode);
    }

    public void readyGo(Class<?> clazz, Bundle bundle, int requestCode) {
        if (clazz != null) {
            Intent intent = new Intent(this, clazz);
            if (null != bundle) {
                intent.putExtras(bundle);
            }
            if (requestCode == 0) {
                startActivity(intent);
            } else {
                startActivityForResult(intent, requestCode);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            onActivityResult(requestCode, data);
        }
    }

    protected void onActivityResult(int requestCode, Intent data) {

    }

    /**
     * startActivity with bundle and finish
     */
    public void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        if (clazz != null) {
            Intent intent = new Intent(this, clazz);
            if (null != bundle) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
            finish();
        }
    }

    /**
     * startActivity and finish
     */
    public void readyGoThenKill(Class<?> clazz) {
        readyGoThenKill(clazz, null);
    }


    /**
     * 获取指定ID字符串
     *
     * @param id 资源id,如R.string.app_name
     */
    public String getStr(@StringRes int id) {
        return getResources().getString(id);
    }

    /**
     * 获取指定ID字符串
     *
     * @param id 资源id,如R.string.app_name
     */
    public String getStrFormat(@StringRes int id, int num) {
        return String.format(getStr(id), num);
    }

    public String getStrFormat(@StringRes int id, String... str) {
        return String.format(getStr(id), (Object[]) str);
    }

    @Override
    public void showLoadPw() {
        if (!hasWindowFocus()) {
            isShowLoad = true;
            return;
        }

        if (mPwLoad == null) {
            mPwLoad = WindowUtils.getLoadPopopWindow(this);
        }

        if (!mPwLoad.isShowing() && !isFinishing()) {
            mPwLoad.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public void dismissLoadPw() {
        isShowLoad = false;
        WindowUtils.closePW(mPwLoad);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isShowLoad) {
            isShowLoad = false;
            showLoadPw();
        }
    }

    @Subscriber(tag = Const.Event.TAG_TOKEN_EXPIRE)
    public void logout(int code) {
        UserSp.getInstance().clearUser();
        showLongToast("请先登录");
        Bundle bundle = new Bundle();
        bundle.putBoolean("skip", false);
        readyGo(BaseApplication.loginAct, bundle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtil.closeKeyBoard(this);
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (bCloseKeyBoard) {

                    View view = getCurrentFocus();
                    if (view != null && view instanceof EditText) {
                        int[] location = {0, 0};
                        view.getLocationInWindow(location);
                        int left = location[0], top = location[1], right = left
                                + view.getWidth(), bootom = top + view.getHeight();
                        if (ev.getRawX() < left || ev.getRawX() > right
                                || ev.getY() < top || ev.getRawY() > bootom) {
                            IBinder token = view.getWindowToken();
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (inputMethodManager != null) {
                                inputMethodManager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        }
                    }
                }

                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public int getResourceColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getColor(id);
        } else {
            return getResources().getColor(id);
        }
    }

    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static void setMargins(View view, Context context) {
        int type = StatusBarUtil.getType((Activity) context);
        if (type != 2) {
            //   setMargins(view,0, StatusBarUtil.getStatusBarHeight(context),0,0);
        }

    }

    /**
     * 是否登陆
     *
     * @return
     */
    public boolean isLogin() {
        return UserSp.getInstance().getLogin();
    }

    protected ImmersionBar mImmersionBar;

    public void ImmersionBar(int color, boolean font) {

        mImmersionBar = ImmersionBar.with(this)
                .statusBarColor(color)
                .fitsSystemWindows(true)
                .keyboardEnable(true)
                .statusBarDarkFont(font);
        mImmersionBar.init();
    }

    private static final int MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

}

