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
            } else {
                clipViewRoundRect(view, radius)
            }
            radiusTr.recycle()
        }
    }

    fun clipViewRadius(view: View, radius: Float) {
        val dp360 = dip2px(view.context, 360f)
        if (radius >= dp360) {
            clipViewCircle(view)
        } else {
            clipViewRoundRect(view, radius.toInt())
        }
    }

    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}