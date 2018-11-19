package com.itdlc.android.library.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itdlc.android.library.utils.ImageUtils;

import java.util.List;

/**
 * Created by felear on 2017/8/14 0014.
 */

public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private Context mContext = null;
    private List<T> mDatas;
    protected final int mItemLayoutId;

    public RecyclerAdapter(Context context, List<T> datas, int itemLayoutId) {
        mContext = context;
        mDatas = datas;
        mItemLayoutId = itemLayoutId;
    }

    public RecyclerAdapter(List<T> datas, int itemLayoutId) {
        mDatas = datas;
        mItemLayoutId = itemLayoutId;
    }

    // item第一次创建时调用，复用时不会调用
    // viewType表示当前item类型，没有使用多布局是此值无效
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 布局加载 参数2:null时根据item内容自动分配宽高，parent时才会根据父容器属性设置宽高
        View view = LayoutInflater.from(mContext == null ? parent.getContext() : mContext).inflate(mItemLayoutId, parent, false);
        return new RecyclerViewHolder(view);
    }

    // 每个item出来时都会调用
    @Override
    public void onBindViewHolder(RecyclerAdapter.RecyclerViewHolder holder, final int position) {
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
        convert(holder, position, mDatas.get(position));
    }

    public abstract void convert(RecyclerViewHolder helper, int position, T item);

    // 返回item数量
    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    /**
     * 替换元素并刷新
     *
     * @param mDatas
     */
    public void refresh(List<T> mDatas) {
        this.mDatas = mDatas;
        this.notifyDataSetChanged();
    }

    /**
     * 获取当前的数据列表
     *
     * @return
     */
    public List<T> getData() {
        return this.mDatas;
    }

    public void setData(List<T> mlist) {
        this.mDatas = mlist;
        notifyDataSetChanged();
    }

    /**
     * 每个item的点击监听
     */
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mOnItemClickListener = clickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener2<T> mOnItemClickListener2;

    public void setOnItemClickListener2(OnItemClickListener2<T> clickListener) {
        mOnItemClickListener2 = clickListener;
    }

    public interface OnItemClickListener2<T> {
        void onItemClick(int position, T t);
    }

    // 内部类存储item中的控件
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final SparseArray<View> mViews = new SparseArray<View>();
        private View mConvertView;

        public RecyclerViewHolder(View convertView) {
            super(convertView);
            mConvertView = convertView;
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public View getConvertView() {
            return mConvertView;
        }

        /**
         * 为TextView设置字符串
         *
         * @param viewId
         * @param string
         * @return
         */

        public RecyclerViewHolder setText(int viewId, CharSequence string) {
            TextView view = getView(viewId);
            view.setText(string);
            return this;
        }

        public RecyclerViewHolder setClick(int viewId, final View.OnClickListener listener) {
            getView(viewId).setOnClickListener(listener);
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
        public RecyclerViewHolder setImageResource(int viewId, int drawableId) {
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
        public RecyclerViewHolder setImageBitmap(int viewId, Bitmap bm) {
            ImageView view = getView(viewId);
            view.setImageBitmap(bm);
            return this;
        }

        /**
         * 为ImageView设置图片
         *
         * @param viewId
         * @return
         */
        public RecyclerViewHolder setImageByUrl(int viewId, String url) {
            ImageView iv = getView(viewId);
            ImageUtils.loadImageUrl(mContext, iv, url);
            return this;
        }

        /**
         * 为ImageView设置圆形图片
         *
         * @param viewId
         * @return
         */
        public RecyclerViewHolder setCircleImageByUrl(int viewId, String url) {
            ImageView iv = getView(viewId);
            ImageUtils.loadCircleImageUrl(mContext, iv, url);
            return this;
        }

        /**
         * 给view设置背景色
         *
         * @param viewId
         * @param color
         * @return
         */
        public RecyclerViewHolder setBackgroundColor(int viewId, int color) {
            View view = getView(viewId);
            view.setBackgroundColor(color);
            return this;
        }

    }

}
