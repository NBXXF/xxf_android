package com.xxf.arch.fragment.navigation

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes

class AnimBuilder {
    /**
     * The custom Animation or Animator resource for the enter animation.
     *
     * Note: Animator resources are not supported for navigating to a new Activity
     */
    @AnimRes
    @AnimatorRes
    var enter = -1

    /**
     * The custom Animation or Animator resource for the exit animation.
     *
     * Note: Animator resources are not supported for navigating to a new Activity
     */
    @AnimRes
    @AnimatorRes
    var exit = -1

    /**
     * The custom Animation or Animator resource for the enter animation
     * when popping off the back stack.
     *
     * Note: Animator resources are not supported for navigating to a new Activity
     */
    @AnimRes
    @AnimatorRes
    var popEnter = -1

    /**
     * The custom Animation or Animator resource for the exit animation
     * when popping off the back stack.
     *
     * Note: Animator resources are not supported for navigating to a new Activity
     */
    @AnimRes
    @AnimatorRes
    var popExit = -1
}
