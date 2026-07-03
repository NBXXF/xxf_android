package com.xxf.arch.fragment.navigation

import androidx.navigation.AnimBuilder
import com.xxf.arch.R

fun AnimBuilder.withDefaultHorizontalSwitchAnimation(): AnimBuilder = apply {
    this.enter = (R.anim.navigation_horizontal_open_enter)
    this.exit = (R.anim.navigation_horizontal_open_exit)
    this.popEnter = (R.anim.navigation_horizontal_close_enter)
    this.popExit = (R.anim.navigation_horizontal_close_exit)
}
