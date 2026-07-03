package com.xxf.ktx.window.statusbar

import androidx.fragment.app.Fragment
import com.xxf.ktx.NO_GETTER
import com.xxf.ktx.noGetter

/**
 *设置 Window 的布局是否适配系统窗口插图，可以更灵活地实现沉浸式布局或其他窗口管理需求。
 */
var <T : Fragment> T.decorFitsSystemWindows: Boolean
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR)
    get() = noGetter()
    set(value) {
        requireActivity().decorFitsSystemWindows = value
    }


/**
 * 状态栏的高度
 */
val <T : Fragment> T.statusBarHeight: Int
    get() = requireActivity().statusBarHeight

/**
 * 是否是亮色的状态栏
 */
inline var <T : Fragment> T.isLightStatusBar: Boolean
    get() = requireActivity().isLightStatusBar
    set(value) {
        requireActivity().isLightStatusBar = value;
    }

/**
 * 状态栏是否可见
 * 只能在界面渲染完成获取
 */
inline var <T : Fragment> T.isStatusBarVisible: Boolean
    get() = requireActivity().isStatusBarVisible
    set(value) {
        requireActivity().isStatusBarVisible = value
    }


/**
 * 沉浸式界面
 */
fun <T : Fragment> T.immerseStatusBar(lightMode: Boolean = true) {
    requireActivity().immerseStatusBar(lightMode = lightMode)
}


/**
 * 状态栏背景色透明
 */
inline var <T : Fragment> T.statusBarColor: Int
    get() = requireActivity().statusBarColor
    set(value) {
        requireActivity().statusBarColor = value
    }