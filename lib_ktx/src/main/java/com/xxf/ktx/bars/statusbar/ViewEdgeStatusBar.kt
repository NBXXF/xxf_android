package com.xxf.ktx.bars.statusbar

import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat.Type
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import com.xxf.ktx.R
import com.xxf.ktx.findActivity
import com.xxf.ktx.rootWindowInsetsCompat
import com.xxf.ktx.viewTags
import com.xxf.ktx.windowInsetsControllerCompat

private var View.isAddedMarginTop: Boolean? by viewTags(R.id.tag_is_added_margin_top)
private var View.isAddedPaddingTop: Boolean? by viewTags(R.id.tag_is_added_padding_top)

val View.statusBarHeight:Int
    get() = this.context.findActivity()?.statusBarHeight?:0

inline var View.isStatusBarVisible: Boolean
    get() = rootWindowInsetsCompat?.isVisible(Type.statusBars()) == true
    set(value) {
        windowInsetsControllerCompat?.run {
            if (value) show(Type.statusBars()) else hide(Type.statusBars())
        }
    }

/**
 * 将状态栏高度 添加到MarginTop
 */
fun View.addStatusBarHeightToMarginTop() = post {
    if (isStatusBarVisible && isAddedMarginTop != true) {
        updateLayoutParams<ViewGroup.MarginLayoutParams> {
            updateMargins(top = topMargin + statusBarHeight)
            isAddedMarginTop = true
        }
    }
}

fun View.subtractStatusBarHeightToMarginTop() = post {
    if (isStatusBarVisible && isAddedMarginTop == true) {
        updateLayoutParams<ViewGroup.MarginLayoutParams> {
            updateMargins(top = topMargin - statusBarHeight)
            isAddedMarginTop = false
        }
    }
}

/**
 * 将状态栏高度 添加到PaddingTop
 */
fun View.addStatusBarHeightToPaddingTop() = post {
    if (isAddedPaddingTop != true) {
        updatePadding(top = paddingTop + statusBarHeight)
        updateLayoutParams {
            height = measuredHeight + statusBarHeight
        }
        isAddedPaddingTop = true
    }
}

fun View.subtractStatusBarHeightToPaddingTop() = post {
    if (isAddedPaddingTop == true) {
        updatePadding(top = paddingTop - statusBarHeight)
        updateLayoutParams {
            height = measuredHeight - statusBarHeight
        }
        isAddedPaddingTop = false
    }
}