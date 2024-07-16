package com.xxf.view.gradient

import android.content.Context
import android.util.AttributeSet
import com.xxf.view.gradient.GradientUtils.setGradientBackground
import com.xxf.view.round.XXFRoundTextView

/**
 * @Description: eg app:radius="4dp"
 *
 * <attr name="radius" format="dimension"></attr>
 *
 * <attr name="topLeftRadius" format="dimension"></attr>
 *
 * <attr name="topRightRadius" format="dimension"></attr>
 *
 * <attr name="bottomLeftRadius" format="dimension"></attr>
 *
 * <attr name="bottomRightRadius" format="dimension"></attr>
 *
 *
 *
 * 渐变属性如下
 * <attr name="start_color" format="color|reference"></attr>
 * <attr name="center_color" format="color|reference"></attr>
 * <attr name="end_color" format="color|reference"></attr>
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
 * <attr name="gradient_orientation" format="enum">
 * <enum name="TOP_BOTTOM" value="0"></enum>
 * <enum name="TR_BL" value="1"></enum>
 * <enum name="RIGHT_LEFT" value="2"></enum>
 * <enum name="BR_TL" value="3"></enum>
 * <enum name="BOTTOM_TOP" value="4"></enum>
 * <enum name="BL_TR" value="5"></enum>
 * <enum name="LEFT_RIGHT" value="6"></enum>
 * <enum name="TL_BR" value="7"></enum>
</attr> *
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/21/21 5:40 PM
 * Description: 渐变组件
 */
class XXFGradientCompatTextView : XXFRoundTextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(
        context, attrs
    ) {
        setGradientBackground(context, this, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        setGradientBackground(context, this, attrs)
    }
}
