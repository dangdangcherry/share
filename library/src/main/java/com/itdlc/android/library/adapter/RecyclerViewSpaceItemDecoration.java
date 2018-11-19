package com.itdlc.android.library.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Felear on 2017/9/11 0011.
 * 设置RecyclerView Item 间距
 */

public class RecyclerViewSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int spanCount;
    private int mDecorationColor = Color.TRANSPARENT;

    public RecyclerViewSpaceItemDecoration(int space) {
        this.space = space;
    }

    public RecyclerViewSpaceItemDecoration(int space, int spanCount) {
        this.space = space;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.bottom = space;

        //由于每行都只有spanCount个，所以第一个都是spanCount的倍数，把左边距设为0
        if (spanCount > 0 && parent.getChildLayoutPosition(view) % spanCount != 0) {
            outRect.left = space;
        }
    }

    public RecyclerViewSpaceItemDecoration setColor(@ColorInt int color) {
        mDecorationColor = color;
        return this;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        Paint dividerPaint = new Paint();
        dividerPaint.setColor(mDecorationColor);

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + space;
            c.drawRect(left + 40, top, right - 40, bottom, dividerPaint);
        }
    }


}
