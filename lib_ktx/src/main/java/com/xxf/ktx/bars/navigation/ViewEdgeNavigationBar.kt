package com.xxf.ktx.bars.navigation

import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import com.xxf.ktx.R
import com.xxf.ktx.isNavigationBarVisible
import com.xxf.ktx.navigationBarHeight
import com.xxf.ktx.viewTags

private var View.isAddedMarginBottom: Boolean? by viewTags(R.id.tag_is_added_margin_bottom)

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