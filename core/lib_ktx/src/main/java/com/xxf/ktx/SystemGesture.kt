package com.xxf.ktx

import android.app.Activity
import android.graphics.Rect
import android.view.Window
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment

/**
 * 屏蔽系统手势的api
 * 品比手势 屏蔽滑动手势
 * 控制是否允许系统手势
 */
var Fragment.isSystemGesturesEnabled: Boolean
    get() {
        return this.requireActivity().isSystemGesturesEnabled
    }
    set(value) {
        this.requireActivity().isSystemGesturesEnabled = value
    }


/**
 * 控制是否允许系统手势
 */
var Activity.isSystemGesturesEnabled: Boolean
    get() {
        return this.window.isSystemGesturesEnabled
    }
    set(value) {
        this.window.isSystemGesturesEnabled = value
    }

/**
 * 控制是否允许系统手势
 */
var Window.isSystemGesturesEnabled: Boolean
    get() {
        return ViewCompat.getSystemGestureExclusionRects(this.decorView).indexOfFirst {
            it.contains(
                Rect(
                    0,
                    0,
                    context.resources.displayMetrics.widthPixels,
                    context.resources.displayMetrics.heightPixels
                )
            )
        } == -1
    }
    set(value) {
        val systemGestureExclusionRects =
            ViewCompat.getSystemGestureExclusionRects(this.decorView).filter {
                !it.contains(
                    Rect(
                        0,
                        0,
                        context.resources.displayMetrics.widthPixels,
                        context.resources.displayMetrics.heightPixels
                    )
                )
            }.toMutableList()

        if (!value) {
            val edgePadding = 48.dp
            systemGestureExclusionRects.add(
                Rect(
                    0,
                    0,
                    context.resources.displayMetrics.widthPixels + edgePadding,
                    context.resources.displayMetrics.heightPixels + edgePadding
                )
            )
        }
        ViewCompat.setSystemGestureExclusionRects(this.decorView, systemGestureExclusionRects)
    }
