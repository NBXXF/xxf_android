package com.xxf.ktx.internals;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.xxf.ktx.ContextKt;
import com.xxf.ktx.KeyboardKt;

/**
 * 触摸事件 关闭键盘
 * 不要转成kotlin!!!,不要转成kotlin!!!,不要转成kotlin!!!
 * onScroll 方法 第一个参数可能为空
 * Parameter specified as non-null is null: method com.xxf.ktx.internals.KeyboardHiddenTouchListener$onTouch$1.onScroll, parameter e1
 * com.xxf.ktx.internals.KeyboardHiddenTouchListener$onTouch$1.onScroll(Unknown Source:2)
 */
public class KeyboardHiddenTouchListener
        extends GestureDetector.SimpleOnGestureListener
        implements View.OnTouchListener {
    private GestureDetector gestureDetector;
    private View target;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        target = v;
        if (gestureDetector == null && v != null) {
            gestureDetector = new GestureDetector(v.getContext(), this);
        }
        if (gestureDetector != null && event != null) {
            gestureDetector.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {
        super.onShowPress(e);
        hideKeyboard();

    }


    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        hideKeyboard();
        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        hideKeyboard();
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    protected void hideKeyboard() {
        if (target != null) {
            Activity activity = ContextKt.findActivity(target.getContext());
            if (activity != null) {
                View focsusedView = activity.getCurrentFocus();
                if (focsusedView != null) {
                    /// 需要清除焦點,不然在搜索场景 搜索历史标签和默认列表无法切换,默认列表的规则是 没有焦点且输入框为空
                    focsusedView.clearFocus();
                }
            }
            KeyboardKt.hideKeyboard(target);
        }
    }

}
