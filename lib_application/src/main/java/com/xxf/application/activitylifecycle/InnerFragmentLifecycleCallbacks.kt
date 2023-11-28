package com.xxf.application.activitylifecycle

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.xxf.application.lifecycle.ViewLifecycleOwner

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
           // f.dialog?.window?.runAlphaDimAnimation()
        }
    }


    /**
     * window 执行dim 动画
     * 在dialogfragment 上再弹dialogfragment 有闪动问题,执行这个动画避免生硬
     */
    private fun Window?.runAlphaDimAnimation() {
        if (this == null) {
            return
        }
        val layoutParams = this.attributes
        val setDimAmount = layoutParams?.dimAmount ?: 0f
        if (setDimAmount > 0f) {
            val objectAnimator = ValueAnimator.ofFloat(0f, setDimAmount)
            objectAnimator.duration = 600
            objectAnimator.addUpdateListener { animation ->
                layoutParams?.dimAmount = animation.animatedValue as Float
                attributes = layoutParams
            }
            objectAnimator.start()
        }
    }
}