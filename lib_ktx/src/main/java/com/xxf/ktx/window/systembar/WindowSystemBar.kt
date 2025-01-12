package com.xxf.ktx.window.systembar

import android.graphics.Color
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.xxf.ktx.windowInsetsControllerCompat
import androidx.core.view.WindowInsetsCompat.Type
import com.xxf.ktx.rootWindowInsetsCompat
import com.xxf.ktx.window.statusbar.decorFitsSystemWindows

/**
 * 状态栏&导航栏是否可见
 */
inline var <T : Window> T.isSystemBarVisible: Boolean
    get() = decorView.rootWindowInsetsCompat?.isVisible(Type.systemBars()) == true
    set(value) {
        decorView.windowInsetsControllerCompat?.run {
            WindowCompat.setDecorFitsSystemWindows(this@isSystemBarVisible, value)
            if (value) {
                show(Type.systemBars())
            } else {
                hide(Type.systemBars())
                // 设置沉浸式交互行为，用户从边缘滑动可以唤出系统栏
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

/**
 * 设置状态栏&导航栏的颜色
 * 获取导航栏&状态栏的颜色
 */
inline var <T : Window> T.systemBarColor: Int
    get() = statusBarColor
    set(value) {
        statusBarColor = value
        navigationBarColor = value
    }

/**
 * 是否是亮色的状态栏&导航栏
 */
inline var <T : Window> T.isLightSystemBar: Boolean
    get() = decorView.windowInsetsControllerCompat?.run {
        isAppearanceLightStatusBars && isAppearanceLightNavigationBars
    } == true
    set(value) {
        decorView.windowInsetsControllerCompat?.run {
            isAppearanceLightStatusBars = value
            isAppearanceLightNavigationBars = value
        }
    }

/**
 * 沉浸式界面
 */
fun <T : Window> T.immerseSystemBar(lightMode: Boolean = true) {
    decorFitsSystemWindows = false
    decorView.windowInsetsControllerCompat?.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    systemBarColor = Color.TRANSPARENT
    isLightSystemBar = lightMode
}