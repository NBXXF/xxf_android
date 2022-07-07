package com.xxf.view.view.bar

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.View.MeasureSpec.EXACTLY
import android.view.WindowInsets
import androidx.annotation.CallSuper
import com.xxf.utils.BarUtils
import com.xxf.view.round.XXFRoundView

/**
 * 状态栏 固定高度
 */
open class StatusBarPlaceHolder : XXFRoundView {
    private var mBarHeight = -1

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mBarHeight < 0) {
            resetBarHeight()
        }
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(
                mBarHeight, EXACTLY
            )
        )
    }

    @CallSuper
    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        return super.onApplyWindowInsets(insets)
        resetBarHeight()
    }

    private fun resetBarHeight() {
        mBarHeight = getBarHeight()
    }

    open fun getBarHeight(): Int {
        return BarUtils.getStatusBarHeight()
    }

    @CallSuper
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        resetBarHeight()
    }
}