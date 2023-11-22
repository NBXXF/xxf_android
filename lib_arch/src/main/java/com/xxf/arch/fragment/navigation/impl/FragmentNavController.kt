package com.xxf.arch.fragment.navigation.impl

import android.app.Activity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.xxf.arch.fragment.navigation.NavController

open class FragmentNavController : NavController {
    constructor(lifecycle: Fragment) : super(
        lifecycle,
        lifecycle.childFragmentManager
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