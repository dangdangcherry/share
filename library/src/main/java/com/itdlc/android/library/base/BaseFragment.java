package com.itdlc.android.library.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.itdlc.android.library.R;
import com.itdlc.android.library.sp.UserSp;
import com.itdlc.android.library.utils.StatusBarUtil;
import com.itdlc.android.library.utils.ToastUtil;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements IContract.IView {
    protected String TAG = this.getClass().getSimpleName();
    protected boolean mShowBack = false;
    protected long mTimeFreshData;
    protected boolean mIsFreshOnResume = true;
    protected boolean mImmersionBarStatus = false;
    View mStatusBarHolder;
    public Unbinder mUnbinder;
    private TextView mTitle;
    protected boolean isLoad;
    private FrameLayout contentParent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    protected abstract int getLayoutRes();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isLoad && isVisibleToUser && System.currentTimeMillis() - mTimeFreshData > 2000) {
            freshData();
            mTimeFreshData = System.currentTimeMillis();
        }

    }

    @Override
    public void showHttpStatus(int layoutId) {
        if (contentParent == null) {
            View decorView = getActivity().getWindow().getDecorView();
            contentParent = decorView.findViewById(android.R.id.content);
        }
        if (contentParent != null) {
            View mLoadingLayout = View.inflate(getActivity(), layoutId, null);
            mLoadingLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            contentParent.addView(mLoadingLayout);
        }
    }

    @Override
    public void hideHttpStatus() {
        if (contentParent != null) {
            for (int i = contentParent.getChildCount(); i < 1; i--) {
                contentParent.removeViewAt(i);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isLoad && !hidden && System.currentTimeMillis() - mTimeFreshData > 2000) {
            freshData();
            mTimeFreshData = System.currentTimeMillis();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLoad && mIsFreshOnResume && getUserVisibleHint() && System.currentTimeMillis() - mTimeFreshData > 2000) {
            freshData();
            mTimeFreshData = System.currentTimeMillis();
        }
    }

    public void freshData() {

    }

    @UiThread
    @CallSuper
    protected void initialView(View view) {

        mStatusBarHolder = view.findViewById(R.id.status_bar_placeholder);
        mTitle = view.findViewById(R.id.tv_title);
        if (mStatusBarHolder != null) {
            mStatusBarHolder.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(getContext());
        }

        if (mShowBack) {
            View back = view.findViewById(R.id.iv_left);
            if (back != null) {
                back.setVisibility(View.VISIBLE);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
                    }
                });
            }
        }
        isLoad = true;
        mTimeFreshData = 0;
    }

    public void setTitle(CharSequence title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }

    @UiThread
    protected void initialEvent() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 弹出窗
        mUnbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().registerSticky(this);
        if (mImmersionBarStatus) {
            ImmersionBar(R.color.colorPrimary, true);
        }
        initialView(view);
        initialEvent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mUnbinder.unbind();
        if (mImmersionBar != null) {
            mImmersionBar.with(this).destroy();
        }
    }

    @Override
    public void showShortToast(int strId) {
        ToastUtil.showOne(getContext(), getString(strId), Toast.LENGTH_SHORT);
    }

    @Override
    public void showShortToast(String str) {
        ToastUtil.showOne(getContext(), str, Toast.LENGTH_SHORT);
    }

    @Override
    public void showLongToast(int strId) {
        ToastUtil.showOne(getContext(), getString(strId), Toast.LENGTH_LONG);
    }

    @Override
    public void showLongToast(String str) {
        ToastUtil.showOne(getContext(), str, Toast.LENGTH_LONG);
    }

    /**
     * startActivity with bundle
     */
    public void readyGo(Class<?> clazz, Bundle bundle) {
        if (clazz != null) {
            Intent intent = new Intent(getContext(), clazz);
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

    /**
     * startActivity with bundle and finish
     */
    public void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        if (clazz != null) {
            Intent intent = new Intent(getContext(), clazz);
            if (null != bundle) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
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

    /**
     * 弹出加载弹出窗
     */
    @Override
    public void showLoadPw() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showLoadPw();
        }
    }

    @Override
    public void dismissLoadPw() {
        ((BaseActivity) getActivity()).dismissLoadPw();
    }


    @Override
    public void close() {
        getActivity().finish();
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
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(color);
        mImmersionBar.statusBarDarkFont(font);
        mImmersionBar.fitsSystemWindows(true);
        //mImmersionBar.titleBar(R.id.toolbar);
        mImmersionBar.init();
    }

    private static final int MIN_DELAY_TIME = 300;  // 两次点击间隔不能少于300ms
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

    public int getResourceColor(Context mContext, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return mContext.getColor(id);
        } else {
            return getResources().getColor(id);
        }
    }
}
