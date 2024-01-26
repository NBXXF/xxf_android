package com.xxf.ktx

import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.annotation.AnimRes

/**
 * 设置布局动画拓展拓展
 */
fun ViewGroup.setLayoutAnimation(
    @AnimRes layoutAnimationId: Int,
    block: LayoutAnimationController.() -> Unit = {}
) {
    this.layoutAnimation =
        AnimationUtils.loadLayoutAnimation(context, layoutAnimationId).apply(block)
}

/**
 * 只执行第一次开始
 */
fun ViewGroup.startLayoutAnimationFirstOnly(): Boolean {
    return if (this.getTag<Boolean>(ViewGroup::startLayoutAnimationFirstOnly.name) != true) {
        startLayoutAnimation()
        true
    } else {
        false
    }
}

/**
 * 只执行一次
 */
fun ViewGroup.scheduleLayoutAnimationFirstOnly(): Boolean {
    return if (this.getTag<Boolean>(ViewGroup::scheduleLayoutAnimationFirstOnly.name) != true) {
        scheduleLayoutAnimation()
        true
    } else {
        false
    }
}
