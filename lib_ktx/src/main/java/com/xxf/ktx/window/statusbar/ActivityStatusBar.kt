package com.xxf.ktx.window.statusbar

import android.app.Activity
import com.xxf.ktx.NO_GETTER
import com.xxf.ktx.noGetter

/**
 *设置 Window 的布局是否适配系统窗口插图，可以更灵活地实现沉浸式布局或其他窗口管理需求。
 */
var <T : Activity> T.decorFitsSystemWindows: Boolean
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR)
    get() = noGetter()
    set(value) {
        this.window.decorFitsSystemWindows = value
    }


/**
 * 状态栏的高度
 */
val <T : Activity> T.statusBarHeight: Int
    get() = window.statusBarHeight

/**
 * 是否是亮色的状态栏
 */
inline var <T : Activity> T.isLightStatusBar: Boolean
    get() = window.isLightStatusBar
    set(value) {
        window.isLightStatusBar = value;
    }

/**
 * 状态栏是否可见
 */
inline var <T : Activity> T.isStatusBarVisible: Boolean
    get() = window.isStatusBarVisible
    set(value) {
        window.isStatusBarVisible = value
    }


/**
 * 沉浸式界面
 */
fun <T : Activity> T.immerseStatusBar(lightMode: Boolean = true) {
    window.immerseStatusBar(lightMode = lightMode)
}

/**
 * 状态栏背景色透明
 */
inline var <T : Activity> T.statusBarColor: Int
    get() = window.statusBarColor
    set(value) {
        window.statusBarColor = value
    }
