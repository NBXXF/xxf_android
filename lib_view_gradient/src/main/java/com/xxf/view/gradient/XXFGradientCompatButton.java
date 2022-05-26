package com.xxf.view.gradient;

import android.content.Context;
import android.util.AttributeSet;

import com.xxf.view.round.XXFRoundButton;

/**
 * @Description: eg app:radius="4dp"
 * <p>
 * 渐变属性如下
 * <attr name="start_color" format="color|reference" />
 * <attr name="center_color" format="color|reference" />
 * <attr name="end_color" format="color|reference" />
 * <attr name="radius_top_left" format="float" />
 * <attr name="radius_top_right" format="float" />
 * <attr name="radius_bottom_right" format="float" />
 * <attr name="radius_bottom_left" format="float" />
 * <attr name="orientation" format="enum">
 * <enum name="TOP_BOTTOM" value="0" />
 * <enum name="TR_BL" value="1" />
 * <enum name="RIGHT_LEFT" value="2" />
 * <enum name="BR_TL" value="3" />
 * <enum name="BOTTOM_TOP" value="4" />
 * <enum name="BL_TR" value="5" />
 * <enum name="LEFT_RIGHT" value="6" />
 * <enum name="TL_BR" value="7" />
 * </attr>
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/21/21 5:38 PM
 * Description: 渐变背景组件
 */
public class XXFGradientCompatButton extends XXFRoundButton {
    public XXFGradientCompatButton(Context context) {
        super(context);
    }

    public XXFGradientCompatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        GradientUtils.setGradientBackground(context, this, attrs);
    }

    public XXFGradientCompatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        GradientUtils.setGradientBackground(context, this, attrs);
    }
}
