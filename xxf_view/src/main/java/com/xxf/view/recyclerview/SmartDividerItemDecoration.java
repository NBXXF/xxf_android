package com.xxf.view.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.v7.widget.DividerItemDecoration;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 扩展 {@link android.support.v7.widget.DividerItemDecoration}
 */
public class SmartDividerItemDecoration extends DividerItemDecoration {


    public SmartDividerItemDecoration(Context context, int orientation) {
        super(context, orientation);
    }

    /**
     * 设置分割线 dp
     *
     * @param color
     * @param dp
     */
    @SuppressLint("RestrictedApi")
    public SmartDividerItemDecoration setDrawable(@ColorInt int color, @IntRange(from = 0) final int dp) {
        this.setDrawable(new ColorDrawable(color) {
            @Override
            public int getIntrinsicHeight() {
                return dp > 0 ? -dp : super.getIntrinsicHeight();
            }

            @Override
            public int getIntrinsicWidth() {
                return dp > 0 ? -dp : super.getIntrinsicWidth();
            }
        });
        return this;
    }
}
