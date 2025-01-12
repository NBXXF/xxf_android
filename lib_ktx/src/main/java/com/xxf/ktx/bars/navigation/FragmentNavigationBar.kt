package com.xxf.ktx.bars.navigation

import androidx.fragment.app.Fragment

/**
 * 获取导航栏高度
 */
val <T : Fragment> T.navigationBarHeight: Int
    get() = requireActivity().navigationBarHeight

/**
 * 是否显示导航栏
 */
inline var <T : Fragment> T.isNavigationBarVisible: Boolean
    get() = requireActivity().isNavigationBarVisible
    set(value) {
        requireActivity().isNavigationBarVisible = value
    }


/**
 * 使导航栏背景色透明
 */
fun <T : Fragment> T.transparentNavigationBar() {
    requireActivity().transparentNavigationBar()
}
