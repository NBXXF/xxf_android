package com.xxf.view.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.viewpager.widget.ViewPager;

/**
 * 有边界动画的viewpager
 */
public class FlexibleViewPager extends ViewPager {
    private static final String TAG = "FlexibleViewPager::::";
    private boolean isMoveLeft = true;//左边是否能移动
    private boolean isMoveRight;
    private Rect normal = new Rect();//记录原来的位置
    private OnRefreshListener listener;
    float x = 0;//记录开始触摸的位置
    int xMove = 0;//移动的距离
    private int SCALE = 5;

    public FlexibleViewPager(Context context) {
        super(context);
    }

    public FlexibleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (x == 0) {
                    x = ev.getX();//记录位置
                }
                xMove = (int) (x - ev.getX()) / 2;//计算移动距离
                Log.e(TAG, "xMove:" + xMove + "isMoveLeft:" + isMoveLeft + "isMoveRight:" + isMoveRight);
                if (isMoveLeft && xMove <= 0 || isMoveRight && xMove >= 0) {//移动位置
                    this.layout(-xMove, normal.top, normal.right - xMove, normal.bottom);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP://多点触摸
            case MotionEvent.ACTION_POINTER_INDEX_SHIFT:
                if (isMoveLeft || isMoveRight) {
                    animation(xMove);//还原位置
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {//监听viewpager是否是第一页或最后一页
        if (getAdapter() == null && getAdapter().getCount() == 0) {
            isMoveLeft = false;
            isMoveRight = false;
        } else if (position == 0 && offset == 0 && offsetPixels == 0) {
            isMoveLeft = true;
        } else if (position == getAdapter().getCount() - 1 && offset == 0 && offsetPixels == 0) {
            isMoveRight = true;
        } else {
            isMoveLeft = false;
            isMoveRight = false;
        }
        if (normal.isEmpty() || normal.top - normal.bottom == 0) {
            normal.set(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());//viewpager记录原来位置
        }
        super.onPageScrolled(position, offset, offsetPixels);
    }

    /***
     * 回缩动画
     */
    public void animation(int moveX) {
        if (listener != null) {
            if (moveX > getWidth() / SCALE) {//滑动的距离超过屏幕的1/SCALE才回调
                listener.onLoadMore();
            } else if (moveX < -getWidth() / SCALE) {
                listener.onRefresh();
            }
        }
        x = 0;
        xMove = 0;
        TranslateAnimation ta = new TranslateAnimation(this.getX(), normal.left, 0, 0);
        ta.setDuration(600);
        ta.setInterpolator(new AccelerateDecelerateInterpolator());
        this.startAnimation(ta);

        this.layout(normal.left, normal.top, normal.right, normal.bottom);
    }


    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }

}
