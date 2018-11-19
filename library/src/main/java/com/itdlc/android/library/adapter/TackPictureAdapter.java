package com.itdlc.android.library.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itdlc.android.library.R;
import com.itdlc.android.library.utils.DeviceUtils;
import com.itdlc.android.library.utils.KeyboardUtil;
import com.itdlc.android.library.popup_window.ImageDetailPopupWindow;
import com.itdlc.android.library.popup_window.TackPicturePopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by felear on 2018/1/24.
 */

public class TackPictureAdapter extends RecyclerAdapter<Integer> {

    private final Integer MODE_URL = 0;
    private final Integer MODE_BITMAP = 1;
    private final Integer MODE_ADD = 2;

    private Activity activity;
    private TackPicturePopupWindow mPwTackPic;
    private ImageDetailPopupWindow mPwImageDetail;
    private int limitCount;
    private List<Bitmap> mLstBitmap = new ArrayList<>();
    private List<String> mLstUrl = new ArrayList<>();
    private List<Integer> mLstMode = new ArrayList<>();
    private List<File> mLstFile = new ArrayList<>();
    private boolean bIsUploadMode = true;
    private OnCheckListener mOnCheckListener;
    private ViewGroup.LayoutParams params;

    public interface OnCheckListener {
        void onSelect(TackPictureAdapter adapter);
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        mOnCheckListener = onCheckListener;
    }

    public TackPictureAdapter(Activity activity) {
        this(activity, null);
    }

    public TackPictureAdapter(Activity activity, Fragment callback) {
        super(activity, null, R.layout.item_photo_sel);
        this.activity = activity;
        mLstMode.add(MODE_ADD);
        super.refresh(mLstMode);

        mPwTackPic = new TackPicturePopupWindow(activity, callback, new TackPicturePopupWindow.TackPictureListener() {
            @Override
            public void newBitmap(Bitmap bitmap) {
                if (!hasGetFile) {
                    mLstBitmap.add(bitmap);
                    mLstMode.add(mLstMode.size() - 1, MODE_BITMAP);
                    refresh();
                }
            }
        });
        mPwTackPic.setTackFileListener(new TackPicturePopupWindow.TackFileListener() {
            @Override
            public void tackFile(File file) {
                mLstFile.add(file);
                mLstMode.add(mLstMode.size() - 1, MODE_BITMAP);
                refresh();
            }
        });

        mPwImageDetail = new ImageDetailPopupWindow(activity, null);
    }

    public void setUcrop(int maxWidth, int maxHeight, float proption) {
        mPwTackPic.setUcrop(maxWidth, maxHeight, proption);
    }

    public void setResult(int requestCode, int resultCode, Intent data) {
        mPwTackPic.setResult(requestCode, resultCode, data);
    }

    public void setIsUploadMode(boolean isUploadMode) {
        if (isUploadMode == bIsUploadMode) {
            return;
        }
        bIsUploadMode = isUploadMode;
        Integer mode = mLstMode.get(mLstMode.size() - 1);
        if (bIsUploadMode) {
            // 可以关闭
            if (mode != MODE_ADD) {
                mLstMode.add(MODE_ADD);
            }
        } else {
            if (mode == MODE_ADD) {
                mLstMode.remove(mLstMode.size() - 1);
            }
        }

        refresh();
    }

    @Override
    public void convert(RecyclerViewHolder helper, final int position, final Integer item) {

        if (params != null) {
            helper.getConvertView().setLayoutParams(params);
        }

        ImageView ivContent = (ImageView) helper.getView(R.id.imageView);
        ImageView ivDel = (ImageView) helper.getView(R.id.iv_photo_delete);
        TextView tvNum = (TextView) helper.getView(R.id.tv_img_num);
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtil.closeKeyBoard(activity);
                if (item == MODE_ADD) {
                    if (hasGetFile) {
                        mPwTackPic.show(true);
                    } else {
                        mPwTackPic.show();
                    }

                    if (mOnCheckListener != null) {
                        mOnCheckListener.onSelect(TackPictureAdapter.this);
                    }
                } else {
                    mPwImageDetail.setUrlList(mLstUrl);
                    mPwImageDetail.setBitList(mLstBitmap);
                    mPwImageDetail.show(position);
                }
            }
        });

        if (item == MODE_ADD) {
            // 显示加号
            if (limitCount > 0) {
                tvNum.setVisibility(View.VISIBLE);
                tvNum.setText((position + 1) + "/" + (limitCount - 1));
            }

            ivDel.setVisibility(View.GONE);
            ivContent.setScaleType(ImageView.ScaleType.CENTER);
            ivContent.setImageResource(R.mipmap.apply_add);
        } else {

            tvNum.setVisibility(View.GONE);
            if (bIsUploadMode) {
                ivDel.setVisibility(View.VISIBLE);
            } else {
                ivDel.setVisibility(View.GONE);
            }
            ivContent.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (hasGetFile) {
                if (position < mLstUrl.size()) {
                    helper.setImageByUrl(R.id.imageView, getUrlItem(position));
                } else if (position < mLstUrl.size() + mLstFile.size()) {
                    Glide.with(activity).load(mLstFile.get(position - mLstUrl.size())).into(ivContent);
                }
            } else {
                if (item == MODE_BITMAP)
                    ivContent.setImageBitmap(getBitmapItem(position));
                else {
                    helper.setImageByUrl(R.id.imageView, getUrlItem(position));
                }
            }

        }

        ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLstMode.remove(position);
                removeItem(position);
                if (mLstMode.size() < limitCount && !mLstMode.contains(MODE_ADD)) {
                    mLstMode.add(MODE_ADD);
                }
                refresh();
            }
        });

    }

    private void removeItem(int position) {
        if (hasGetFile) {
            if (position < mLstUrl.size()) {
                mLstUrl.remove(position);
            } else {
                mLstFile.remove(position);
            }
        } else {
            if (position < mLstUrl.size()) {
                mLstUrl.remove(position);
            } else {
                mLstBitmap.remove(position - mLstUrl.size());
            }
        }
    }

    /**
     * @param scale   高度/宽度 值
     * @param count   横向个数
     * @param padding 布局外边距 默认右边有10dp边距
     */
    public void setScale(float scale, int count, int padding) {
        int dip = (count - 1) * 10 + padding * 2;
        int h = (int) ((DeviceUtils.getScreenWdith() - DeviceUtils.dip2px(dip)) / count * scale);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
        refresh();
    }

    public void setLimitCount(int limitCount) {
        if (limitCount > 0) {
            this.limitCount = limitCount + 1;
            refresh();
        }
    }

    public void refresh() {
        if (limitCount > 0 && limitCount <= mLstMode.size()) {
            mLstMode.remove(mLstMode.size() - 1);
        }
        notifyDataSetChanged();
    }

    public List<Bitmap> getBitmapLst() {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < mLstBitmap.size(); i++) {
            if (mLstBitmap.get(i) != null) {
                bitmaps.add(mLstBitmap.get(i));
            }
        }
        return bitmaps;
    }

    public void clear() {
        mLstMode.clear();
        if (bIsUploadMode) {
            mLstMode.add(MODE_ADD);
        }
        refresh();
    }

    public void setUrlList(String url) {
        if (url == null || url.length() == 0) {
            return;
        }
        mLstBitmap.clear();
        mLstUrl.clear();
        mLstMode.clear();
        String[] split = url.split(",");
        for (int i = 0; i < split.length; i++) {
            mLstUrl.add(split[i]);
            mLstMode.add(MODE_URL);
        }
        if (bIsUploadMode) {
            mLstMode.add(MODE_ADD);
        }
        refresh();
    }

    public String getUrlList() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < mLstUrl.size(); i++) {
            if (i > 0) {
                s.append(",");
            }
            s.append(mLstUrl.get(i));
        }
        return s.toString();
    }

    private String getUrlItem(int position) {
        if (position < mLstUrl.size()) {
            return mLstUrl.get(position);
        }
        return null;
    }

    private Bitmap getBitmapItem(int position) {
        position -= mLstUrl.size();
        if (position < mLstBitmap.size()) {
            return mLstBitmap.get(position);
        }
        return null;
    }

    boolean hasGetFile;//是否需要获取File列表

    public void hasGetFile(boolean bool) {
        this.hasGetFile = bool;
    }

    public List<File> getLstFile() {
        return mLstFile;
    }

}
