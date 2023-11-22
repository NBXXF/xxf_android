package com.xxf.arch.dialog;

import android.content.Context;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.appcompat.app.AlertDialog;

import com.xxf.arch.component.ObservableComponent;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.0
 * @Description 监听dialog外部点击事件
 * @date createTime：2018/1/4
 */
public abstract class TouchListenAlertDialog<E> extends XXFAlertDialog<E> implements ObservableComponent<AlertDialog,E> {
    public TouchListenAlertDialog(Context context) {
        super(context);
    }
    public TouchListenAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    protected TouchListenAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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
