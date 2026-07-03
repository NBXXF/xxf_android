package com.xxf.ktx.window.navigationbar

import androidx.fragment.app.Fragment

/**
 * 获取导航栏高度
 */
val <T : Fragment> T.navigationBarHeight: Int
    get() = requireActivity().navigationBarHeight

/**
 * 是否显示导航栏
 * 只能在界面渲染完成获取
 */
inline var <T : Fragment> T.isNavigationBarVisible: Boolean
    get() = requireActivity().isNavigationBarVisible
    set(value) {
        requireActivity().isNavigationBarVisible = value
    }


/**
 * 是否是亮色的导航栏
 */
inline var <T : Fragment> T.isLightNavigationBar: Boolean
    get() = requireActivity().isLightNavigationBar
    set(value) {
        requireActivity().isLightNavigationBar = value
    }

/**
 * 导航栏背景色
 */
inline var <T : Fragment> T.navigationBarColor: Int
    get() = requireActivity().navigationBarColor
    set(value) {
        requireActivity().navigationBarColor = value
    }

/**
 * 沉浸式界面
 */
fun <T : Fragment> T.immerseNavigationBar(lightMode: Boolean = true) {
    requireActivity().immerseNavigationBar(lightMode = lightMode)
}