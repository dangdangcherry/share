package com.itdlc.android.library.widget.loopview;

//
// Source status recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class LoopViewPager extends ViewPager {
    private static final String TAG = "LoopViewPager";
    private static final boolean DEFAULT_BOUNDARY_CASHING = true;
    OnPageChangeListener mOuterPageChangeListener;
    private LoopPagerAdapterWrapper mAdapter;
    private boolean mBoundaryCaching = true;
    private boolean mEnableAutoLoop;
    private int mLoopInterval = 3000;
    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private float mPreviousOffset = -1.0F;
        private float mPreviousPosition = -1.0F;

        public void onPageSelected(int position) {
            int realPosition = LoopViewPager.this.mAdapter.toRealPosition(position);
            if(this.mPreviousPosition != (float)realPosition) {
                this.mPreviousPosition = (float)realPosition;
                if(LoopViewPager.this.mOuterPageChangeListener != null) {
                    LoopViewPager.this.mOuterPageChangeListener.onPageSelected(realPosition);
                }
            }

        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = position;
            if(LoopViewPager.this.mAdapter != null) {
                if(LoopViewPager.this.mAdapter != null) {
                    realPosition = LoopViewPager.this.mAdapter.toRealPosition(position);
                    if(positionOffset == 0.0F && this.mPreviousOffset == 0.0F && (position == 0 || position == LoopViewPager.this.mAdapter.getCount() - 1)) {
                        LoopViewPager.this.setCurrentItem(realPosition, false);
                    }
                }

                this.mPreviousOffset = positionOffset;
                if(LoopViewPager.this.mOuterPageChangeListener != null) {
                    if(realPosition != LoopViewPager.this.mAdapter.getRealCount() - 1) {
                        LoopViewPager.this.mOuterPageChangeListener.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
                    } else if((double)positionOffset > 0.5D) {
                        LoopViewPager.this.mOuterPageChangeListener.onPageScrolled(0, 0.0F, 0);
                    } else {
                        LoopViewPager.this.mOuterPageChangeListener.onPageScrolled(realPosition, 0.0F, 0);
                    }
                }

            }
        }

        public void onPageScrollStateChanged(int state) {
            if(LoopViewPager.this.mAdapter != null) {
                int position = LoopViewPager.super.getCurrentItem();
                int realPosition = LoopViewPager.this.mAdapter.toRealPosition(position);
                if(state == 0 && (position == 0 || position == LoopViewPager.this.mAdapter.getCount() - 1)) {
                    LoopViewPager.this.setCurrentItem(realPosition, false);
                }
            }

            if(LoopViewPager.this.mOuterPageChangeListener != null) {
                LoopViewPager.this.mOuterPageChangeListener.onPageScrollStateChanged(state);
            }

        }
    };
    private float mDownX;
    private float mDownY;
    private Runnable mLooper = new Runnable() {
        public void run() {
            int next = LoopViewPager.this.getCurrentItem() + 1;
            LoopViewPager.this.setCurrentItem(next);
            LoopViewPager.this.startLoop();
        }
    };

    public void setEnableAutoLoop(boolean enableAutoLoop) {
        this.mEnableAutoLoop = enableAutoLoop;
    }

    public void setLoopInterval(int loopInterval) {
        this.mLoopInterval = loopInterval;
    }

    public static int toRealPosition(int position, int count) {
        --position;
        if(position < 0) {
            position += count;
        } else {
            position %= count;
        }

        return position;
    }

    public void setBoundaryCaching(boolean flag) {
        this.mBoundaryCaching = flag;
        if(this.mAdapter != null) {
            this.mAdapter.setBoundaryCaching(flag);
        }

    }

    public void setAdapter(PagerAdapter adapter) {
        this.mAdapter = new LoopPagerAdapterWrapper(adapter);
        this.mAdapter.setBoundaryCaching(this.mBoundaryCaching);
        super.setAdapter(this.mAdapter);
        this.setCurrentItem(0, false);
    }

    public PagerAdapter getAdapter() {
        return (PagerAdapter)(this.mAdapter != null?this.mAdapter.getRealAdapter():this.mAdapter);
    }

    public int getCurrentItem() {
        return this.mAdapter != null?this.mAdapter.toRealPosition(super.getCurrentItem()):0;
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = this.mAdapter.toInnerPosition(item);
        super.setCurrentItem(realItem, smoothScroll);
    }

    public void setCurrentItem(int item) {
        if(this.getCurrentItem() != item) {
            this.setCurrentItem(item, true);
        }

    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOuterPageChangeListener = listener;
    }

    public LoopViewPager(Context context) {
        super(context);
        this.init();
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    private void init() {
        super.setOnPageChangeListener(this.onPageChangeListener);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
            case 0:
                this.mDownX = ev.getX();
                this.mDownY = ev.getY();
                this.stopLoop();
                break;
            case 1:
                this.startLoop();
                break;
            case 2:
                float moveX = ev.getX();
                float moveY = ev.getY();
                float dx = Math.abs(this.mDownX - moveX);
                float dy = Math.abs(this.mDownY - moveY);
                if(dx > dy) {
                    this.requestDisallowInterceptTouchEvent(true);
                }
        }

        return super.onTouchEvent(ev);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.startLoop();
    }

    private void startLoop() {
        if(this.mEnableAutoLoop) {
            this.removeCallbacks(this.mLooper);
            this.postDelayed(this.mLooper, (long)this.mLoopInterval);
        }

    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.stopLoop();
    }

    public void stopLoop() {
        if(this.mEnableAutoLoop) {
            this.removeCallbacks(this.mLooper);
        }

    }
}

