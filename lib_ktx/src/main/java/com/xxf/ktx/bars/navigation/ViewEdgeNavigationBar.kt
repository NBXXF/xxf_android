package com.xxf.ktx.bars.navigation

import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat.Type
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import com.xxf.ktx.R
import com.xxf.ktx.findActivity
import com.xxf.ktx.rootWindowInsetsCompat
import com.xxf.ktx.viewTags
import com.xxf.ktx.windowInsetsControllerCompat

private var View.isAddedMarginBottom: Boolean? by viewTags(R.id.tag_is_added_margin_bottom)

val View.navigationBarHeight: Int
    get() = this.context.findActivity()?.navigationBarHeight ?: 0


inline var View.isNavigationBarVisible: Boolean
    get() = rootWindowInsetsCompat?.isVisible(Type.navigationBars()) == true
    set(value) {
        windowInsetsControllerCompat?.run {
            if (value) show(Type.navigationBars()) else hide(Type.navigationBars())
        }
    }

/**
 * 将导航栏高度 添加到PaddingTop
 */
fun View.addNavigationBarHeightToMarginBottom() = post {
    if (isNavigationBarVisible && isAddedMarginBottom != true) {
        updateLayoutParams<ViewGroup.MarginLayoutParams> {
            updateMargins(bottom = bottomMargin + navigationBarHeight)
            isAddedMarginBottom = true
        }
    }
}

fun View.subtractNavigationBarHeightToMarginBottom() = post {
    if (isNavigationBarVisible && isAddedMarginBottom == true) {
        updateLayoutParams<ViewGroup.MarginLayoutParams> {
            updateMargins(bottom = bottomMargin - navigationBarHeight)
            isAddedMarginBottom = false
        }
    }
}