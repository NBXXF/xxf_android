package com.xxf.view.ration;

import android.content.Context;
import android.util.AttributeSet;

import com.xxf.view.ration.inner.RatioDatumMode;
import com.xxf.view.ration.inner.RatioLayoutDelegate;
import com.xxf.view.ration.inner.XXFRatioWidget;
import com.xxf.view.round.XXFRoundLinearLayout;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/4/21 2:45 PM
 * Description: 支持宽高比例
 * {@link R.styleable.xxf_ratio_styleable}
 * <p>
 * <declare-styleable name="xxf_ratio_styleable" tools:ignore="ResourceName">
 * <!-- 宽度比例系数 -->
 * <attr name="widthRatio" format="float" />
 * <!-- 高度比例系数 -->
 * <attr name="heightRatio" format="float" />
 * <!-- 宽高比 -->
 * <attr name="aspectRatio" format="float" />
 * <!-- 测量模式 -->
 * <attr name="datumRatio">
 * <!-- 自动 -->
 * <enum name="datumAuto" value="0" />
 * <!-- 以宽度为基准 -->
 * <enum name="datumWidth" value="1" />
 * <!-- 以高度为基准 -->
 * <enum name="datumHeight" value="2" />
 * </attr>
 * </declare-styleable>
 */

public class XXFRatioLinearLayout extends XXFRoundLinearLayout implements XXFRatioWidget {

    private RatioLayoutDelegate mRatioLayoutDelegate;


    public XXFRatioLinearLayout(Context context) {
        super(context);
    }

    public XXFRatioLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRatioLayoutDelegate = RatioLayoutDelegate.obtain(this, attrs);
    }

    public XXFRatioLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRatioLayoutDelegate = RatioLayoutDelegate.obtain(this, attrs, defStyleAttr);
    }

    public XXFRatioLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mRatioLayoutDelegate = RatioLayoutDelegate.obtain(this, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate.update(widthMeasureSpec, heightMeasureSpec);
            widthMeasureSpec = mRatioLayoutDelegate.getWidthMeasureSpec();
            heightMeasureSpec = mRatioLayoutDelegate.getHeightMeasureSpec();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setRatio(RatioDatumMode mode, float widthRatio, float heightRatio) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate.setRatio(mode, widthRatio, heightRatio);
        }
    }



    @Override
    public void setAspectRatio(float aspectRatio) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate.setAspectRatio(aspectRatio);
        }
    }
}
