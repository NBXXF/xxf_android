package com.xxf.view.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xxf.arch.utils.DensityUtil;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 扩展 {@link android.support.v7.widget.DividerItemDecoration}
 */
public class SmartDividerItemDecoration extends DividerItemDecoration {

    private int px;
    private Drawable mDivider;
    private int mOrientation;

    public SmartDividerItemDecoration(Context context, int orientation) {
        super(context, orientation);
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        this.mOrientation = orientation;
    }

    @CallSuper
    @Override
    public void setDrawable(@NonNull Drawable drawable) {
        super.setDrawable(drawable);
        this.mDivider = drawable;
    }

    /**
     * 设置分割线
     *
     * @param drawable
     * @param px
     */
    public SmartDividerItemDecoration setDrawable(@NonNull Drawable drawable, int px) {
        this.px = px;
        this.setDrawable(drawable);
        return this;
    }

    /**
     * 设置分割线
     *
     * @param color
     * @param px
     */
    public SmartDividerItemDecoration setDrawable(@ColorInt int color, int px) {
        this.px = px;
        ColorDrawable colorDrawable = new ColorDrawable(color);
        this.setDrawable(colorDrawable);
        return this;
    }

    /**
     * 设置分割线 dp
     *
     * @param color
     * @param dp
     */
    public SmartDividerItemDecoration setDrawableDp(@ColorInt int color, int dp) {
        this.px = DensityUtil.dip2px(dp);
        ColorDrawable colorDrawable = new ColorDrawable(color);
        this.setDrawable(colorDrawable);
        return this;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, 0, px > 0 ? px : mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, px > 0 ? px : mDivider.getIntrinsicWidth(), 0);
        }
    }
}
