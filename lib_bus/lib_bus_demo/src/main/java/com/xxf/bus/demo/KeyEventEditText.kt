package com.next.space.cflow.editor.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import androidx.annotation.CallSuper
import androidx.appcompat.widget.AppCompatEditText

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：5/15/21
 * Description ://处理google输入法删除键调用
 */
open class KeyEventEditText : AppCompatEditText {

    companion object {
        val regex = "[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}".toRegex()
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mOnKeyListener: OnKeyListener? = null;

    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection? {
        if (super.onCreateInputConnection(outAttrs) == null) {
            return null
        }
        return CustomInputConnectionWrapper(super.onCreateInputConnection(outAttrs), true)
    }

    /**
     * 解决google输入法删除不走OnKeyListener()回调问题
     */
    private inner class CustomInputConnectionWrapper(target: InputConnection?, mutable: Boolean) :
        InputConnectionWrapper(target, mutable) {

        //修复搜狗输入法监听不到回车bug by xuanyouwu@163.com
        override fun commitText(text: CharSequence, newCursorPosition: Int): Boolean {
            if (text == "\n") {
                sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
                return false
            }
            if (text == " ") {
                sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE))
                return false
            }
            if (text == "]") {
                sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_RIGHT_BRACKET))
                return false
            }
            if (text == "-") {
                sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MINUS))
                return false
            }
            if (text.matches(regex)) {
                val intercept: Boolean = onPasteCallback?.invoke(Unit) ?: false
                if (intercept) {
                    return true
                }
            }
            val result= super.commitText(text, newCursorPosition)

            if (text == "@") {
                sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_AT))
                return false
            }
            return result
        }


        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
            return (beforeLength == 1 && afterLength == 0)
                    && (mOnKeyListener != null && mOnKeyListener!!.onKey(
                this@KeyEventEditText,
                KeyEvent.KEYCODE_DEL,
                KeyEvent(
                    KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_DEL
                )
            )
                    ) || super.deleteSurroundingText(beforeLength, afterLength)
        }
    }

    @CallSuper
    override fun setOnKeyListener(l: OnKeyListener?) {
        super.setOnKeyListener(l)
        this.mOnKeyListener = l;
    }


    internal var onPasteCallback: ((Unit) -> Boolean)? = null

    fun setOnPasteCallback(listener: ((Unit) -> Boolean)) {
        onPasteCallback = listener
    }


}