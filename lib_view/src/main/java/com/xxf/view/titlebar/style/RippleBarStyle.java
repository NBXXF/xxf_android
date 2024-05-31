package com.xxf.view.titlebar.style;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.xxf.view.titlebar.TitleBarSupport;


public class RippleBarStyle extends TransparentBarStyle {

    @Override
    public Drawable getLeftTitleBackground(Context context) {
        Drawable drawable = createRippleDrawable(context);
        if (drawable != null) {
            return drawable;
        }
        return super.getLeftTitleBackground(context);
    }

    @Override
    public Drawable getRightTitleBackground(Context context) {
        Drawable drawable = createRippleDrawable(context);
        if (drawable != null) {
            return drawable;
        }
        return super.getRightTitleBackground(context);
    }

    /**
     * 获取水波纹的点击效果
     */
    public Drawable createRippleDrawable(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, typedValue, true)) {
            return TitleBarSupport.getDrawable(context, typedValue.resourceId);
        }
        return null;
    }
}