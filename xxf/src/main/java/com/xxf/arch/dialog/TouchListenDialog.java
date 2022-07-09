package com.xxf.arch.dialog;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.appcompat.app.AppCompatDialog;

import com.xxf.arch.component.ObservableComponent;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.0
 * @Description 监听dialog外部点击事件
 * @date createTime：2018/1/4
 */
public abstract class TouchListenDialog<E> extends XXFDialog<E> implements ObservableComponent<AppCompatDialog,E> {

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
