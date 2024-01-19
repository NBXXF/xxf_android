@file:Suppress("unused")

package com.xxf.ktx

import android.view.View
import androidx.core.view.WindowInsetsCompat.Type
import com.xxf.ktx.internals.KeyboardHiddenTouchListener

fun View.showKeyboard() = windowInsetsControllerCompat?.show(Type.ime())

fun View.hideKeyboard() = windowInsetsControllerCompat?.hide(Type.ime())

fun View.toggleKeyboard() = if (isKeyboardVisible) hideKeyboard() else showKeyboard()

inline var View.isKeyboardVisible: Boolean
    get() = rootWindowInsetsCompat?.isVisible(Type.ime()) == true
    set(value) {
        if (value) showKeyboard() else hideKeyboard()
    }

inline val View.keyboardHeight: Int
    get() = rootWindowInsetsCompat?.getInsets(Type.ime())?.bottom ?: -1


/**
 * 触摸是否关闭键盘
 */
inline var View.isKeyboardHiddenInTouchMode: Boolean
    get() {
        return this.getTag<Boolean>(View::isKeyboardHiddenInTouchMode.name) == true
    }
    set(value) {
        this.setTag(View::isKeyboardHiddenInTouchMode.name, value)
        if (value) {
            this.setOnTouchListener(KeyboardHiddenTouchListener())
        } else {
            this.setOnTouchListener(null)
        }
    }
