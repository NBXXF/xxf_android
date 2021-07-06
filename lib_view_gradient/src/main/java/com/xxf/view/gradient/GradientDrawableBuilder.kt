package com.xxf.view.gradient

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet

class GradientDrawableBuilder {
    private var startColor = 0
    private var centerColor = 0
    private var endColor = 0
    private var orientation: GradientDrawable.Orientation? = GradientDrawable.Orientation.TOP_BOTTOM
    private var radiusCornerTopLeft = 0f
    private var radiusCornerTopRight = 0f
    private var radiusCornerBottomRight = 0f
    private var radiusCornerBottomLeft = 0f

    constructor() {}
    constructor(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.GradientLayout, 0, 0)
        startColor = a.getColor(R.styleable.GradientLayout_start_color, -1)
        centerColor = a.getColor(R.styleable.GradientLayout_center_color, -1)
        endColor = a.getColor(R.styleable.GradientLayout_end_color, -1)
        radiusCornerTopLeft = a.getFloat(R.styleable.GradientLayout_radius_top_left, 0f)
        radiusCornerTopRight = a.getFloat(R.styleable.GradientLayout_radius_top_right, 0f)
        radiusCornerBottomLeft = a.getFloat(R.styleable.GradientLayout_radius_bottom_left, 0f)
        radiusCornerBottomRight = a.getFloat(R.styleable.GradientLayout_radius_bottom_right, 0f)
        val attrOrientation = a.getInt(R.styleable.GradientLayout_orientation, 0)
        orientation = intToOrientation(attrOrientation)
        a.recycle()
    }

    fun setCenterColor(centerColor: Int) {
        this.centerColor = centerColor
    }

    fun setStartColor(startColor: Int): GradientDrawableBuilder {
        this.startColor = startColor
        return this
    }

    fun setEndColor(endColor: Int): GradientDrawableBuilder {
        this.endColor = endColor
        return this
    }

    fun setOrientation(orientation: GradientDrawable.Orientation?): GradientDrawableBuilder {
        this.orientation = orientation
        return this
    }

    fun setRadiusCornerTopLeft(radius: Float): GradientDrawableBuilder {
        radiusCornerTopLeft = radius
        return this
    }

    fun setRadiusCornerTopRight(radius: Float): GradientDrawableBuilder {
        radiusCornerTopRight = radius
        return this
    }

    fun setRadiusCornerBottomRight(radius: Float): GradientDrawableBuilder {
        radiusCornerBottomRight = radius
        return this
    }

    fun setRadiusCornerBottomLeft(radius: Float): GradientDrawableBuilder {
        radiusCornerBottomLeft = radius
        return this
    }

    fun build(): GradientDrawable {
        populateMissingColors()
        val colors: IntArray
        colors = if (centerColor != -1) {
            intArrayOf(startColor, centerColor, endColor)
        } else {
            intArrayOf(startColor, endColor)
        }
        val validOrientation = if (orientation == null) GradientDrawable.Orientation.TOP_BOTTOM else orientation!!
        val drawable = GradientDrawable(validOrientation, colors)
        val radii = FloatArray(8)
        radii[0] = radiusCornerTopLeft
        radii[1] = radiusCornerTopLeft
        radii[2] = radiusCornerTopRight
        radii[3] = radiusCornerTopRight
        radii[4] = radiusCornerBottomRight
        radii[5] = radiusCornerBottomRight
        radii[6] = radiusCornerBottomLeft
        radii[7] = radiusCornerBottomLeft
        drawable.cornerRadii = radii
        return drawable
    }

    private fun intToOrientation(original: Int): GradientDrawable.Orientation {
        return when (original) {
            1 -> GradientDrawable.Orientation.TR_BL
            2 -> GradientDrawable.Orientation.RIGHT_LEFT
            3 -> GradientDrawable.Orientation.BR_TL
            4 -> GradientDrawable.Orientation.BOTTOM_TOP
            5 -> GradientDrawable.Orientation.BL_TR
            6 -> GradientDrawable.Orientation.LEFT_RIGHT
            7 -> GradientDrawable.Orientation.TL_BR
            0 -> GradientDrawable.Orientation.TOP_BOTTOM
            else -> GradientDrawable.Orientation.TOP_BOTTOM
        }
    }

    private fun populateMissingColors() {
        if (endColor == -1 && startColor == -1) {
            startColor = Color.RED
        }
        if (endColor == -1) {
            endColor = lighter(startColor, 0.5f)
            return
        }
        if (startColor == -1) {
            startColor = lighter(endColor, 0.5f)
        }
    }

    private fun lighter(color: Int, factor: Float): Int {
        val red = ((Color.red(color) * (1 - factor) / 255 + factor) * 255).toInt()
        val green = ((Color.green(color) * (1 - factor) / 255 + factor) * 255).toInt()
        val blue = ((Color.blue(color) * (1 - factor) / 255 + factor) * 255).toInt()
        return Color.argb(Color.alpha(color), red, green, blue)
    }
}