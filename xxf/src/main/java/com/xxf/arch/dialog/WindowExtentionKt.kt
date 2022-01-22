package com.xxf.arch.dialog

import android.animation.ValueAnimator
import android.view.Window


/**
 * window 执行dim 动画
 * 一般是避免生硬
 */
fun Window?.runAlphaDimAnimation() {
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