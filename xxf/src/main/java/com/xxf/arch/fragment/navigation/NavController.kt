package com.xxf.arch.fragment.navigation

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xxf.arch.R

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2022/2/22
 */
class NavController(val lifecycle: LifecycleOwner, val fragmentManager: FragmentManager) :
    INavigationController {
    override fun navigation(destination: Fragment, anim: AnimBuilder?, tag: String?, flag: Int) {
        checkFragment(destination)
        fragmentManager.beginTransaction()
            .apply {
                if (anim != null) {
                    setCustomAnimations(
                        anim.enter,
                        anim.exit,
                        anim.popEnter,
                        anim.popExit
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

    /**
     * 检查childFragment
     * 子集是 BottomDialogFragment 会导致整体滚动问题
     * 子集是 DialogFragment 调用自身的dismissXxx方法 会导致白屏
     */
    private fun checkFragment(fragment: Fragment) {
        if (fragment is DialogFragment) {
            throw IllegalStateException(
                "Fragment " + this
                        + " does not be DialogFragment"
            )
        }
    }

    override fun navigationUp(flag: Int): Boolean {
        if (flag == Intent.FLAG_ACTIVITY_CLEAR_TASK) {
            if (lifecycle is DialogFragment) {
                lifecycle.dismissAllowingStateLoss()
                return true
            } else if (lifecycle is Activity) {
                lifecycle.onBackPressed()
                return true
            }
            return false
        } else {
            if (fragmentManager.backStackEntryCount > 1) {
                fragmentManager.popBackStack()
                return true
            } else {
                if (lifecycle is DialogFragment) {
                    lifecycle.dismissAllowingStateLoss()
                    return true
                } else if (lifecycle is Activity) {
                    lifecycle.onBackPressed()
                    return true
                }
                return false
            }
        }
    }

    override fun getNavigationCount(): Int {
        return fragmentManager.backStackEntryCount
    }

    override fun getNavigationFragmentManager(): FragmentManager {
        return fragmentManager
    }

    override fun getNavigationLifecycleOwner(): LifecycleOwner {
        return lifecycle
    }

    override fun finishNavigation(): Boolean {
        if (lifecycle is DialogFragment) {
            lifecycle.dismissAllowingStateLoss()
            return true
        } else if (lifecycle is Activity) {
            lifecycle.onBackPressed()
            return true
        }
        return false
    }
}