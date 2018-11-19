package com.itdlc.android.library.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;

/**
 * Created by jjs on 2018/4/9.
 */

public class SpanUtils {
    private SpannableStringBuilder mBuilder = new SpannableStringBuilder();
    private String mText = "";
    private int mDefaultColor;

    public static SpanUtils getInstance() {
        return new SpanUtils();
    }

    /**
     * 添加金额->将自动添加￥符号
     */
    public SpanUtils appendPrice(String price, @ColorInt int color, int textSize) {
        if (!TextUtils.isEmpty(price))
            append("￥").setColor(color).setTextSize(textSize - 8).append(price).setColor(color).setTextSize(textSize);
        return this;
    }

    public SpanUtils setDefaultColor(@ColorInt int color) {
        mDefaultColor = color;
        return this;
    }

    public SpanUtils appendLine(String str) {
        mBuilder.append(System.getProperty("line.separator"));
        return append(str);
    }

    public SpanUtils append(String str) {
        if (str==null){
            str="";
        }
        mBuilder.append(str);
        mText = str;
        if (mDefaultColor != 0) {
            setColor(mDefaultColor);
        }
        return this;
    }

    public SpanUtils setColor(@ColorInt int color) {
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        initSpan(span);
        return this;
    }

    /**
     * sp
     */
    public SpanUtils setTextSize(int textSize) {
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(DeviceUtils.sp2px(textSize));
        initSpan(span);
        return this;
    }

    public SpanUtils setBullet(int color) {
        return setBullet(color, DeviceUtils.dip2px(4), DeviceUtils.dip2px(4));
    }

    public SpanUtils setBullet(int color, int bulletRadius, int bulletGapWidth) {
        append(" ");
        CustomBulletSpan span = new CustomBulletSpan(color, bulletRadius, bulletGapWidth);
        initSpan(span);
        return this;
    }

    public SpanUtils addClickSpan(ClickableSpan span) {
        initSpan(span);
        return this;
    }

    /**
     * 返回供View使用
     */
    public SpannableStringBuilder create() {
        return mBuilder;
    }

    private void initSpan(Object span) {
        mBuilder.setSpan(span, mBuilder.length() - mText.length(), mBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    class CustomBulletSpan implements LeadingMarginSpan {

        private final int color;
        private final int radius;
        private final int gapWidth;

        private Path sBulletPath = null;

        private CustomBulletSpan(final int color, final int radius, final int gapWidth) {
            this.color = color;
            this.radius = radius;
            this.gapWidth = gapWidth;
        }

        public int getLeadingMargin(final boolean first) {
            return 2 * radius + gapWidth;
        }

        public void drawLeadingMargin(final Canvas c, final Paint p, final int x, final int dir,
                                      final int top, final int baseline, final int bottom,
                                      final CharSequence text, final int start, final int end,
                                      final boolean first, final Layout l) {
            if (((Spanned) text).getSpanStart(this) == start) {
                Paint.Style style = p.getStyle();
                int oldColor = 0;
                oldColor = p.getColor();
                p.setColor(color);
                p.setStyle(Paint.Style.FILL);
                if (c.isHardwareAccelerated()) {
                    if (sBulletPath == null) {
                        sBulletPath = new Path();
                        // Bullet is slightly better to avoid aliasing artifacts on mdpi devices.
                        sBulletPath.addCircle(0.0f, 0.0f, radius, Path.Direction.CW);
                    }
                    c.save();
                    c.translate(x + dir * radius, (top + bottom) / 2.0f);
                    c.drawPath(sBulletPath, p);
                    c.restore();
                } else {
                    c.drawCircle(x + dir * radius, (top + bottom) / 2.0f, radius, p);
                }
                p.setColor(oldColor);
                p.setStyle(style);
            }
        }
    }

}
