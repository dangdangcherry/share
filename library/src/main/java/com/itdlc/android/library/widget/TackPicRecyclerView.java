package com.itdlc.android.library.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.itdlc.android.library.adapter.TackPictureAdapter;
import com.itdlc.android.library.utils.DeviceUtils;

import java.io.File;
import java.util.List;

/**
 * Created by felear on 2018/3/26.
 */

public class TackPicRecyclerView extends RecyclerView {

    private int count = 3;
    final private TackPictureAdapter adapter;

    public TackPicRecyclerView(Context context) {
        super(context);
        adapter = new TackPictureAdapter((Activity) getContext());
        initView();
    }

    public TackPicRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        adapter = new TackPictureAdapter((Activity) getContext());
        initView();
    }

    private void initView() {
        setPadding(DeviceUtils.dip2px(10), 0, 0, DeviceUtils.dip2px(10));
        setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext()
                , count
                , GridLayoutManager.VERTICAL
                , false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        setLayoutManager(gridLayoutManager);
    }

    public void setCount(int count) {

        this.count = count;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext()
                , count
                , GridLayoutManager.VERTICAL
                , false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        setLayoutManager(gridLayoutManager);

    }

    public void setLimit(int limit) {
        adapter.setLimitCount(limit);
    }

    public void setUrlList(String url) {
        adapter.setUrlList(url);
    }

    public void setIsUploadMode(boolean isUploadMode) {
        adapter.setIsUploadMode(isUploadMode);
    }

    public void setUcrop(int maxWidth, int maxHeight, float proption) {
        adapter.setUcrop(maxWidth, maxHeight, proption);
    }

    // 必须调用
    public void setResult(int requestCode, int resultCode, Intent data) {
        adapter.setResult(requestCode, resultCode, data);
    }

    public List<Bitmap> getBitmapLst() {
        return adapter.getBitmapLst();
    }

    public String getUrlList() {
        return adapter.getUrlList();
    }

    public void setOnCheckListener(TackPictureAdapter.OnCheckListener onCheckListener) {
        adapter.setOnCheckListener(onCheckListener);
    }

    public List<File> getLstFile() {
        return adapter.getLstFile();
    }

    public void hasGetFile(boolean bool) {
        adapter.hasGetFile(bool);
    }

    public int getCount(){
        return adapter.getItemCount();
    }

}
