

@file:Suppress("unused")

package com.xxf.ktx

import android.content.res.Resources
import android.util.TypedValue

inline val Int.dp: Float get() = toFloat().dp

inline val Long.dp: Float get() = toFloat().dp

inline val Double.dp: Float get() = toFloat().dp

inline val Float.dp: Float
  get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

inline val Int.sp: Float get() = toFloat().sp

inline val Long.sp: Float get() = toFloat().sp

inline val Double.sp: Float get() = toFloat().sp

inline val Float.sp: Float
  get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)

fun Int.pxToDp(): Int = toFloat().pxToDp()

fun Long.pxToDp(): Int = toFloat().pxToDp()

fun Double.pxToDp(): Int = toFloat().pxToDp()

fun Float.pxToDp(): Int =
  (this / Resources.getSystem().displayMetrics.density + 0.5f).toInt()

fun Int.pxToSp(): Int = toFloat().pxToSp()

fun Long.pxToSp(): Int = toFloat().pxToSp()

fun Double.pxToSp(): Int = toFloat().pxToSp()

fun Float.pxToSp(): Int =
  (this / Resources.getSystem().displayMetrics.scaledDensity + 0.5f).toInt()
