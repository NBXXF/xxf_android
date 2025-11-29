package com.xxf.view.gradient

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import com.xxf.view.gradient.R

class GradientDrawableBuilder {
    private var startColor = 0
    private var centerColor = 0
    private var endColor = 0
    private var orientation: GradientDrawable.Orientation? = GradientDrawable.Orientation.TOP_BOTTOM
    private var radiusCornerTopLeft = 0f
    private var radiusCornerTopRight = 0f
    private var radiusCornerBottomRight = 0f
    private var radiusCornerBottomLeft = 0f

    constructor()

    constructor(context: Context, attrs: AttributeSet?) {
        if(attrs==null){
            return
        }
        val a = context.obtainStyledAttributes(attrs, R.styleable.GradientLayout, 0, 0)
        this.startColor = a.getColor(R.styleable.GradientLayout_start_color, -1)
        this.centerColor = a.getColor(R.styleable.GradientLayout_center_color, -1)
        this.endColor = a.getColor(R.styleable.GradientLayout_end_color, -1)

        val radiusTr = context.obtainStyledAttributes(attrs, com.xxf.view.round.R.styleable.xxf_radius_style)
        if (radiusTr.hasValue(com.xxf.view.round.R.styleable.xxf_radius_style_radius)) {
            val radius =
                radiusTr.getDimensionPixelSize(com.xxf.view.round.R.styleable.xxf_radius_style_radius, 0).toFloat()
            this.radiusCornerTopLeft = radius
            this.radiusCornerTopRight = radius
            this.radiusCornerBottomLeft = radius
            this.radiusCornerBottomRight = radius
        } else {
            this.radiusCornerTopLeft =
                radiusTr.getDimensionPixelSize(com.xxf.view.round.R.styleable.xxf_radius_style_topLeftRadius, 0)
                    .toFloat()
            this.radiusCornerTopRight =
                radiusTr.getDimensionPixelSize(com.xxf.view.round.R.styleable.xxf_radius_style_topRightRadius, 0)
                    .toFloat()
            this.radiusCornerBottomLeft =
                radiusTr.getDimensionPixelSize(com.xxf.view.round.R.styleable.xxf_radius_style_bottomLeftRadius, 0)
                    .toFloat()
            this.radiusCornerBottomRight = radiusTr.getDimensionPixelSize(
                com.xxf.view.round.R.styleable.xxf_radius_style_bottomRightRadius,
                0
            ).toFloat()
        }

        var attrOrientation = 0
        /**
         * fix 线性布局本身有orientation 误导
         */
        attrOrientation = if (a.hasValue(R.styleable.GradientLayout_gradient_orientation)) {
            a.getInt(R.styleable.GradientLayout_gradient_orientation, 0)
        } else {
            a.getInt(R.styleable.GradientLayout_orientation, 0)
        }
        this.orientation = intToOrientation(attrOrientation)
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

    fun setOrientation(
        orientation: GradientDrawable.Orientation?
    ): GradientDrawableBuilder {
        this.orientation = orientation
        return this
    }

    fun setRadiusCornerTopLeft(radius: Float): GradientDrawableBuilder {
        this.radiusCornerTopLeft = radius
        return this
    }

    fun setRadiusCornerTopRight(radius: Float): GradientDrawableBuilder {
        this.radiusCornerTopRight = radius
        return this
    }

    fun setRadiusCornerBottomRight(radius: Float): GradientDrawableBuilder {
        this.radiusCornerBottomRight = radius
        return this
    }

    fun setRadiusCornerBottomLeft(radius: Float): GradientDrawableBuilder {
        this.radiusCornerBottomLeft = radius
        return this
    }


    fun build(): GradientDrawable {
        populateMissingColors()
        val colors = if (centerColor != -1) {
            intArrayOf(
                this.startColor,
                centerColor,
                endColor
            )
        } else {
            intArrayOf(this.startColor, this.endColor)
        }
        val validOrientation = if (this.orientation == null
        ) GradientDrawable.Orientation.TOP_BOTTOM
        else orientation!!
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
        if (this.endColor == -1 && this.startColor == -1) {
            this.startColor = Color.RED
        }

        if (this.endColor == -1) {
            this.endColor = lighter(this.startColor, 0.5f)
            return
        }

        if (this.startColor == -1) {
            this.startColor = lighter(this.endColor, 0.5f)
        }
    }

    private fun lighter(color: Int, factor: Float): Int {
        val red = ((Color.red(color) * (1 - factor) / 255 + factor) * 255).toInt()
        val green = ((Color.green(color) * (1 - factor) / 255 + factor) * 255).toInt()
        val blue = ((Color.blue(color) * (1 - factor) / 255 + factor) * 255).toInt()
        return Color.argb(Color.alpha(color), red, green, blue)
    }
}
