package com.xxf.effect.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation

/**
 * 执行抖动动画 相对位置
 * @param counts 执行次数
 * @param toXDelta 绝对距离
 * @param toYDelta 绝对距离
 * @param block
 */
fun <T : View> T.startAnimationShake(
    counts: Int = 5,
    toXDelta: Float = 0f,
    toYDelta: Float = 10f,
    block: TranslateAnimation.() -> TranslateAnimation = { this }
): Animation {
    val translateAnimation: TranslateAnimation = TranslateAnimation(0f, toXDelta, 0f, toYDelta)
    translateAnimation.interpolator = CycleInterpolator(counts.toFloat())
    translateAnimation.duration = 500
    return translateAnimation.run(block).also {
        this.startAnimation(it)
    }
}

/**
 * 执行抖动动画 相对位置
 * @param counts 执行次数
 * @param toXValue 百分比
 * @param toYValue 百分比
 * @param block
 */
fun <T : View> T.startAnimationShakeRelative(
    counts: Int = 5,
    toXValue: Float = 0f,
    toYValue: Float = 0.5f,
    block: TranslateAnimation.() -> TranslateAnimation = { this }
): Animation {
    // type = Animation.RELATIVE_TO_SELF 表示x坐标移动到相对自己的0.5倍距离
    // type = Animation.RELATIVE_TO_PARENT 表示x坐标移动到自己父控件x轴的0.5倍距离 即 x + 0.5ParentX

    val translateAnimation = TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, toXValue,
        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, toYValue
    );
    translateAnimation.interpolator = CycleInterpolator(counts.toFloat())
    translateAnimation.duration = 500
    return translateAnimation.run(block).also {
        this.startAnimation(it)
    }
}