package com.xxf.arch.fragment.navigation

import androidx.fragment.app.Fragment
import java.lang.IllegalStateException


/**
 * 是否在导航控制器中
 */
fun Fragment.isInNavController(): Boolean {
    return try {
        findNavContainer()
        true
    } catch (e: Throwable) {
        false
    }
}


/**
 * 获取导航控制器容器
 * 不在导航控制器中会报错
 */
fun Fragment.findNavContainer(): NavigationContainer {
    var findFragment: Fragment? = this
    while (findFragment != null) {
        if (findFragment is NavigationContainer) {
            return findFragment
        }
        findFragment = findFragment.parentFragment
    }
    val act = this.activity
    if (act is NavigationContainer) {
        return act
    }
    throw IllegalStateException(
        "Fragment " + this
                + " does not have a NavController set"
    )
}

/**
 * 深度向上查找到第一个导航控制器
 * 不在导航控制器中会报错
 */
fun Fragment.findNavController(): INavigationController {
    return findNavContainer().getNavigationController()
}

/**
 * 安全 深度向上查找到第一个导航控制器
 */
fun Fragment.findSafeNavController(): INavigationController? {
    if (this.isInNavController()) {
        try {
            return this.findNavController()
        } catch (e: Throwable) {
        }
    }
    return null
}

/**
 * 导航
 * 不在导航控制器中会报错
 */
fun Fragment.navigation(
    destination: Fragment,
    anim: AnimBuilder? = null,
    tag: String? = null,
    flag: Int = -1
) {
    findNavController().navigation(destination, anim, tag, flag)
}

/**
 * 后退
 * 不在导航控制器中会报错
 */
fun Fragment.navigationUp(flag: Int = -1) {
    findNavController().navigationUp(flag)
}