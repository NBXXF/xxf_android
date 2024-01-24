package com.xxf.arch.fragment.navigation

import androidx.navigation.NavOptions
import com.xxf.arch.R

/**
 * 转换
 */
fun NavOptions.newBuilder(): NavOptions.Builder = run {
    return NavOptions.Builder().setLaunchSingleTop(this.shouldLaunchSingleTop())
        .setRestoreState(this.shouldRestoreState())
        .setPopUpTo(
            this.popUpToId,
            this.isPopUpToInclusive(),
            this.shouldPopUpToSaveState()
        )
        .setEnterAnim(this.enterAnim)
        .setExitAnim(this.exitAnim)
        .setPopEnterAnim(this.popEnterAnim)
        .setPopExitAnim(this.popExitAnim)
}

/**
 * 转换
 */
fun NavOptions.Builder.of(options: NavOptions): NavOptions.Builder = options.newBuilder()

/**
 * 默认动画
 */
fun NavOptions.Builder.withDefaultHorizontalSwitchAnimation(): NavOptions.Builder = run {
    this.setEnterAnim(R.anim.navigation_horizontal_open_enter)
        .setExitAnim(R.anim.navigation_horizontal_open_exit)
        .setPopEnterAnim(R.anim.navigation_horizontal_close_enter)
        .setPopExitAnim(R.anim.navigation_horizontal_close_exit)
}

/**
 * 去除动画
 */
fun NavOptions.Builder.notAnimation(): NavOptions.Builder = run {
    this.setEnterAnim(-1)
        .setExitAnim(-1)
        .setPopEnterAnim(-1)
        .setPopExitAnim(-1)
}