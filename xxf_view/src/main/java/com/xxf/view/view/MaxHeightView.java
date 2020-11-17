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
 *
 * @Description: 自定义最大高度viewGroup
 * @Author: XGod
 * @CreateDate: 2020/9/24 10:14
 */
public class MaxHeightView extends FrameLayout {


    private int mMaxHeight;

    /**
     * @param maxHeight 像素
     */
    public void setMaxHeight(float maxHeight) {
        if (this.mMaxHeight != mMaxHeight) {
            this.mMaxHeight = mMaxHeight;
            this.requestLayout();
        }
    }

    /**
     * 占屏幕比例
     *
     * @param heightRatio
     */
    public void setHeightRatio(@FloatRange(from = 0.0f, to = 1.0f) float heightRatio) {
        setMaxHeight(heightRatio * getScreenHeight());
    }


    public MaxHeightView(Context context) {
        super(context);
    }

    public MaxHeightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public MaxHeightView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MaxHeightView);
        if (a.hasValue(R.styleable.MaxHeightView_maxHeight)) {
            mMaxHeight = a.getLayoutDimension(R.styleable.MaxHeightView_maxHeight, 0);
        } else if (a.hasValue(R.styleable.MaxHeightView_maxHeightRatio)) {
            mMaxHeight = (int) (a.getFloat(R.styleable.MaxHeightView_maxHeightRatio, 0) * getScreenHeight());
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mMaxHeight <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            heightSize = heightSize <= mMaxHeight ? heightSize
                    : mMaxHeight;
        }

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = heightSize <= mMaxHeight ? heightSize
                    : mMaxHeight;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = heightSize <= mMaxHeight ? heightSize
                    : mMaxHeight;
        }
        int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,
                heightMode);
        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec);
    }

    /**
     * 获取屏幕高度
     */
    private int getScreenHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }

}