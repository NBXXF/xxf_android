package com.xxf.view.ration

import android.content.Context
import android.util.AttributeSet
import com.xxf.view.ration.inner.RatioDatumMode
import com.xxf.view.ration.inner.RatioLayoutDelegate
import com.xxf.view.ration.inner.RatioLayoutDelegate.Companion.obtain
import com.xxf.view.ration.inner.XXFRatioWidget
import com.xxf.view.round.XXFRoundTextView

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
class XXFRatioTextView : XXFRoundTextView, XXFRatioWidget {
    private var mRatioLayoutDelegate: RatioLayoutDelegate<*>? = null

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mRatioLayoutDelegate = obtain(this, attrs!!)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mRatioLayoutDelegate = obtain(this, attrs!!, defStyleAttr)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate!!.update(widthMeasureSpec, heightMeasureSpec)
            widthMeasureSpec = mRatioLayoutDelegate!!.widthMeasureSpec
            heightMeasureSpec = mRatioLayoutDelegate!!.heightMeasureSpec
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun setAspectRatio(aspectRatio: Float) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate!!.setAspectRatio(aspectRatio)
        }
    }

    override fun setRatio(mode: RatioDatumMode?, widthRatio: Float, heightRatio: Float) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate!!.setRatio(mode, widthRatio, heightRatio)
        }
    }
}