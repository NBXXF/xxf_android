package com.xxf.arch.fragment.navigation

import androidx.fragment.app.Fragment
import java.lang.IllegalStateException


/**
 * 是否在导航控制器中
 */
fun Fragment.isInNavController(): Boolean {
    var findFragment: Fragment? = this
    while (findFragment != null) {
        if (findFragment is NavigationOwner) {
            return true
        }
        findFragment = findFragment.parentFragment
    }
    return false
}


/**
 * 深度向上查找到第一个导航控制器
 */
fun Fragment.findNavController(): INavigationController {
    var findFragment: Fragment? = this
    while (findFragment != null) {
        if (findFragment is NavigationOwner) {
            return findFragment.getNavigation()
        }
        findFragment = findFragment.parentFragment
    }
    throw IllegalStateException(
        "Fragment " + this
                + " does not have a NavController set"
    )
}

/**
 * 导航
 */
fun Fragment.navigation(
    destination: Fragment,
    anim: Boolean = true,
    tag: String? = null,
    flag: Int = -1
) {
    findNavController().navigation(destination, anim, tag, flag)
}

/**
 * 后退
 */
fun Fragment.navigationUp(flag: Int = -1) {
    findNavController().navigationUp(flag)
}