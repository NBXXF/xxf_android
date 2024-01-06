@file:Suppress("unused")

package com.xxf.ktx

import android.content.res.Resources
import android.util.TypedValue

/**
 * 设计规范为 元数据什么类型 就返回什么类型 更方便业务,业务大部分是Int
 * 如果参与多运算 建议先将原数据 转换成Float 再用拓展 以保证计算的准确性
 *
 * 字段级别 都是最终为像素
 */

inline val Int.dp: Int get() = toFloat().dp.toInt()

inline val Long.dp: Long get() = toFloat().dp.toLong()

inline val Double.dp: Double get() = toFloat().dp.toDouble()

inline val Float.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )


inline val Int.sp: Int get() = toFloat().sp.toInt()

inline val Long.sp: Long get() = toFloat().sp.toLong()

inline val Double.sp: Double get() = toFloat().sp.toDouble()

inline val Float.sp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
    )


/**
 * 转换方法
 */

fun Int.pxToDp(): Int = toFloat().pxToDp().toInt()

fun Long.pxToDp(): Long = toFloat().pxToDp().toLong()

fun Double.pxToDp(): Double = toFloat().pxToDp().toDouble()

fun Float.pxToDp(): Float = (this / Resources.getSystem().displayMetrics.density + 0.5f)


fun Int.pxToSp(): Int = toFloat().pxToSp().toInt()

fun Long.pxToSp(): Long = toFloat().pxToSp().toLong()

fun Double.pxToSp(): Double = toFloat().pxToSp().toDouble()

fun Float.pxToSp(): Float = (this / Resources.getSystem().displayMetrics.scaledDensity + 0.5f)
