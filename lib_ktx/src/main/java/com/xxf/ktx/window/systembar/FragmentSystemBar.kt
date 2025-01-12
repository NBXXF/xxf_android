package com.xxf.ktx.window.systembar

import androidx.fragment.app.Fragment
import com.xxf.ktx.window.statusbar.isLightStatusBar

/**
 * 状态栏和导航栏是否可见
 */
inline var <T : Fragment> T.isSystemBarVisible: Boolean
    get() = requireActivity().isSystemBarVisible
    set(value) {
        requireActivity().isSystemBarVisible = value
    }

/**
 * 设置状态栏和导航栏的颜色
 * 获取导航栏活着状态栏的颜色
 */
inline var <T : Fragment> T.systemBarColor: Int
    get() = requireActivity().systemBarColor
    set(value) {
        requireActivity().systemBarColor = value
    }

/**
 * 是否是亮色的状态栏&导航栏
 */
inline var <T : Fragment> T.isLightSystemBar: Boolean
    get() = requireActivity().isLightStatusBar
    set(value) {
        requireActivity().isLightStatusBar = value
    }

/**
 * 沉浸式界面
 */
fun <T : Fragment> T.immerseSystemBar(lightMode: Boolean = true) {
    requireActivity().immerseSystemBar(lightMode = lightMode)
}