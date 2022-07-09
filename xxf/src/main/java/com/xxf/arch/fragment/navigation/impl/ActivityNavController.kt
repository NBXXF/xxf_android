package com.xxf.arch.fragment.navigation.impl

import android.app.Activity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.xxf.arch.fragment.navigation.NavController

open class ActivityNavController : NavController {
    constructor(lifecycle: FragmentActivity) : super(
        lifecycle,
        lifecycle.supportFragmentManager
    )

    override fun finishNavigation(): Boolean {
        if (lifecycle is DialogFragment) {
            lifecycle.dismissAllowingStateLoss()
            return true
        } else if (lifecycle is Activity) {
            lifecycle.finish()
            return true
        }
        return false
    }
}