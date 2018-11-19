package com.itdlc.android.library.widget.loopview;

/**
 * Created by Administrator on 2017/9/9 0009.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itdlc.android.library.R;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class FunBanner extends FrameLayout {
    LoopViewPager mVp;
    CirclePageIndicator mCirclePageIndicator;
    TextView mTitle;
    FunBanner.FunBannerParams mFunBannerParams;
    LinearLayout mIndicatorBar;
    private OnPageChangeListener mOnPageChangeListener;
    private PagerAdapter mPagerAdapter;
    private double mTouchSlop;

    private static final String TAG = "FunBanner";
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickLisenter(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position, ImageView imageView);
    }

    private OnPositionChangeListener mPositionChangeListener;

    public void setonPositionChangeListener(OnPositionChangeListener mPositionChangeListener) {
        this.mPositionChangeListener = mPositionChangeListener;
    }

    public interface OnPositionChangeListener {
        void onPosition(int position, int count);
    }

    public FunBanner(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mFunBannerParams = new FunBanner.FunBannerParams();
        this.mOnPageChangeListener = new OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (FunBanner.this.mFunBannerParams.mTitles != null) {
                    FunBanner.this.mTitle.setText((CharSequence) FunBanner.this.mFunBannerParams.mTitles.get(position));
                }
                if (mPositionChangeListener != null) {
                    mPositionChangeListener.onPosition(position, mIndicatorBar == null ? 0 : mIndicatorBar.getChildCount());
                }

            }

            public void onPageScrollStateChanged(int state) {
            }
        };
        this.mPagerAdapter = new PagerAdapter() {
            private Stack<View> mRecyclerViews = new Stack();

            public int getCount() {
                return FunBanner.this.mFunBannerParams.mImageUrls != null ? FunBanner.this.mFunBannerParams.mImageUrls.size() : (FunBanner.this.mFunBannerParams.mImagesResIds != null ? FunBanner.this.mFunBannerParams.mImagesResIds.length : 0);
            }

            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            public Object instantiateItem(ViewGroup container, final int position) {
                if (FunBanner.this.getContext() instanceof Activity && ((Activity) FunBanner.this.getContext()).isFinishing()) {
                    FunBanner.this.mVp.stopLoop();
                    return null;
                } else {
                    ImageView imageView = null;
                    if (this.mRecyclerViews.isEmpty()) {
                        imageView = new ImageView(FunBanner.this.getContext());
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    } else {
                        imageView = (ImageView) this.mRecyclerViews.pop();
                    }

                    if (FunBanner.this.mFunBannerParams.mImageUrls == null) {
                        imageView.setImageResource(FunBanner.this.mFunBannerParams.mImagesResIds[position]);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    } else {
                        String url = FunBanner.this.mFunBannerParams.mHost + (String) FunBanner.this.mFunBannerParams.mImageUrls.get(position);
                        if (FunBanner.this.mFunBannerParams.mHeightWidthRatio > 0.0F) {
                            Glide.with(FunBanner.this.getContext()).load(url).into(imageView);
                        } else {
                            Glide.with(FunBanner.this.getContext()).load(url).into(imageView);
                        }
                    }

                    imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onClick(position, (ImageView) v);
                            }
                        }
                    });

                    container.addView(imageView);
                    return imageView;
                }
            }

            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
                this.mRecyclerViews.add((View) object);
            }
        };
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoopViewStyle);
        this.mFunBannerParams.mDotRadius = (int) TypedValue.applyDimension(1, 3.0F, this.getResources().getDisplayMetrics());
        this.mFunBannerParams.mDotRadius = a.getDimensionPixelSize(R.styleable.LoopViewStyle_dot_radius, this.mFunBannerParams.mDotRadius);
        this.mFunBannerParams.mEnableAutoLoop = a.getBoolean(R.styleable.LoopViewStyle_enable_auto_loop, false);
        this.mFunBannerParams.mLoopInterval = a.getInt(R.styleable.LoopViewStyle_loop_interval, this.mFunBannerParams.mLoopInterval);
        this.mFunBannerParams.mShowIndicator = a.getBoolean(R.styleable.LoopViewStyle_show_indicator, true);
        this.mFunBannerParams.mDotNormalColor = a.getColor(R.styleable.LoopViewStyle_dot_normal_color, -1);
        this.mFunBannerParams.mDotSelectedColor = a.getColor(R.styleable.LoopViewStyle_dot_selected_color, 0);
        this.mFunBannerParams.mIndicatorBarBackgroundColor = a.getColor(R.styleable.LoopViewStyle_indicator_bar_background_color, 0);
        this.mFunBannerParams.mHeightWidthRatio = a.getFloat(R.styleable.LoopViewStyle_height_width_ratio, 0.0F);
        this.mFunBannerParams.mIndicatorBarHeight = (int) TypedValue.applyDimension(1, 30.0F, this.getResources().getDisplayMetrics());
        this.mFunBannerParams.mIndicatorBarHeight = a.getDimensionPixelSize(R.styleable.LoopViewStyle_indicator_bar_height, this.mFunBannerParams.mIndicatorBarHeight);
        this.mFunBannerParams.mTitleColor = a.getColor(R.styleable.LoopViewStyle_title_color, -1);
        this.mFunBannerParams.mClipChildren = a.getBoolean(R.styleable.LoopViewStyle_clip_children, true);
        this.mFunBannerParams.mChildOffset = a.getDimensionPixelSize(R.styleable.LoopViewStyle_child_offset, 0);
        this.mFunBannerParams.mChildMargin = a.getDimensionPixelSize(R.styleable.LoopViewStyle_child_margin, 0);
        this.mFunBannerParams.mChildCornerSize = a.getDimensionPixelSize(R.styleable.LoopViewStyle_child_corner_size, 0);
        a.recycle();
        this.init();
    }

    private FunBanner(Context context) {
        this(context, null);
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        LayoutInflater.from(this.getContext()).inflate(R.layout.view_loop, this);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mFunBannerParams.mHeightWidthRatio > 0.0F) {
            int size = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) ((double) ((float) size * this.mFunBannerParams.mHeightWidthRatio) + 0.5D);
            int changeHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, 1073741824);
            super.onMeasure(widthMeasureSpec, changeHeightMeasureSpec);
        } else if (this.mFunBannerParams.mHeight > 0) {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(this.mFunBannerParams.mHeight, 1073741824));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    private void initViewPager() {
        this.mVp = (LoopViewPager) this.findViewById(R.id.vp);
        this.mTitle = (TextView) this.findViewById(R.id.title);
        this.mIndicatorBar = (LinearLayout) this.findViewById(R.id.indicator_bar);
        this.mCirclePageIndicator = (CirclePageIndicator) this.findViewById(R.id.indicator);
        this.mVp.setEnableAutoLoop(this.mFunBannerParams.mEnableAutoLoop);
        this.mVp.setLoopInterval(this.mFunBannerParams.mLoopInterval);
        if (this.mFunBannerParams.mImageUrls != null || this.mFunBannerParams.mImagesResIds != null) {
            this.mVp.setAdapter(this.mPagerAdapter);
            if (this.mFunBannerParams.mShowIndicator) {
                this.mCirclePageIndicator.setViewPager(this.mVp);
                this.mCirclePageIndicator.setRadius((float) this.mFunBannerParams.mDotRadius);
                this.mCirclePageIndicator.setPageColor(this.mFunBannerParams.mDotNormalColor);
                this.mCirclePageIndicator.setFillColor(this.mFunBannerParams.mDotSelectedColor);
                this.mCirclePageIndicator.setOnPageChangeListener(this.mOnPageChangeListener);
                this.mIndicatorBar.setMinimumHeight(this.mFunBannerParams.mIndicatorBarHeight);
                this.mIndicatorBar.setBackgroundColor(this.mFunBannerParams.mIndicatorBarBackgroundColor);
            } else {
                this.mCirclePageIndicator.setVisibility(GONE);
            }
        }

        if (this.mFunBannerParams.mTitles != null) {
            this.mTitle.setVisibility(VISIBLE);
            this.mTitle.setText((CharSequence) this.mFunBannerParams.mTitles.get(0));
            this.mTitle.setTextColor(this.mFunBannerParams.mTitleColor);
        } else {
            this.mTitle.setVisibility(GONE);
        }

        // 是否裁剪子视图，视差效果。
        setClipChildren(this.mFunBannerParams.mClipChildren);
        mVp.setClipChildren(this.mFunBannerParams.mClipChildren);
        if (!this.mFunBannerParams.mClipChildren) {
            MarginLayoutParams layoutParams = ((MarginLayoutParams) (mVp.getLayoutParams()));
            if (layoutParams != null) {
                layoutParams.leftMargin = this.mFunBannerParams.mChildMargin;
                layoutParams.rightMargin = this.mFunBannerParams.mChildMargin;
            }
            mVp.setOffscreenPageLimit(mPagerAdapter.getCount());
            mVp.setPageMargin(this.mFunBannerParams.mChildOffset);
        }
    }

    public ViewPager getViewPager() {
        return mVp;
    }

    public void setImageUrls(List<String> data) {
        this.mFunBannerParams.mImageUrls = data;
        this.initViewPager();
    }

    public void setImageUrlsAndTitles(List<String> urls, List<String> titles) {
        this.mFunBannerParams.mImageUrls = urls;
        this.mFunBannerParams.mTitles = titles;
        this.initViewPager();
    }

    public void setImageUrlsAndTitles(String[] urls, String[] titles) {
        this.mFunBannerParams.mImageUrls = Arrays.asList(urls);
        this.mFunBannerParams.mTitles = Arrays.asList(titles);
        this.initViewPager();
    }

    public void setImageUrls(String[] data) {
        List list = Arrays.asList(data);
        this.setImageUrls(list);
    }

    public void setImageUrlHost(String host) {
        this.mFunBannerParams.mHost = host;
    }

    public void setImageResIds(int[] resIds) {
        this.mFunBannerParams.mImagesResIds = resIds;
        this.initViewPager();
    }

    public static class Builder {
        private final FunBanner.FunBannerParams mFunBannerParams = new FunBanner.FunBannerParams();
        private final Context mContext;

        public Builder(Context context) {
            this.mContext = context;
        }

        public FunBanner.Builder setEnableAutoLoop(boolean enableAutoLoop) {
            this.mFunBannerParams.mEnableAutoLoop = enableAutoLoop;
            return this;
        }

        public FunBanner.Builder setLoopInterval(int loopInterval) {
            this.mFunBannerParams.mLoopInterval = loopInterval;
            return this;
        }

        public FunBanner.Builder setDotRadius(int dotRadius) {
            this.mFunBannerParams.mDotRadius = dotRadius;
            return this;
        }

        public FunBanner.Builder setDotNormalColor(int dotNormalColor) {
            this.mFunBannerParams.mDotNormalColor = dotNormalColor;
            return this;
        }

        public FunBanner.Builder setDotSelectedColor(int dotSelectedColor) {
            this.mFunBannerParams.mDotSelectedColor = dotSelectedColor;
            return this;
        }

        public FunBanner.Builder setShowIndicator(boolean showIndicator) {
            this.mFunBannerParams.mShowIndicator = showIndicator;
            return this;
        }

        public FunBanner.Builder setIndicatorBackgroundColor(int indicatorBackgroundColor) {
            this.mFunBannerParams.mIndicatorBarBackgroundColor = indicatorBackgroundColor;
            return this;
        }

        public FunBanner.Builder setIndicatorBarHeight(int height) {
            this.mFunBannerParams.mIndicatorBarHeight = height;
            return this;
        }

        public FunBanner.Builder setHeightWidthRatio(float ratio) {
            this.mFunBannerParams.mHeightWidthRatio = ratio;
            return this;
        }

        public FunBanner.Builder setImageResIds(int[] resIds) {
            this.mFunBannerParams.mImagesResIds = resIds;
            return this;
        }

        public FunBanner.Builder setImageUrlHost(String host) {
            this.mFunBannerParams.mHost = host;
            return this;
        }

        public FunBanner.Builder setImageUrls(List<String> data) {
            this.mFunBannerParams.mImageUrls = data;
            return this;
        }

        public FunBanner.Builder setImageUrls(String[] data) {
            List list = Arrays.asList(data);
            this.setImageUrls(list);
            return this;
        }

        public FunBanner.Builder setTitles(List<String> titles) {
            this.mFunBannerParams.mTitles = titles;
            return this;
        }

        public FunBanner.Builder setTitles(String[] titles) {
            List list = Arrays.asList(titles);
            this.setTitles(list);
            return this;
        }

        public FunBanner.Builder setTitleColor(int color) {
            this.mFunBannerParams.mTitleColor = color;
            return this;
        }

        public FunBanner.Builder setHeight(int height) {
            this.mFunBannerParams.mHeight = height;
            return this;
        }

        public FunBanner.Builder setClipChildren(boolean clipChildren) {
            this.mFunBannerParams.mClipChildren = clipChildren;
            return this;
        }

        public FunBanner.Builder setChildOffset(int offset) {
            this.mFunBannerParams.mChildOffset = offset;
            return this;
        }

        public FunBanner.Builder setChildMargin(int margin) {
            this.mFunBannerParams.mChildMargin = margin;
            return this;
        }

        public FunBanner.Builder setChildCornerSize(int size) {
            this.mFunBannerParams.mChildCornerSize = size;
            return this;
        }

        public FunBanner build() {
            FunBanner funBanner = new FunBanner(this.mContext, null);
            this.mFunBannerParams.apply(funBanner);
            funBanner.initViewPager();
            return funBanner;
        }
    }

    private static class FunBannerParams {
        private boolean mEnableAutoLoop;
        private int mLoopInterval;
        private int mDotRadius;
        private List<String> mImageUrls;
        private List<String> mTitles;
        private String mHost;
        private int[] mImagesResIds;
        private int mDotNormalColor;
        private int mDotSelectedColor;
        private boolean mShowIndicator;
        private int mIndicatorBarBackgroundColor;
        private float mHeightWidthRatio;
        private int mIndicatorBarHeight;
        private int mTitleColor;
        private int mHeight;
        private boolean mClipChildren;
        private int mChildOffset;
        private int mChildMargin;
        private int mChildCornerSize; // 圆角大小

        private FunBannerParams() {
            this.mLoopInterval = 3000;
            this.mDotRadius = 0;
            this.mHost = "";
            this.mShowIndicator = true;
        }

        public void apply(FunBanner funBanner) {
            funBanner.mFunBannerParams.mEnableAutoLoop = this.mEnableAutoLoop;
            funBanner.mFunBannerParams.mLoopInterval = this.mLoopInterval;
            if (this.mDotRadius > 0) {
                funBanner.mFunBannerParams.mDotRadius = this.mDotRadius;
            }

            if (this.mImageUrls != null) {
                funBanner.mFunBannerParams.mImageUrls = this.mImageUrls;
            }

            if (this.mHost.length() > 0) {
                funBanner.mFunBannerParams.mHost = this.mHost;
            }

            if (this.mImagesResIds != null) {
                funBanner.mFunBannerParams.mImagesResIds = this.mImagesResIds;
            }

            if (this.mDotNormalColor != 0) {
                funBanner.mFunBannerParams.mDotNormalColor = this.mDotNormalColor;
            }

            if (this.mDotSelectedColor != 0) {
                funBanner.mFunBannerParams.mDotSelectedColor = this.mDotSelectedColor;
            }

            funBanner.mFunBannerParams.mShowIndicator = this.mShowIndicator;
            if (this.mIndicatorBarBackgroundColor != 0) {
                funBanner.mFunBannerParams.mIndicatorBarBackgroundColor = this.mIndicatorBarBackgroundColor;
            }

            if (this.mHeightWidthRatio != 0.0F) {
                funBanner.mFunBannerParams.mHeightWidthRatio = this.mHeightWidthRatio;
            }

            if (this.mIndicatorBarHeight != 0) {
                funBanner.mFunBannerParams.mIndicatorBarHeight = this.mIndicatorBarHeight;
            }

            if (this.mTitles != null) {
                funBanner.mFunBannerParams.mTitles = this.mTitles;
            }

            if (this.mTitleColor != 0) {
                funBanner.mFunBannerParams.mTitleColor = this.mTitleColor;
            }

            if (this.mHeight != 0) {
                funBanner.mFunBannerParams.mHeight = this.mHeight;
            }

            funBanner.mFunBannerParams.mClipChildren = this.mClipChildren;
            if (this.mChildOffset != 0) {
                funBanner.mFunBannerParams.mChildOffset = mChildOffset;
            }
            if (this.mChildMargin != 0) {
                funBanner.mFunBannerParams.mChildMargin = mChildMargin;
            }
            if (this.mChildCornerSize != 0) {
                funBanner.mFunBannerParams.mChildCornerSize = mChildCornerSize;
            }
        }
    }

    int mDownX, mDownY;
    int mOffsetX, mOffsetY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean dispatch = super.dispatchTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mOffsetX = (int) ev.getY() - mDownY;
                mOffsetY = (int) ev.getY() - mDownY;
                if (Math.abs(mOffsetY) > Math.abs(mOffsetX) && Math.abs(mOffsetY) < mTouchSlop) {
                    return false;
                }
                break;
        }
        return dispatch;

        //        if (this.mFunBannerParams != null
        //                && this.mVp != null
        //                && !this.mFunBannerParams.mClipChildren) {
        //            return this.mVp.dispatchTouchEvent(ev);
        //        }
        //        return super.dispatchTouchEvent(ev);
    }
}

