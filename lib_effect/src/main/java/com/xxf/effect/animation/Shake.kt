package com.xxf.effect.animation

import android.annotation.SuppressLint
import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import androidx.core.content.ContextCompat

/**
 * 执行抖动动画 相对位置
 * 为了更好效果 请在manifest文件添加android.Manifest.permission.VIBRATE
 * @param counts 执行次数
 * @param toXDelta 绝对距离
 * @param toYDelta 绝对距离
 * @param block
 */
@SuppressLint("MissingPermission")
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
        this.startVibrator()
    }
}

/**
 * 执行抖动动画 相对位置
 * 为了更好效果 请在manifest文件添加android.Manifest.permission.VIBRATE
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
        this.startVibrator()
    }
}

/**
 * 振动
 * https://developer.android.google.cn/develop/ui/views/haptics?hl=cs
 */
@SuppressLint("MissingPermission")
private fun View.startVibrator() {
    try {
        ContextCompat.getSystemService(this.context, Vibrator::class.java)?.also { vibrator ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(100)
            }
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}