package com.xxf.snackbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat

class SnackbarLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    var messageView: TextView? = null
        private set
    var actionView: Button? = null
        private set
    @JvmField
    var mMaxWidth: Int
    private val mMaxInlineActionWidth: Int

    interface OnLayoutChangeListener {
        fun onLayoutChange(view: View?, left: Int, top: Int, right: Int, bottom: Int)
    }

    interface OnAttachStateChangeListener {
        fun onViewAttachedToWindow(v: View?)
        fun onViewDetachedFromWindow(v: View?)
    }

    private var mOnLayoutChangeListener: OnLayoutChangeListener? = null
    private var mOnAttachStateChangeListener: OnAttachStateChangeListener? = null
    override fun onFinishInflate() {
        super.onFinishInflate()
        messageView = findViewById<View>(R.id.snackbar_text) as TextView
        actionView = findViewById<View>(R.id.snackbar_action) as Button
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mMaxWidth > 0 && measuredWidth > mMaxWidth) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
        val multiLineVPadding = resources.getDimensionPixelSize(
                R.dimen.design_snackbar_padding_vertical_2lines)
        val singleLineVPadding = resources.getDimensionPixelSize(
                R.dimen.design_snackbar_padding_vertical)
        val isMultiLine = messageView!!.layout
                .lineCount > 1
        var remeasure = false
        if (isMultiLine && mMaxInlineActionWidth > 0 && actionView!!.measuredWidth > mMaxInlineActionWidth) {
            if (updateViewsWithinLayout(VERTICAL, multiLineVPadding,
                            multiLineVPadding - singleLineVPadding)) {
                remeasure = true
            }
        } else {
            val messagePadding = if (isMultiLine) multiLineVPadding else singleLineVPadding
            if (updateViewsWithinLayout(HORIZONTAL, messagePadding, messagePadding)) {
                remeasure = true
            }
        }
        if (remeasure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun animateChildrenIn(delay: Int, duration: Int) {
        ViewCompat.setAlpha(messageView, 0f)
        ViewCompat.animate(messageView!!)
                .alpha(1f)
                .setDuration(duration.toLong())
                .setStartDelay(delay.toLong())
                .start()
        if (actionView!!.visibility == VISIBLE) {
            ViewCompat.setAlpha(actionView, 0f)
            ViewCompat.animate(actionView!!)
                    .alpha(1f)
                    .setDuration(duration.toLong())
                    .setStartDelay(delay.toLong())
                    .start()
        }
    }

    fun animateChildrenOut(delay: Int, duration: Int) {
        ViewCompat.setAlpha(messageView, 1f)
        ViewCompat.animate(messageView!!)
                .alpha(0f)
                .setDuration(duration.toLong())
                .setStartDelay(delay.toLong())
                .start()
        if (actionView!!.visibility == VISIBLE) {
            ViewCompat.setAlpha(actionView, 1f)
            ViewCompat.animate(actionView!!)
                    .alpha(0f)
                    .setDuration(duration.toLong())
                    .setStartDelay(delay.toLong())
                    .start()
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (mOnLayoutChangeListener != null) {
            mOnLayoutChangeListener!!.onLayoutChange(this, l, t, r, b)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mOnAttachStateChangeListener != null) {
            mOnAttachStateChangeListener!!.onViewAttachedToWindow(this)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mOnAttachStateChangeListener != null) {
            mOnAttachStateChangeListener!!.onViewDetachedFromWindow(this)
        }
    }

    fun setOnLayoutChangeListener(onLayoutChangeListener: OnLayoutChangeListener?) {
        mOnLayoutChangeListener = onLayoutChangeListener
    }

    fun setOnAttachStateChangeListener(listener: OnAttachStateChangeListener?) {
        mOnAttachStateChangeListener = listener
    }

    private fun updateViewsWithinLayout(orientation: Int,
                                        messagePadTop: Int, messagePadBottom: Int): Boolean {
        var changed = false
        if (orientation != getOrientation()) {
            setOrientation(orientation)
            changed = true
        }
        if (messageView!!.paddingTop != messagePadTop
                || messageView!!.paddingBottom != messagePadBottom) {
            updateTopBottomPadding(messageView, messagePadTop, messagePadBottom)
            changed = true
        }
        return changed
    }

    companion object {
        private fun updateTopBottomPadding(view: View?, topPadding: Int, bottomPadding: Int) {
            if (ViewCompat.isPaddingRelative(view!!)) {
                ViewCompat.setPaddingRelative(view,
                        ViewCompat.getPaddingStart(view), topPadding,
                        ViewCompat.getPaddingEnd(view), bottomPadding)
            } else {
                view.setPadding(view.paddingLeft, topPadding,
                        view.paddingRight, bottomPadding)
            }
        }
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SnackbarLayout)
        mMaxWidth = a.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1)
        mMaxInlineActionWidth = a.getDimensionPixelSize(
                R.styleable.SnackbarLayout_maxActionInlineWidth, -1)
        if (a.hasValue(R.styleable.SnackbarLayout_elevation)) {
            ViewCompat.setElevation(this, a.getDimensionPixelSize(
                    R.styleable.SnackbarLayout_elevation, 0).toFloat())
        }
        a.recycle()
        isClickable = true
        LayoutInflater.from(context)
                .inflate(R.layout.xxf_snackbar_layout_include, this)
        ViewCompat.setAccessibilityLiveRegion(this,
                ViewCompat.ACCESSIBILITY_LIVE_REGION_POLITE)
    }
}