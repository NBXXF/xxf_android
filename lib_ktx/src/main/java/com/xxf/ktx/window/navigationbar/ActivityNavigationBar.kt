package com.xxf.ktx.window.navigationbar

import android.app.Activity

/**
 * 获取导航栏高度
 */
val <T : Activity> T.navigationBarHeight: Int
    get() = window.navigationBarHeight

/**
 * 是否显示导航栏
 */
inline var <T : Activity> T.isNavigationBarVisible: Boolean
    get() = window.isNavigationBarVisible
    set(value) {
        window.isNavigationBarVisible = value
    }

/**
 * 是否是亮色的导航栏
 */
inline var <T : Activity> T.isLightNavigationBar: Boolean
    get() = window.isLightNavigationBar
    set(value) {
        window.isLightNavigationBar = value
    }

/**
 * 导航栏背景色
 */
inline var <T : Activity> T.navigationBarColor: Int
    get() = window.navigationBarColor
    set(value) {
        window.navigationBarColor = value
    }

/**
 * 沉浸式界面
 */
fun <T : Activity> T.immerseNavigationBar(lightMode: Boolean = true) {
    window.immerseNavigationBar(lightMode = lightMode)
}