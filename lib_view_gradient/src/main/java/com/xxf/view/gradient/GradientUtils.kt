package com.xxf.view.gradient;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/21/21 5:21 PM
 * Description: 渐变处理
 */
public class GradientUtils {

    /**
     * 是否有渐变属性
     *
     * @param context
     * @param attrs
     * @return
     */
    public static boolean hasGradientAttr(Context context, final AttributeSet attrs) {
        boolean hasGradientAttr = false;
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GradientLayout, 0, 0);
            hasGradientAttr = a.hasValue(R.styleable.GradientLayout_start_color) && a.hasValue(R.styleable.GradientLayout_end_color);
            a.recycle();
        }
        return hasGradientAttr;
    }

    /**
     * 设置渐变背景
     *
     * @param context
     * @param view
     * @param attrs
     * @return
     */
    public static boolean setGradientBackground(final Context context,
                                                View view,
                                                final AttributeSet attrs) {
        if (hasGradientAttr(context, attrs)) {
            view.setBackground(new GradientDrawableBuilder(context, attrs).build());
            return true;
        }
        return false;
    }

}
