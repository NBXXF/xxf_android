package com.xxf.ktx.window.systembar

import android.graphics.Color
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.xxf.ktx.windowInsetsControllerCompat
import androidx.core.view.WindowInsetsCompat.Type
import com.xxf.ktx.NO_GETTER
import com.xxf.ktx.noGetter
import com.xxf.ktx.rootWindowInsetsCompat

/**
 *设置 Window 的布局是否适配系统窗口插图，可以更灵活地实现沉浸式布局或其他窗口管理需求。
 */
var <T : Window> T.decorFitsSystemWindows: Boolean
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR)
    get() = noGetter()
    set(value) = WindowCompat.setDecorFitsSystemWindows(this, value)

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
            }
        }
    }

/**
 * 状态栏与手势交互
 * BEHAVIOR_SHOW_BARS_BY_TOUCH：表示系统栏通过触摸屏幕的任意位置显示。
 * BEHAVIOR_SHOW_BARS_BY_SWIPE：表示系统栏通过从屏幕边缘的滑动手势显示。
 * BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE：表示系统栏通过滑动手势显示，并且在显示后短暂时间内自动隐藏。
 */
inline var <T : Window> T.systemBarsBehavior: Int
    get() = decorView.windowInsetsControllerCompat?.systemBarsBehavior
        ?: WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
    set(value) {
        decorView.windowInsetsControllerCompat?.run {
            systemBarsBehavior = value
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
 * 导航栏&状态栏
 */
fun <T : Window> T.immerseSystemBar(lightMode: Boolean = true) {
    decorFitsSystemWindows = false
    decorView.windowInsetsControllerCompat?.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    systemBarColor = Color.TRANSPARENT
    isLightSystemBar = lightMode
}