package com.xxf.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.FloatRange;

import com.xxf.view.R;

/**
 * 先判断是否设定了maxHeight，如果设定了maxHeight，则直接使用maxHeight的值，
 * 如果没有设定maxHeight，则判断是否设定了heightRatio，如果设定了heightRatio的值 则使用此值与屏幕高度的乘积作为最高高度
 * <p>
 * app:maxHeight="180dp"
 * app:heightRatio="0.5"
 * <p>
 * app:maxWidth="180dp"
 * app:widthRatio="0.5"
 *
 *
 * 实例如下：
 *         <com.xxf.view.view.MaxOrMinLayout
 *             android:layout_width="wrap_content"
 *             android:layout_height="wrap_content"
 *             android:background="#6cf"
 *
 *             app:minWidthRatio="0.4"
 *             app:maxWidthRatio="0.6"
 *
 *             app:minHeightRatio="0.3"
 *             app:maxHeightRatio="0.5"
 *             >
 * @Description: 自定义最大高度viewGroup
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/9/24 10:14
 */
interface IMaxOrMinLayout {

    /**
     * 设置最小高度
     *
     * @param height
     */
    void setMinimumHeight(int height);

    /**
     * 设置最小高度倍数 相对于屏幕高度
     *
     * @param ratio
     */
    void setMinimumHeightRatio(@FloatRange(from = 0.0f, to = 1.0f) float ratio);


    /**
     * 设置最大高度
     *
     * @param height
     */
    void setMaximumHeight(int height);

    /**
     * 设置最大高度倍数 相对于屏幕高度
     *
     * @param ratio
     */
    void setMaximumHeightRatio(@FloatRange(from = 0.0f, to = 1.0f) float ratio);


    /**
     * 设置最小宽度
     *
     * @param width
     */
    void setMinimumWidth(int width);

    /**
     * 设置最小宽度倍数 相对于屏幕宽度
     *
     * @param ratio
     */
    void setMinimumWidthRatio(@FloatRange(from = 0.0f, to = 1.0f) float ratio);


    /**
     * 设置最大宽度
     *
     * @param width
     */
    void setMaximumWidth(int width);

    /**
     * 设置最大宽度倍数 相对于屏幕宽度
     *
     * @param ratio
     */
    void setMaximumWidthRatio(@FloatRange(from = 0.0f, to = 1.0f) float ratio);
}

public class MaxOrMinLayout extends FrameLayout implements IMaxOrMinLayout {


    private int mMaxHeight;
    private int mMaxWidth;

    public void setMaximumHeight(int height) {
        if (this.mMaxHeight != height) {
            this.mMaxHeight = height;
            this.requestLayout();
        }
    }

    @Override
    public void setMaximumHeightRatio(@FloatRange(from = 0.0f, to = 1.0f) float ratio) {
        setMaximumHeight((int) (ratio * getScreenHeight()));
    }

    @Override
    public void setMinimumHeight(int minHeight) {
        super.setMinimumHeight(minHeight);
    }

    @Override
    public void setMinimumHeightRatio(@FloatRange(from = 0.0f, to = 1.0f) float heightRatio) {
        setMinimumHeight((int) (heightRatio * getScreenHeight()));
    }


    @Override
    public void setMaximumWidth(int width) {
        if (this.mMaxWidth != width) {
            this.mMaxWidth = width;
            this.requestLayout();
        }
    }

    @Override
    public void setMaximumWidthRatio(float ratio) {
        this.setMaximumWidth((int) (ratio * getScreenWidth()));
    }


    @Override
    public void setMinimumWidth(int minWidth) {
        super.setMinimumWidth(minWidth);
    }

    @Override
    public void setMinimumWidthRatio(float ratio) {
        this.setMinimumWidth((int) (ratio * getScreenWidth()));
    }


    public MaxOrMinLayout(Context context) {
        super(context);
    }

    public MaxOrMinLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public MaxOrMinLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MaxOrMinView);
        //处理高度
        if (a.hasValue(R.styleable.MaxOrMinView_maxHeight)) {
            mMaxHeight = a.getLayoutDimension(R.styleable.MaxOrMinView_maxHeight, 0);
        } else if (a.hasValue(R.styleable.MaxOrMinView_maxHeightRatio)) {
            mMaxHeight = (int) (a.getFloat(R.styleable.MaxOrMinView_maxHeightRatio, 0) * getScreenHeight());
        }
        if (a.hasValue(R.styleable.MaxOrMinView_minHeightRatio)) {
            int minHeight = (int) (a.getFloat(R.styleable.MaxOrMinView_minHeightRatio, 0) * getScreenHeight());
            setMinimumHeight(minHeight);
        }


        //处理宽度
        if (a.hasValue(R.styleable.MaxOrMinView_maxWidth)) {
            mMaxWidth = a.getLayoutDimension(R.styleable.MaxOrMinView_maxWidth, 0);
        } else if (a.hasValue(R.styleable.MaxOrMinView_maxHeightRatio)) {
            mMaxWidth = (int) (a.getFloat(R.styleable.MaxOrMinView_maxWidthRatio, 0) * getScreenWidth());
        }
        if (a.hasValue(R.styleable.MaxOrMinView_minWidthRatio)) {
            int minWidth = (int) (a.getFloat(R.styleable.MaxOrMinView_minWidthRatio, 0) * getScreenWidth());
            setMinimumWidth(minWidth);
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (mMaxWidth > 0) {
            if (widthMode == MeasureSpec.EXACTLY) {
                widthSize = Math.min(widthSize, mMaxWidth);
            }
            if (widthMode == MeasureSpec.UNSPECIFIED) {
                widthSize = Math.min(widthSize, mMaxWidth);
            }
            if (widthMode == MeasureSpec.AT_MOST) {
                widthSize = Math.min(widthSize, mMaxWidth);
            }
        }


        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (mMaxHeight > 0) {
            if (heightMode == MeasureSpec.EXACTLY) {
                heightSize = Math.min(heightSize, mMaxHeight);
            }
            if (heightMode == MeasureSpec.UNSPECIFIED) {
                heightSize = Math.min(heightSize, mMaxHeight);
            }
            if (heightMode == MeasureSpec.AT_MOST) {
                heightSize = Math.min(heightSize, mMaxHeight);
            }
        }


        int maxWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize,
                widthMode);
        int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,
                heightMode);
        super.onMeasure(maxWidthMeasureSpec, maxHeightMeasureSpec);
    }

    /**
     * 获取屏幕高度
     */
    private int getScreenHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度
     */
    private int getScreenWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }
}