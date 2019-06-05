package com.xxf.view.recyclerview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 扩展 {@link android.support.v7.widget.DividerItemDecoration}
 */
public class SmartDividerItemDecoration extends DividerItemDecoration {

    public SmartDividerItemDecoration(Context context, int orientation) {
        super(context, orientation);
    }

    @Override
    public void setDrawable(@NonNull Drawable drawable) {
        super.setDrawable(drawable);
    }

    /**
     * 设置分割线
     *
     * @param drawable
     * @param px
     */
    public void setDrawable(@NonNull Drawable drawable, int px) {
        try {
            if (px > 0) {
                drawable.setBounds(0, 0, px, px);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setDrawable(drawable);
    }

    /**
     * 设置分割线
     *
     * @param color
     * @param px
     */
    public void setDrawable(@ColorInt int color, int px) {
        ColorDrawable colorDrawable = new ColorDrawable(color);
        try {
            if (px > 0) {
                colorDrawable.setBounds(0, 0, px, px);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setDrawable(colorDrawable);
    }
}
