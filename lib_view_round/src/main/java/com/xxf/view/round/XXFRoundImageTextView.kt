package com.xxf.view.round

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout

/**
 * @Description: eg app:radius="4dp"
 * @Author: XGod
 * @CreateDate: 2018/6/25 15:32
 */
open class XXFRoundImageTextView : XXFRoundLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    val imageView by lazy {
        XXFRoundImageView(context).apply {
            this@XXFRoundImageTextView.addView(
                this,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER
                )
            )
        }
    }
    val textView by lazy {
        XXFRoundTextView(context).apply {
            setTextColor(Color.BLACK)
            this@XXFRoundImageTextView.addView(
                this,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER
                )
            )
        }
    }


    override fun setRadius(radius: Float) {
        super.setRadius(radius)
        textView.setRadius(radius)
        imageView.setRadius(radius)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        textView.isSelected = selected
        imageView.isSelected = selected
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        textView.isEnabled = enabled
        imageView.isEnabled = enabled
    }
}