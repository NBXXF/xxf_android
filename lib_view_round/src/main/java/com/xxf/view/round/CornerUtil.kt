package com.xxf.view.round

import android.graphics.Outline
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider

/**
 * @Description: view裁切工具类
 * @Author: XGod
 * @CreateDate: 2018/6/25 10:50
 *
 */
object CornerUtil {
    @JvmOverloads
    fun clipViewCircle(view: View) {
        view.clipToOutline = true
        view.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setOval(0, 0, view.width, view.height)
            }
        }
    }

    @JvmOverloads
    fun clipViewRoundRect(view: View, radius: Float) {
        view.clipToOutline = true
        view.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, radius)
            }
        }
    }

    @JvmOverloads
    fun clipViewRoundRect(
        view: View,
        mTopLeft: Float,
        mTopRight: Float,
        mBottomLeft: Float,
        mBottomRight: Float
    ) {
        view.clipToOutline = true
        view.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                fun calculateBounds(): RectF {
                    // 没有处理Padding的逻辑
                    return RectF(0f, 0f, view.width.toFloat(), view.height.toFloat())
                }
                //如果是单独的圆角
                val path = Path()
                path.addRoundRect(
                    calculateBounds(),
                    floatArrayOf(
                        mTopLeft,
                        mTopLeft,
                        mTopRight,
                        mTopRight,
                        mBottomRight,
                        mBottomRight,
                        mBottomLeft,
                        mBottomLeft
                    ),
                    Path.Direction.CCW
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    outline.setPath(path)
                } else {
                    //不支持2阶的曲线
                    outline.setConvexPath(path)
                }
            }
        }
    }

    fun clipView(view: View?, attrs: AttributeSet?) {
        if (view != null && attrs != null) {
            val radiusTr = view.context.obtainStyledAttributes(attrs, R.styleable.xxf_radius_style)
            if (radiusTr.hasValue(R.styleable.xxf_radius_style_radius)) {
                val radius = radiusTr.getDimensionPixelSize(R.styleable.xxf_radius_style_radius, 0)
                clipViewRadius(view,radius.toFloat())
            } else {
                val topLeftRadius =
                    radiusTr.getDimensionPixelSize(R.styleable.xxf_radius_style_topLeftRadius, 0)
                val topRightRadius =
                    radiusTr.getDimensionPixelSize(R.styleable.xxf_radius_style_topRightRadius, 0)
                val bottomLeftRadius =
                    radiusTr.getDimensionPixelSize(R.styleable.xxf_radius_style_bottomLeftRadius, 0)
                val bottomRightRadius = radiusTr.getDimensionPixelSize(
                    R.styleable.xxf_radius_style_bottomRightRadius,
                    0
                )
                if (topLeftRadius > 0 || topRightRadius > 0 || bottomLeftRadius > 0 || bottomRightRadius > 0) {
                    clipViewRoundRect(
                        view,
                        topLeftRadius.toFloat(),
                        topRightRadius.toFloat(),
                        bottomLeftRadius.toFloat(),
                        bottomRightRadius.toFloat()
                    )
                } else {
                    clearClip(view)
                }
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
        val dp360 = dip2px(view, 360f)
        if (radius >= dp360) {
            clipViewCircle(view)
        } else if (radius > 0) {
            clipViewRoundRect(view, radius)
        } else {
            clearClip(view)
        }
    }

    private fun dip2px(view: View, dpValue: Float): Int {
        if (view.isInEditMode) return (dpValue * 3).toInt()
        val scale = view.context.resources.displayMetrics.density
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