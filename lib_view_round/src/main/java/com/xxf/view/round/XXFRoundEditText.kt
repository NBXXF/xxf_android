package com.xxf.view.round

import android.content.Context
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.text.style.CharacterStyle
import android.util.AttributeSet
import android.widget.TextView.BufferType
import androidx.annotation.CallSuper
import androidx.appcompat.widget.AppCompatEditText

/**
 * @Description: eg app:radius="4dp"
 * @Author: XGod
 * @CreateDate: 2018/6/25 15:37
 */
open class XXFRoundEditText : AppCompatEditText, XXFRoundWidget {
    protected val textWatchers = mutableListOf<TextWatcher>()
    private var focusedSelStart: Int = -1;
    private var focusedSelEnd: Int = -1;

    /**
     * 是否可以更新 setText 是否有效
     */
    open var updateable: Boolean = true


    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        CornerUtil.clipView(this, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        CornerUtil.clipView(this, attrs)
    }

    /**
     * @param ignoreSetTextChange 避免调用textWatcher 安卓本身默认setText会通知textWatcher 其目的避免 死循环问题
     * @param  keepState 是否保持选中状态
     */
    open fun setText(
        text: CharSequence,
        ignoreSetTextChange: Boolean = false,
        keepState: Boolean = false
    ) {
        if (updateable) {
            if (ignoreSetTextChange) {
                textWatchers.forEach {
                    super.removeTextChangedListener(it)
                }
            }
            if (keepState) {
                val oldText = getText()
                if (oldText != null) {
                    val spans = oldText?.getSpans(0, oldText.length, CharacterStyle::class.java)
                    spans?.forEach {
                        oldText.removeSpan(it)
                    }
                    //避免输入法联想次闪烁
                    val start = selectionStart
                    val end = selectionEnd
                    val len = text.length
                    oldText.replace(0, oldText.length, text)
                    if (start >= 0 || end >= 0) {
                        if (oldText != null) {
                            Selection.setSelection(
                                oldText,
                                Math.max(0, Math.min(start, len)),
                                Math.max(0, Math.min(end, len))
                            )
                        }
                    }
                } else {
                    this.setTextKeepState(text)
                }
            } else {
                this.setText(text)
            }
            if (ignoreSetTextChange) {
                textWatchers.forEach {
                    super.addTextChangedListener(it)
                }
            }
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if (updateable) {
            super.setText(text, type)
        }
    }

    /**
     * 焦点改变系统函数
     */
    @CallSuper
    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        if (isFocused && selStart >= 0 && selEnd >= 0) {
            this.focusedSelStart = selStart;
            this.focusedSelEnd = selEnd;
        }
    }

    /**
     * 恢复焦点选中
     * @return 是否恢复成功（上次没有选中 不会恢复成功）
     */
    open fun recoverySelection(): Boolean {
        if (this.focusedSelStart >= 0
            && this.focusedSelStart <= (this.text?.length ?: 0)
            && this.focusedSelEnd >= 0
            && this.focusedSelEnd <= (this.text?.length ?: 0)
        ) {
            setSelection(this.focusedSelStart, this.focusedSelEnd)
            return true
        }
        return false
    }

    @CallSuper
    override fun addTextChangedListener(watcher: TextWatcher?) {
        watcher?.let { textWatchers.add(it) }
        super.addTextChangedListener(watcher)
    }

    @CallSuper
    override fun removeTextChangedListener(watcher: TextWatcher?) {
        watcher?.let { textWatchers.remove(watcher) }
        super.removeTextChangedListener(watcher)
    }


    override fun setRadius(radius: Float) {
        CornerUtil.clipViewRadius(this, radius)
    }


    /**
     * fix 锤子手机8.0崩溃
     */
    override fun getText(): Editable? {
        val text = super.getText()
        if (text == null) {
            super.setText("", BufferType.EDITABLE)
            return super.getText()
        }
        return text
    }
}