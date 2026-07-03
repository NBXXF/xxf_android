@file:Suppress("unused")

package com.xxf.ktx

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.Window
import androidx.core.view.WindowInsetsCompat.Type
import androidx.fragment.app.Fragment
import com.xxf.ktx.internals.KeyboardHiddenTouchListener

/*************************************/
;

/***************显示软键盘*************************/
fun View.showKeyboard() = windowInsetsControllerCompat?.show(Type.ime())

fun Window.showKeyboard() = this.decorView.showKeyboard()

fun Activity.showKeyboard() = this.window.showKeyboard()

fun Fragment.showKeyboard() = this.requireActivity().showKeyboard()

fun Dialog.showKeyboard() = this.window?.showKeyboard()


/***************隐藏软键盘*************************/
fun View.hideKeyboard() = windowInsetsControllerCompat?.hide(Type.ime())

fun Window.hideKeyboard() = this.decorView.hideKeyboard()

fun Activity.hideKeyboard() = this.window.hideKeyboard()

fun Fragment.hideKeyboard() = this.requireActivity().hideKeyboard()

fun Dialog.hideKeyboard() = this.window?.hideKeyboard()


/***************反转软键盘*************************/
fun View.toggleKeyboard() = if (isKeyboardVisible) hideKeyboard() else showKeyboard()

fun Window.toggleKeyboard() = this.decorView.toggleKeyboard()

fun Activity.toggleKeyboard() = this.window.toggleKeyboard()

fun Fragment.toggleKeyboard() = this.requireActivity().toggleKeyboard()

fun Dialog.toggleKeyboard() = this.window?.toggleKeyboard()


/***************软键盘是否可见*************************/
inline var View.isKeyboardVisible: Boolean
    get() = rootWindowInsetsCompat?.isVisible(Type.ime()) == true
    set(value) {
        if (value) showKeyboard() else hideKeyboard()
    }

inline var Window.isKeyboardVisible: Boolean
    set(value) {
        this.decorView.isKeyboardVisible = value
    }
    get() {
        return this.decorView.isKeyboardVisible
    }

inline var Activity.isKeyboardVisible: Boolean
    set(value) {
        this.window.isKeyboardVisible = value
    }
    get() {
        return this.window.isKeyboardVisible
    }

inline var Fragment.isKeyboardVisible: Boolean
    set(value) {
        this.requireActivity().isKeyboardVisible = value
    }
    get() {
        return this.requireActivity().isKeyboardVisible
    }

inline var Dialog.isKeyboardVisible: Boolean
    set(value) {
        this.window?.isKeyboardVisible = value
    }
    get() {
        return this.window?.isKeyboardVisible ?: false
    }


inline val View.keyboardHeight: Int
    get() = rootWindowInsetsCompat?.getInsets(Type.ime())?.bottom ?: -1


/***************触摸是否关闭键盘*************************/
inline var <T : View> T.isKeyboardHiddenInTouchMode: Boolean
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

inline var Window.isKeyboardHiddenInTouchMode: Boolean
    set(value) {
        this.decorView.isKeyboardHiddenInTouchMode = value
    }
    get() {
        return this.decorView.isKeyboardHiddenInTouchMode
    }

inline var Activity.isKeyboardHiddenInTouchMode: Boolean
    set(value) {
        this.window.isKeyboardHiddenInTouchMode = value
    }
    get() {
        return this.window.isKeyboardHiddenInTouchMode
    }

inline var Fragment.isKeyboardHiddenInTouchMode: Boolean
    set(value) {
        this.requireView().isKeyboardHiddenInTouchMode = value
    }
    get() {
        return this.requireView().isKeyboardHiddenInTouchMode
    }

inline var Dialog.isKeyboardHiddenInTouchMode: Boolean
    set(value) {
        this.window?.isKeyboardHiddenInTouchMode = value
    }
    get() {
        return this.window?.isKeyboardHiddenInTouchMode ?: false
    }