package com.itdlc.android.library.popup_window;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bm.library.PhotoView;
import com.itdlc.android.library.R;
import com.itdlc.android.library.adapter.MyPagerAdapter;
import com.itdlc.android.library.utils.ImageUtils;
import com.itdlc.android.library.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/9 0009.
 */

public class ImageDetailPopupWindow extends PopupWindow {

    private ViewGroup.LayoutParams mParams;
    private ViewPager mViewPager;
    private MyPagerAdapter mAdapter;
    private Activity mActivity;
    private List<View> viewList = new ArrayList<>();
    private List<String> mUrlList;
    private List<Bitmap> mBitList;

    public ImageDetailPopupWindow(final Activity activity, List<String> urlList) {
        super(LayoutInflater.from(activity).inflate(R.layout.pw_show_image, null, false)
                , ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        if (urlList != null) {
            mUrlList = urlList;
        }

        mActivity = activity;
        mParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        mViewPager = (ViewPager) getContentView().findViewById(R.id.viewPager);
        mAdapter = new MyPagerAdapter(viewList, null);
        mViewPager.setAdapter(mAdapter);

        refresh();

        setTouchable(true);
        // 设置背景颜色
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //  弹出窗监听
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                StatusBarUtil.setFullScreen(mActivity, false);
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1;
                mActivity.getWindow().setAttributes(params);
            }
        });
    }

    public void show(int position) {
        StatusBarUtil.setFullScreen(mActivity, true);
        setCurrentItem(position);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 0;
                mActivity.getWindow().setAttributes(params);
            }
        });

    }

    private void refresh() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewList.clear();
                if (mUrlList != null) {
                    for (int i = 0; i < mUrlList.size(); i++) {

                        if (mUrlList.get(i) == null) {
                            continue;
                        }

                        PhotoView imageView = new PhotoView(mActivity);
                        imageView.setLayoutParams(mParams);
                        imageView.enable();
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        viewList.add(imageView);
                        ImageUtils.loadImageUrl(mActivity,imageView, mUrlList.get(i));
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageDetailPopupWindow.this.dismiss();
                            }
                        });
                    }
                }

                if (mBitList != null) {
                    for (int i = 0; i < mBitList.size(); i++) {

                        if (mBitList.get(i) == null) {
                            continue;
                        }

                        PhotoView imageView = new PhotoView(mActivity);
                        imageView.setLayoutParams(mParams);
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        imageView.setImageBitmap(mBitList.get(i));
                        imageView.enable();
                        viewList.add(imageView);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageDetailPopupWindow.this.dismiss();
                            }
                        });
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });


    }

    public void setUrlList(List<String> urlList) {
        this.mUrlList = urlList;
        refresh();
    }

    public void setBitList(List<Bitmap> bitList) {
        mBitList = bitList;
        refresh();
    }

    public void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position);
    }


}
