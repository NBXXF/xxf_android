package com.xxf.ktx.window.statusbar

import android.graphics.Color
import android.view.Window
import androidx.core.view.WindowInsetsCompat.Type
import androidx.core.view.WindowInsetsControllerCompat
import com.xxf.ktx.rootWindowInsetsCompat
import com.xxf.ktx.window.systembar.decorFitsSystemWindows
import com.xxf.ktx.windowInsetsControllerCompat


/**
 * 状态栏的高度
 */
val <T : Window> T.statusBarHeight: Int
    get() = decorView.rootWindowInsetsCompat?.getInsets(Type.statusBars())?.top
        ?: decorView.resources.getIdentifier("status_bar_height", "dimen", "android")
            .let { if (it > 0) decorView.resources.getDimensionPixelSize(it) else 0 }

/**
 * 是否是亮色的状态栏
 */
inline var <T : Window> T.isLightStatusBar: Boolean
    get() = decorView.windowInsetsControllerCompat?.isAppearanceLightStatusBars == true
    set(value) {
        decorView.windowInsetsControllerCompat?.isAppearanceLightStatusBars = value
    }

/**
 * 状态栏是否可见
 */
inline var <T : Window> T.isStatusBarVisible: Boolean
    get() = decorView.rootWindowInsetsCompat?.isVisible(Type.statusBars()) == true
    set(value) {
        decorView.windowInsetsControllerCompat?.run {
            if (value) show(Type.statusBars()) else hide(Type.statusBars())
        }
    }


/**
 * 沉浸式界面
 */
fun <T : Window> T.immerseStatusBar(lightMode: Boolean = true) {
    decorFitsSystemWindows = false
    decorView.windowInsetsControllerCompat?.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    statusBarColor = Color.TRANSPARENT
    isLightStatusBar = lightMode
}

