package com.xxf.effect.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation

/**
 * 执行抖动动画
 */
fun <T : View> T.startAnimationShake(
    counts: Int = 5,
    block: TranslateAnimation.() -> Unit = {}
): Animation {
    val translateAnimation: TranslateAnimation = TranslateAnimation(0f, 0f, 0f, 10f)
    translateAnimation.interpolator = CycleInterpolator(counts.toFloat())
    translateAnimation.duration = 500
    return translateAnimation.apply(block).also {
        this.startAnimation(it)
    }
}