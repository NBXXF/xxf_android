package com.xxf.arch.widget;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @version 2.3.0
 * @Description 监听dialog外部点击事件
 * @date createTime：2018/1/4
 */
public abstract class TouchListenDialog extends AppCompatDialog {
    public TouchListenDialog(Context context) {
        super(context);
    }

    public TouchListenDialog(Context context, int theme) {
        super(context, theme);
    }

    protected TouchListenDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected abstract void onDialogTouchOutside(MotionEvent event);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /* 触摸外部弹窗 */
        if (isOutOfBounds(getContext(), event)) {
            onDialogTouchOutside(event);
        }
        return super.onTouchEvent(event);
    }

    private boolean isOutOfBounds(Context context, MotionEvent event) {
        try {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
            final View decorView = getWindow().getDecorView();
            return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop))
                    || (y > (decorView.getHeight() + slop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
