package com.itdlc.android.library.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itdlc.android.library.utils.ImageUtils;

public class ViewHolder {
    private final SparseArray<View> mViews;

    private int mPosition;
    private View mConvertView;

    private ViewHolder(Context context, ViewGroup parent, int layoutId,
                       int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        // setTag
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }
        return (ViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param string
     * @return
     */
    public ViewHolder setText(int viewId, CharSequence string) {
        TextView view = getView(viewId);
        view.setText(string);
        return this;
    }

    public ViewHolder setVisible(int viewId, int visibility) {
        if (visibility != View.VISIBLE && visibility != View.INVISIBLE && visibility != View.GONE) {
            new Throwable("you can set View.VISIBLE, INVISIBLE, GONE");
            return this;
        }
        getView(viewId).setVisibility(visibility);
        return this;
    }

    public ViewHolder setClick(int viewId, View.OnClickListener clickListener) {
        getView(viewId).setOnClickListener(clickListener);
        return this;
    }

    public ViewHolder setChecked(int viewId, boolean isChecked) {
        CheckBox box = getView(viewId);
        box.setChecked(isChecked);
        return this;
    }

    /**
     * 获取edit文本
     *
     * @param viewId
     * @return
     */
    public String getEditText(int viewId) {
        EditText ed = getView(viewId);
        String str = ed.getText().toString();
        return str;
    }


    public void setRating(int viewId, int iScore) {
        RatingBar ratingBar = getView(viewId);
        //ratingBar.setMax(10);
        ratingBar.setProgress(iScore);
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 加载普通图片
     *
     * @param viewId
     * @return
     */
    public ViewHolder setImageByUrl(Context mContext,int viewId, String url) {
        ImageView iv = getView(viewId);
        ImageUtils.loadImageUrl(mContext,iv, url);
        return this;
    }

    /**
     * @param emptyImage 加载普通带默认图片
     * @return
     */
    public ViewHolder setImageByUrl(Context mContext,int viewId, String url, int emptyImage) {
        ImageView iv = getView(viewId);
        ImageUtils.loadImageUrl(mContext,iv, url, emptyImage);
        return this;
    }

    /**
     * @return
     */
    public ViewHolder setCircleImageByUrl(Context context, int viewId, String url) {
        ImageView iv = getView(viewId);
        ImageUtils.loadCircleImageUrl(context, iv, url);
        return this;
    }

    /**
     * 给view设置背景色
     *
     * @param viewId
     * @param color
     * @return
     */
    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public int getPosition() {
        return mPosition;
    }

}
