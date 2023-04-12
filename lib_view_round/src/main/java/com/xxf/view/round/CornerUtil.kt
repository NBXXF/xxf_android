package com.xxf.view.round

import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider

/**
 * @Description: view裁切工具类
 * @Author: XGod
 * @CreateDate: 2018/6/25 10:50
 */
object CornerUtil {
    fun clipViewCircle(view: View) {
        view.clipToOutline = true
        view.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setOval(0, 0, view.width, view.height)
            }
        }
    }

    fun clipViewRoundRect(view: View, radius: Int) {
        view.clipToOutline = true
        view.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, radius.toFloat())
            }
        }
    }

    fun clipView(view: View?, attrs: AttributeSet?) {
        if (view != null && attrs != null) {
            val radiusTr = view.context.obtainStyledAttributes(attrs, R.styleable.xxf_radius_style)
            val radius = radiusTr.getDimensionPixelSize(R.styleable.xxf_radius_style_radius, 0)
            val dp360 = dip2px(view.context, 360f)
            if (radius >= dp360) {
                clipViewCircle(view)
            } else if (radius > 0) {
                clipViewRoundRect(view, radius)
            } else {
                clearClip(view)
            }
            radiusTr.recycle()
        }
    }

    /**
     * 对于clip 为0的，只需要清除，不需要重新设置 outlineProvider
     */
    private fun clearClip(view: View) {
        view.clipToOutline = false
//        view.outlineProvider = ViewOutlineProvider.BACKGROUND
    }

    fun clipViewRadius(view: View, radius: Float) {
        val dp360 = dip2px(view.context, 360f)
        if (radius >= dp360) {
            clipViewCircle(view)
        } else if (radius > 0) {
            clipViewRoundRect(view, radius.toInt())
        } else {
            clearClip(view)
        }
    }

    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}

/**
 * 拓展 设置圆角
 * 360.dp 为圆角
 * 其他为圆角矩形
 */
fun View.setRadius(radius: Float) {
    CornerUtil.clipViewRadius(this, radius)
}