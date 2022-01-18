package com.xxf.view.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.xxf.view.utils.SystemUtils;

/**
 * @version 2.2.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 搜索页面  键盘关闭的时机:1:点击 2:滚动, 默认会清除焦点
 * @date createTime：2017/11/21
 */
public class SearchManageSoftKeyboardLayout extends SoftKeyboardSizeWatchLayout {

    public SearchManageSoftKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private GestureDetector gestureDetector;

    public GestureDetector getGestureDetector() {
        if (gestureDetector == null) {
            init();
        }
        return gestureDetector;
    }

    private void init() {
        if (gestureDetector == null) {
            gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    closeSoftKeyboardLayout();
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    closeSoftKeyboardLayout();
                    return false;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    Log.d("==========>", "onScroll");
                    closeSoftKeyboardLayout();
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    closeSoftKeyboardLayout();
                    return false;
                }
            });
        }
    }

    /**
     * 清除焦点
     */
    public void closeSoftKeyboardLayout() {
        Context context = getContext();
        if (context instanceof Activity) {
            SystemUtils.hideSoftKeyBoard((Activity) context, true);
        } else if (context instanceof ContextWrapper) {
            ContextWrapper contextWrapper = (ContextWrapper) context;
            Context baseContext = contextWrapper.getBaseContext();
            if (baseContext instanceof Activity) {
                SystemUtils.hideSoftKeyBoard((Activity) baseContext, true);
            }
        }
        this.setFocusable(true);
        this.requestFocus();
        this.findFocus();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getGestureDetector().onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }
}
