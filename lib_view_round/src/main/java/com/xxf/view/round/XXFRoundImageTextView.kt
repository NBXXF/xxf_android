package com.xxf.view.round

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.widget.TextViewCompat

/**
 * @Description: eg app:radius="4dp"
 * @Author: XGod
 * @CreateDate: 2018/6/25 15:32
 */
open class XXFRoundImageTextView : XXFRoundLayout {
    enum class Mode {
        TEXT, IMAGE, ALL
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

//    constructor(
//        context: Context,
//        attrs: AttributeSet?,
//        defStyleAttr: Int,
//        defStyleRes: Int
//    ) : super(context, attrs, defStyleAttr, defStyleRes) {
//        init(attrs)
//    }

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

    /**
     * 初始化子组件
     */
    @SuppressLint("RestrictedApi")
    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.xxf_round_image_text_view)
            if (array.hasValue(R.styleable.xxf_round_image_text_view_android_textSize)) {
                textView.setAutoSizeTextTypeWithDefaults(TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE)
                val textSize =
                    array.getDimension(R.styleable.xxf_round_image_text_view_android_textSize, 0f)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize)
            }
            if (array.hasValue(R.styleable.xxf_round_image_text_view_android_textColor)) {
                val textColor =
                    array.getColor(
                        R.styleable.xxf_round_image_text_view_android_textColor,
                        Color.WHITE
                    )
                textView.setTextColor(textColor)
            } else {
                textView.setTextColor(Color.WHITE)
            }
            if (array.hasValue(R.styleable.xxf_round_image_text_view_android_textStyle)) {
                val textStyle =
                    array.getInt(
                        R.styleable.xxf_round_image_text_view_android_textStyle,
                        Typeface.NORMAL
                    )
                textView.setTypeface(null, textStyle)
            }
            array.recycle()
        }
    }

    val textView by lazy {
        XXFRoundTextView(context).apply {
            this.gravity = Gravity.CENTER
            this.includeFontPadding = false
            this.setTextColor(Color.BLACK)
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

    /**
     * 设置显示模式
     */
    open fun setMode(mode: Mode) {
        when (mode) {
            Mode.TEXT -> {
                textView.visibility = View.VISIBLE
                imageView.visibility = View.GONE
            }
            Mode.IMAGE -> {
                textView.visibility = View.GONE
                imageView.visibility = View.VISIBLE
            }
            Mode.ALL -> {
                textView.visibility = View.VISIBLE
                imageView.visibility = View.VISIBLE
            }
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