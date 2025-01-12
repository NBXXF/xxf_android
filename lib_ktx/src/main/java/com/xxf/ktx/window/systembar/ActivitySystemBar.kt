package com.xxf.ktx.window.systembar

import android.app.Activity
import com.xxf.ktx.window.statusbar.isLightStatusBar

/**
 * 状态栏和导航栏是否可见
 */
inline var <T : Activity> T.isSystemBarVisible: Boolean
    get() = window.isSystemBarVisible
    set(value) {
        window.isSystemBarVisible = value
    }

/**
 * 设置状态栏和导航栏的颜色
 * 获取导航栏活着状态栏的颜色
 */
inline var <T : Activity> T.systemBarColor: Int
    get() = window.systemBarColor
    set(value) {
        window.systemBarColor = value
    }

/**
 * 是否是亮色的状态栏&导航栏
 */
inline var <T : Activity> T.isLightSystemBar: Boolean
    get() = window.isLightStatusBar
    set(value) {
        window.isLightStatusBar = value
    }

/**
 * 沉浸式界面
 */
fun <T : Activity> T.immerseSystemBar(lightMode: Boolean = true) {
    window.immerseSystemBar(lightMode = lightMode)
}