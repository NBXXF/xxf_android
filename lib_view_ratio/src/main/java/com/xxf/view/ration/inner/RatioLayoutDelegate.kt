package com.xxf.view.ration.inner;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xxf.view.ration.R;

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

public final class RatioLayoutDelegate<TARGET extends View & XXFRatioWidget> {

    public static <TARGET extends View & XXFRatioWidget> RatioLayoutDelegate obtain(TARGET target, AttributeSet attrs) {
        return obtain(target, attrs, 0);
    }

    public static <TARGET extends View & XXFRatioWidget> RatioLayoutDelegate obtain(TARGET target, AttributeSet attrs, int defStyleAttr) {
        return obtain(target, attrs, 0, 0);
    }

    public static <TARGET extends View & XXFRatioWidget> RatioLayoutDelegate obtain(TARGET target, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        return new RatioLayoutDelegate<>(target, attrs, defStyleAttr, defStyleRes);
    }

    private final TARGET mRatioTarget;

    private RatioDatumMode mRatioDatumMode;
    private float mWidthRatio;
    private float mHeightRatio;
    private float mAspectRatio;

    private int mWidthMeasureSpec, mHeightMeasureSpec;

    private RatioLayoutDelegate(TARGET target, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mRatioTarget = target;
        TypedArray a = mRatioTarget.getContext().obtainStyledAttributes(attrs, R.styleable.xxf_ratio_styleable, defStyleAttr, defStyleRes);
        mRatioDatumMode = RatioDatumMode.valueOf(a.getInt(R.styleable.xxf_ratio_styleable_datumRatio, 0));
        mWidthRatio = a.getFloat(R.styleable.xxf_ratio_styleable_widthRatio, mWidthRatio);
        mHeightRatio = a.getFloat(R.styleable.xxf_ratio_styleable_heightRatio, mHeightRatio);
        mAspectRatio = a.getFloat(R.styleable.xxf_ratio_styleable_aspectRatio, mAspectRatio);
        a.recycle();
    }

    private RatioDatumMode shouldRatioDatumMode(ViewGroup.LayoutParams params) {
        if (mRatioDatumMode == null || mRatioDatumMode == RatioDatumMode.DATUM_AUTO) {
            if (params.width > 0 || shouldLinearParamsWidth(params)
                    || params.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                return RatioDatumMode.DATUM_WIDTH;
            }
            if (params.height > 0 || shouldLinearParamsHeight(params)
                    || params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                return RatioDatumMode.DATUM_HEIGHT;
            }
            return null;
        }
        return mRatioDatumMode;
    }

    private boolean shouldLinearParamsWidth(ViewGroup.LayoutParams params) {
        if (!(params instanceof LinearLayout.LayoutParams)) {
            return false;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) params;
        return layoutParams.width == 0 && layoutParams.weight > 0;
    }

    private boolean shouldLinearParamsHeight(ViewGroup.LayoutParams params) {
        if (!(params instanceof LinearLayout.LayoutParams)) {
            return false;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) params;
        return layoutParams.height == 0 && layoutParams.weight > 0;
    }

    public final void update(int widthMeasureSpec, int heightMeasureSpec) {
        mWidthMeasureSpec = widthMeasureSpec;
        mHeightMeasureSpec = heightMeasureSpec;

        final RatioDatumMode mode = shouldRatioDatumMode(mRatioTarget.getLayoutParams());
        final int wp = mRatioTarget.getPaddingLeft() + mRatioTarget.getPaddingRight();
        final int hp = mRatioTarget.getPaddingTop() + mRatioTarget.getPaddingBottom();

        if (mode == RatioDatumMode.DATUM_WIDTH) {
            final int width = View.MeasureSpec.getSize(widthMeasureSpec);
            if (mAspectRatio > 0) {
                final int height = resolveSize(Math.round((width - wp) / mAspectRatio + hp), heightMeasureSpec);
                mHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            } else if (mWidthRatio > 0 && mHeightRatio > 0) {
                final int height = resolveSize(Math.round((width - wp) / mWidthRatio * mHeightRatio + hp), heightMeasureSpec);
                mHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            }
        } else if (mode == RatioDatumMode.DATUM_HEIGHT) {
            final int height = View.MeasureSpec.getSize(heightMeasureSpec);
            if (mAspectRatio > 0) {
                final int width = resolveSize(Math.round((height - hp) / mAspectRatio + wp), widthMeasureSpec);
                mWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            } else if (mWidthRatio > 0 && mHeightRatio > 0) {
                final int width = resolveSize(Math.round((height - hp) / mHeightRatio * mWidthRatio + wp), widthMeasureSpec);
                mWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            }
        }
    }

    public final int getWidthMeasureSpec() {
        return mWidthMeasureSpec;
    }

    public final int getHeightMeasureSpec() {
        return mHeightMeasureSpec;
    }

    private void requestLayout() {
        mRatioTarget.requestLayout();
    }

    private int resolveSize(int size, int measureSpec) {
        /*return View.resolveSize(size,measureSpec);*/
        return size;
    }

    public final void setRatio(RatioDatumMode mode, float widthRatio, float heightRatio) {
        mRatioDatumMode = mode;
        mWidthRatio = widthRatio;
        mHeightRatio = heightRatio;
        requestLayout();
    }

    public final void setAspectRatio(float aspectRatio) {
        mAspectRatio = aspectRatio;
        requestLayout();
    }
}
