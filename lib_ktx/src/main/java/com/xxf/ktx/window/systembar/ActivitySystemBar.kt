package com.xxf.ktx.window.systembar

import android.app.Activity
import com.xxf.ktx.NO_GETTER
import com.xxf.ktx.noGetter
import com.xxf.ktx.window.statusbar.isLightStatusBar

/**
 *设置 Window 的布局是否适配系统窗口插图，可以更灵活地实现沉浸式布局或其他窗口管理需求。
 */
var <T : Activity> T.decorFitsSystemWindows: Boolean
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR)
    get() = noGetter()
    set(value) {
        window.decorFitsSystemWindows = value
    }

/**
 * 状态栏和导航栏是否可见
 */
inline var <T : Activity> T.isSystemBarVisible: Boolean
    get() = window.isSystemBarVisible
    set(value) {
        window.isSystemBarVisible = value
    }

/**
 * 状态栏与手势交互
 * BEHAVIOR_SHOW_BARS_BY_TOUCH：表示系统栏通过触摸屏幕的任意位置显示。
 * BEHAVIOR_SHOW_BARS_BY_SWIPE：表示系统栏通过从屏幕边缘的滑动手势显示。
 * BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE：表示系统栏通过滑动手势显示，并且在显示后短暂时间内自动隐藏。
 */
inline var <T : Activity> T.systemBarsBehavior: Int
    get() = window.systemBarsBehavior
    set(value) {
        window.systemBarsBehavior = value
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
 * 导航栏&状态栏
 */
fun <T : Activity> T.immerseSystemBar(lightMode: Boolean = true) {
    window.immerseSystemBar(lightMode = lightMode)
}