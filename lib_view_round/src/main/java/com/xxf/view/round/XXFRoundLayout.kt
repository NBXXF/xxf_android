package com.xxf.view.round

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * @Description: eg app:radius="4dp"
 * @Author: XGod
 * @CreateDate: 2018/6/25 15:41
 */
open class XXFRoundLayout : FrameLayout, XXFRoundWidget, XXFGrayWidget {
    private val grayWidgetHelper by lazy {
        GrayWidgetHelper()
    }

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        CornerUtil.clipView(this, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        CornerUtil.clipView(this, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        CornerUtil.clipView(this, attrs)
    }

    override fun setRadius(radius: Float) {
        CornerUtil.clipViewRadius(this, radius)
    }

    override fun draw(canvas: Canvas?) {
        grayWidgetHelper.draw(canvas)
        super.draw(canvas)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        grayWidgetHelper.dispatchDraw(canvas)
        super.dispatchDraw(canvas)
    }

    override fun setGrayColor(grayColor: Boolean) {
        grayWidgetHelper.setGrayColor(grayColor, this)
    }
}