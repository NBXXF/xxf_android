package com.xxf.effect.recyclerview.demo

import android.view.animation.Interpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.xxf.effect.recyclerview.animators.ScaleInRightAnimator

class MyAnimator(
  interpolator: Interpolator = LinearOutSlowInInterpolator()
) : ScaleInRightAnimator(interpolator) {
}
