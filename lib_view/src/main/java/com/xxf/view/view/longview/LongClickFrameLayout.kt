package com.xxf.view.view.longview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.FrameLayout


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 3/16/21 2:49 PM
 * Description: 自定义长按时间的view
 */
class LongClickFrameLayout :FrameLayout,ILongClickView {
    /**
     * 默认两秒
     */
    override var customLongClickDelayTime: Long=2000L;
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun postDelayed(action: Runnable?, delayMillis: Long): Boolean {
        return if (action != null
            && TextUtils.equals("CheckForLongPress", action.javaClass.simpleName)
        ) {
            super.postDelayed(action, customLongClickDelayTime)
        } else super.postDelayed(action, delayMillis)
    }
}