package com.xxf.ktx.window.insets

import android.app.Activity
import android.view.View
import android.view.Window
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type
import androidx.fragment.app.Fragment
import com.xxf.ktx.doOnApplyWindowInsets

/**
 * 导航栏是否显示
 * 只能在界面渲染完成获取
 */
inline val <T : WindowInsetsCompat> T.isNavigationBarVisible: Boolean
    get() = isVisible(Type.navigationBars())

/**
 * 状态栏是否显示
 * 只能在界面渲染完成获取
 */
inline val <T : WindowInsetsCompat> T.isStatusBarBarVisible: Boolean
    get() = isVisible(Type.statusBars())

/**
 * 监听窗口的 WindowInsets 变化，捕获导航栏的显示状态。
 */
fun <T : Window> T.doOnApplyWindowInsets(action: (View, WindowInsetsCompat) -> WindowInsetsCompat) {
    this.decorView.doOnApplyWindowInsets(action)
}

/**
 * 监听窗口的 WindowInsets 变化，捕获导航栏的显示状态。
 */
fun <T : Activity> T.doOnApplyWindowInsets(action: (View, WindowInsetsCompat) -> WindowInsetsCompat) {
    this.window.doOnApplyWindowInsets(action)
}

/**
 * 监听窗口的 WindowInsets 变化，捕获导航栏的显示状态。
 */
fun <T : Fragment> T.doOnApplyWindowInsets(action: (View, WindowInsetsCompat) -> WindowInsetsCompat) {
    this.requireActivity().doOnApplyWindowInsets(action)
}


