package com.xxf.view.ration.inner

import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.xxf.view.ration.R
import com.xxf.view.ration.inner.RatioDatumMode
import com.xxf.view.ration.inner.RatioDatumMode.Companion.valueOf

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/4/21 2:45 PM
 * Description: 支持宽高比例
 * [R.styleable.xxf_ratio_styleable]
 *
 *
 * <declare-styleable name="xxf_ratio_styleable" tools:ignore="ResourceName">
 *
 * <attr name="widthRatio" format="float"></attr>
 *
 * <attr name="heightRatio" format="float"></attr>
 *
 * <attr name="aspectRatio" format="float"></attr>
 *
 * <attr name="datumRatio">
 *
 * <enum name="datumAuto" value="0"></enum>
 *
 * <enum name="datumWidth" value="1"></enum>
 *
 * <enum name="datumHeight" value="2"></enum>
</attr> *
</declare-styleable> *
 */
class RatioLayoutDelegate<TARGET> private constructor(private val mRatioTarget: TARGET, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) where TARGET : View, TARGET : XXFRatioWidget {
    private var mRatioDatumMode: RatioDatumMode?
    private var mWidthRatio: Float
    private var mHeightRatio: Float
    private var mAspectRatio: Float
    var widthMeasureSpec = 0
        private set
    var heightMeasureSpec = 0
        private set

    private fun shouldRatioDatumMode(params: ViewGroup.LayoutParams): RatioDatumMode? {
        if (mRatioDatumMode == null || mRatioDatumMode === RatioDatumMode.DATUM_AUTO) {
            if (params.width > 0 || shouldLinearParamsWidth(params)
                    || params.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                return RatioDatumMode.DATUM_WIDTH
            }
            return if (params.height > 0 || shouldLinearParamsHeight(params)
                    || params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                RatioDatumMode.DATUM_HEIGHT
            } else null
        }
        return mRatioDatumMode
    }

    private fun shouldLinearParamsWidth(params: ViewGroup.LayoutParams): Boolean {
        if (params !is LinearLayout.LayoutParams) {
            return false
        }
        val layoutParams = params
        return layoutParams.width == 0 && layoutParams.weight > 0
    }

    private fun shouldLinearParamsHeight(params: ViewGroup.LayoutParams): Boolean {
        if (params !is LinearLayout.LayoutParams) {
            return false
        }
        val layoutParams = params
        return layoutParams.height == 0 && layoutParams.weight > 0
    }

    fun update(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        this.widthMeasureSpec = widthMeasureSpec
        this.heightMeasureSpec = heightMeasureSpec
        val mode = shouldRatioDatumMode(mRatioTarget!!.layoutParams)
        val wp = mRatioTarget.paddingLeft + mRatioTarget.paddingRight
        val hp = mRatioTarget.paddingTop + mRatioTarget.paddingBottom
        if (mode === RatioDatumMode.DATUM_WIDTH) {
            val width = View.MeasureSpec.getSize(widthMeasureSpec)
            if (mAspectRatio > 0) {
                val height = resolveSize(Math.round((width - wp) / mAspectRatio + hp), heightMeasureSpec)
                this.heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            } else if (mWidthRatio > 0 && mHeightRatio > 0) {
                val height = resolveSize(Math.round((width - wp) / mWidthRatio * mHeightRatio + hp), heightMeasureSpec)
                this.heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            }
        } else if (mode === RatioDatumMode.DATUM_HEIGHT) {
            val height = View.MeasureSpec.getSize(heightMeasureSpec)
            if (mAspectRatio > 0) {
                val width = resolveSize(Math.round((height - hp) / mAspectRatio + wp), widthMeasureSpec)
                this.widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
            } else if (mWidthRatio > 0 && mHeightRatio > 0) {
                val width = resolveSize(Math.round((height - hp) / mHeightRatio * mWidthRatio + wp), widthMeasureSpec)
                this.widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
            }
        }
    }

    private fun requestLayout() {
        mRatioTarget!!.requestLayout()
    }

    private fun resolveSize(size: Int, measureSpec: Int): Int {
        /*return View.resolveSize(size,measureSpec);*/
        return size
    }

    fun setRatio(mode: RatioDatumMode?, widthRatio: Float, heightRatio: Float) {
        mRatioDatumMode = mode
        mWidthRatio = widthRatio
        mHeightRatio = heightRatio
        requestLayout()
    }

    fun setAspectRatio(aspectRatio: Float) {
        mAspectRatio = aspectRatio
        requestLayout()
    }

    companion object {
        fun <TARGET> obtain(target: TARGET, attrs: AttributeSet): RatioLayoutDelegate<TARGET> where TARGET : View, TARGET : XXFRatioWidget {
            return obtain(target, attrs, 0)
        }

        fun <TARGET> obtain(target: TARGET, attrs: AttributeSet, defStyleAttr: Int): RatioLayoutDelegate<TARGET> where TARGET : View, TARGET : XXFRatioWidget {
            return obtain(target, attrs, 0, 0)
        }

        @JvmStatic
        fun <TARGET> obtain(target: TARGET, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int): RatioLayoutDelegate<TARGET> where TARGET : View, TARGET : XXFRatioWidget {
            return RatioLayoutDelegate(target, attrs, defStyleAttr, defStyleRes)
        }
    }

    init {
        val a = mRatioTarget!!.context.obtainStyledAttributes(attrs, R.styleable.xxf_ratio_styleable, defStyleAttr, defStyleRes)
        mRatioDatumMode = valueOf(a.getInt(R.styleable.xxf_ratio_styleable_datumRatio, 0))
        mWidthRatio = a.getFloat(R.styleable.xxf_ratio_styleable_widthRatio, 0.0f)
        mHeightRatio = a.getFloat(R.styleable.xxf_ratio_styleable_heightRatio, 0.0f)
        mAspectRatio = a.getFloat(R.styleable.xxf_ratio_styleable_aspectRatio, 0.0f)
        a.recycle()
    }
}