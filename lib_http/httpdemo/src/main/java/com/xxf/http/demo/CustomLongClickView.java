package com.xxf.http.demo;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 3/16/21 2:49 PM
 * Description: 自定义长按时间的view
 */
public class CustomLongClickView extends FrameLayout {

    /**
     * 自定义长按时间
     */
    public long customLongClickDelayTime = 5000;

    public CustomLongClickView(Context context) {
        super(context);
    }

    public CustomLongClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLongClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean postDelayed(Runnable action, long delayMillis) {
        if (action!=null
                &&TextUtils.equals("CheckForLongPress",action.getClass().getSimpleName())) {
            return super.postDelayed(action, customLongClickDelayTime);
        }
        return super.postDelayed(action, delayMillis);
    }
}

