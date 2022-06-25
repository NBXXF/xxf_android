package com.xxf.view.round

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import skin.support.widget.SkinCompatLinearLayout

/**
 * @Description: eg app:radius="4dp"
 * @Author: XGod
 * @CreateDate: 2018/6/25 15:32
 */
open class XXFRoundLinearLayoutCompat : SkinCompatLinearLayout, XXFRoundWidget {
    constructor(context: Context) : super(context)
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

    override fun setRadius(radius: Float) {
        CornerUtil.clipViewRadius(this, radius)
    }
}