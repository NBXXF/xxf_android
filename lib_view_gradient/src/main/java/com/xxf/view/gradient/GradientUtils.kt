package com.xxf.view.gradient

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/21/21 5:21 PM
 * Description: 渐变处理
 */
object GradientUtils {
    /**
     * 是否有渐变属性
     *
     * @param context
     * @param attrs
     * @return
     */
    fun hasGradientAttr(context: Context, attrs: AttributeSet?): Boolean {
        var hasGradientAttr = false
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.GradientLayout, 0, 0)
            hasGradientAttr = a.hasValue(R.styleable.GradientLayout_start_color) && a.hasValue(R.styleable.GradientLayout_end_color)
            a.recycle()
        }
        return hasGradientAttr
    }

    /**
     * 设置渐变背景
     *
     * @param context
     * @param view
     * @param attrs
     * @return
     */
    fun setGradientBackground(context: Context,
                              view: View,
                              attrs: AttributeSet?): Boolean {
        if (hasGradientAttr(context, attrs)) {
            view.background = GradientDrawableBuilder(context, attrs).build()
            return true
        }
        return false
    }
}