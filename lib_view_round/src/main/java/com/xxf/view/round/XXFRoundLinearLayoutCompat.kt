package com.xxf.view.round

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat

/**
 * @Description: eg app:radius="4dp"
<!-- 定义四个角的半径。 -->
<attr name="radius" format="dimension" />
<!-- 左上角的半径。 -->
<attr name="topLeftRadius" format="dimension" />
<!-- 右上角的半径。 -->
<attr name="topRightRadius" format="dimension" />
<!-- 左下角的半径。 -->
<attr name="bottomLeftRadius" format="dimension" />
<!-- 右下角的半径。 -->
<attr name="bottomRightRadius" format="dimension" />

 * @Author: XGod
 * @CreateDate: 2018/6/25 15:32
 */
open class XXFRoundLinearLayoutCompat : LinearLayoutCompat, XXFRoundWidget {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
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