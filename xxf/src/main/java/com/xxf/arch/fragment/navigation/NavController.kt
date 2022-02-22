package com.xxf.arch.fragment.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.xxf.arch.R

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2022/2/22
 */
class NavController(val fragmentManager: FragmentManager) : INavigationController {
    override fun navigation(destination: Fragment, anim: Boolean, tag: String?, flag: Int) {
        fragmentManager.beginTransaction()
            .apply {
                if (anim) {
                    setCustomAnimations(
                        R.anim.xxf_activity_open_enter,
                        R.anim.xxf_activity_open_exit,
                        R.anim.xxf_activity_close_enter,
                        R.anim.xxf_activity_close_exit
                    )
                }
                fragmentManager.fragments.forEach { f ->
                    this.hide(f)
                    setMaxLifecycle(f, Lifecycle.State.STARTED)
                }
            }
            .add(R.id.navigation_fragment_container, destination, tag)
            .setMaxLifecycle(destination, Lifecycle.State.RESUMED)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun navigationUp(flag: Int): Boolean {
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStack()
            return true
        } else {
            return false
        }
    }
}