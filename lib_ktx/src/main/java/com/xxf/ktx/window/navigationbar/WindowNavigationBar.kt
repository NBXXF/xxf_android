package com.xxf.ktx.window.navigationbar

import android.view.Window
import androidx.core.view.WindowInsetsCompat.Type
import com.xxf.ktx.rootWindowInsetsCompat
import com.xxf.ktx.windowInsetsControllerCompat


/**
 * 获取导航栏高度
 */
val <T : Window> T.navigationBarHeight: Int
    get() = decorView.rootWindowInsetsCompat?.getInsets(Type.navigationBars())?.bottom
        ?: decorView.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            .let { if (it > 0) decorView.resources.getDimensionPixelSize(it) else 0 }

/**
 * 是否显示导航栏
 */
inline var <T : Window> T.isNavigationBarVisible: Boolean
    get() = decorView.rootWindowInsetsCompat?.isVisible(Type.navigationBars()) == true
    set(value) {
        decorView.windowInsetsControllerCompat?.run {
            if (value) show(Type.navigationBars()) else hide(Type.navigationBars())
        }
    }

/**
 * 是否是亮色的导航栏
 */
inline var <T : Window> T.isLightNavigationBar: Boolean
    get() = decorView.windowInsetsControllerCompat?.isAppearanceLightNavigationBars == true
    set(value) {
        decorView.windowInsetsControllerCompat?.isAppearanceLightNavigationBars = value
    }

