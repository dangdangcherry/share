package com.itdlc.android.library.activity;

import android.text.method.Touch;
import android.util.Log;

import com.bm.library.PhotoView;
import com.itdlc.android.library.R;
import com.itdlc.android.library.base.BaseActivity;
import com.itdlc.android.library.utils.ImageUtils;

public class PhotoViewActivity extends BaseActivity {

    private PhotoView photoView;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_photo_view;
    }

    @Override
    protected void initialView() {
        super.initialView();
        setTitle(getIntent().getStringExtra("title"));
        photoView = findViewById(R.id.photoview);
        // 启用图片缩放功能
        photoView.enable();
        // 判断是否资源文件
        int contentId = getIntent().getExtras().getInt("contentId", 0);
        Log.e("111", contentId + "");
        if (contentId != 0) {
            photoView.setImageResource(contentId);
        }

        String url = getIntent().getStringExtra("url");
        if (url != null) {
            ImageUtils.loadImageUrl(this,photoView, url);
        }
    }
}
