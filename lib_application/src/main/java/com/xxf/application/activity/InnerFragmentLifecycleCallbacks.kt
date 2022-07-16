package com.xxf.application.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.xxf.application.lifecycle.ViewLifecycleOwner
import com.xxf.arch.dialog.runAlphaDimAnimation

/**
 * 内部处理fragment
 */
internal object InnerFragmentLifecycleCallbacks : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
        //设置关联最近的lifecycle
        ViewLifecycleOwner.set(v, f)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        super.onFragmentStarted(fm, f)
        /**
         * 处理dialogFragment 动画
         */
        if (f is DialogFragment && f.showsDialog) {
            f.dialog?.window?.runAlphaDimAnimation()
        }
    }
}