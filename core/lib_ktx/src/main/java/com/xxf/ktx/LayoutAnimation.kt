package com.xxf.ktx

import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.annotation.AnimRes

/**
 * 设置布局动画拓展拓展
 */
fun <T:ViewGroup> T.setLayoutAnimation(
    @AnimRes layoutAnimationId: Int,
    block: LayoutAnimationController.() -> Unit = {}
) {
    this.layoutAnimation =
        AnimationUtils.loadLayoutAnimation(context, layoutAnimationId).apply(block)
}

/**
 * 只执行第一次开始
 */
fun <T:ViewGroup> T.startLayoutAnimationFirstOnly(): Boolean {
    val key = ViewGroup::startLayoutAnimationFirstOnly.name
    return if (this.getTag<Boolean>(key) != true) {
        startLayoutAnimation()
        this.setTag(key, true)
        true
    } else {
        false
    }
}

/**
 * 只执行第一次开始
 */
fun <T:ViewGroup> T.scheduleLayoutAnimationFirstOnly(): Boolean {
    val key = ViewGroup::scheduleLayoutAnimationFirstOnly.name
    return if (this.getTag<Boolean>(key) != true) {
        scheduleLayoutAnimation()
        this.setTag(key, true)
        true
    } else {
        false
    }
}
