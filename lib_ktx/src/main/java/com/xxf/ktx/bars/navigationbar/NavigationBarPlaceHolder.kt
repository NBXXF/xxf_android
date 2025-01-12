package com.xxf.ktx.bars.navigationbar

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.WindowInsets
import androidx.annotation.CallSuper
import com.xxf.ktx.findActivity
import kotlin.properties.Delegates

/**
 * 导航栏占位 固定高度
 */
open class NavigationBarPlaceHolder : View {
    private var mBarHeight = -1

    /**
     * 是否自动填充高度,
     * 否则为固定高度
     */
    var isAutoFillHeight by Delegates.observable(true) { p, o, n ->
        val oldBarHeight = mBarHeight
        resetBarHeight()
        if (oldBarHeight != mBarHeight) {
            requestLayout()
        }
    }

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

    private fun getBarHeight(): Int {
        val activity = context.findActivity()
        if (activity != null && !activity.isNavigationBarVisible) {
            return 0
        }
        return activity?.navigationBarHeight ?: 0
    }

    @CallSuper
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        resetBarHeight()
    }
}