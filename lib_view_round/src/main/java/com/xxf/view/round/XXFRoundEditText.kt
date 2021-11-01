package com.xxf.view.round

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
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

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        CornerUtil.clipView(this, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
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
        if (ignoreSetTextChange) {
            textWatchers.forEach {
                super.removeTextChangedListener(it)
            }
        }
        if (keepState) {
//            val oldText = getText()
//            if (oldText != null) {
//                //避免输入法联想次闪烁
//                oldText.replace(0, oldText.length, text)
//            } else {
//                this.setTextKeepState(text)
//            }
            this.setTextKeepState(text)
        } else {
            this.setText(text)
        }
        if (ignoreSetTextChange) {
            textWatchers.forEach {
                super.addTextChangedListener(it)
            }
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
}