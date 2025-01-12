package com.xxf.ktx.bars.navigationbar

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
 * 使导航栏背景色透明
 */
fun <T : Activity> T.transparentNavigationBar() {
    window.transparentNavigationBar()
}

/**
 * 导航栏背景色
 */
inline var <T : Activity> T.navigationBarColor: Int
    get() = window.navigationBarColor
    set(value) {
        window.navigationBarColor = value
    }