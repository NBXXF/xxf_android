package com.xxf.view.view.bar

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.View.MeasureSpec.EXACTLY
import android.view.WindowInsets
import androidx.annotation.CallSuper
import com.xxf.application.activity.findActivity
import com.xxf.utils.BarUtils
import com.xxf.view.round.XXFRoundView

/**
 * 导航栏 导航栏系统没显示 高度就自动为0,如果系统显示了导航栏 就显示为导航栏的高度
 */
open class NavigationBar : NavigationBarPlaceHolder {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun getBarHeight(): Int {
        if (!BarUtils.isSupportNavBar()) {
            return 0
        }
        val findActivity = context.findActivity()
        if (findActivity != null && !BarUtils.isStatusBarVisible(findActivity)) {
            return 0
        }
        return super.getBarHeight()
    }
}