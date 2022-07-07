package com.xxf.view.view.bar

import android.content.Context
import android.util.AttributeSet
import com.xxf.application.activity.findActivity
import com.xxf.utils.BarUtils

/**
 * 状态栏 状态栏系统没显示 高度就自动为0,如果系统显示了状态栏 就显示为状态栏的高度
 */
open class StatusBar : StatusBarPlaceHolder {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun getBarHeight(): Int {
        val activity = context.findActivity()
        if (activity != null && !BarUtils.isStatusBarVisible(activity)) {
            return 0
        }
        return super.getBarHeight()
    }

}