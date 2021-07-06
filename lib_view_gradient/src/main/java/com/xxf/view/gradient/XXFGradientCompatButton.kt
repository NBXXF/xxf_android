package com.xxf.view.gradient

import android.content.Context
import android.util.AttributeSet
import com.xxf.view.round.XXFRoundButton

/**
 * @Description: eg app:radius="4dp"
 *
 *
 * 渐变属性如下
 * <attr name="start_color" format="color|reference"></attr>
 * <attr name="center_color" format="color|reference"></attr>
 * <attr name="end_color" format="color|reference"></attr>
 * <attr name="radius_top_left" format="float"></attr>
 * <attr name="radius_top_right" format="float"></attr>
 * <attr name="radius_bottom_right" format="float"></attr>
 * <attr name="radius_bottom_left" format="float"></attr>
 * <attr name="orientation" format="enum">
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
 * Date: 1/21/21 5:38 PM
 * Description: 渐变背景组件
 */
class XXFGradientCompatButton : XXFRoundButton {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        GradientUtils.setGradientBackground(context, this, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        GradientUtils.setGradientBackground(context, this, attrs)
    }
}