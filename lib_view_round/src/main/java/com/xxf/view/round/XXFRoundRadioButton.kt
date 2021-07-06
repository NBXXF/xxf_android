package com.xxf.view.round;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatRadioButton;


/**
 * @Description: eg app:radius="4dp"
 * @Author: XGod
 * @CreateDate: 2018/6/25 15:39
 */
public class XXFRoundRadioButton extends AppCompatRadioButton implements XXFRoundWidget {
    public XXFRoundRadioButton(Context context) {
        super(context);
    }

    public XXFRoundRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        CornerUtil.clipView(this, attrs);
    }

    public XXFRoundRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CornerUtil.clipView(this, attrs);
    }

    public void setRadius(float radius) {
        CornerUtil.clipViewRadius(this, radius);
    }

}
