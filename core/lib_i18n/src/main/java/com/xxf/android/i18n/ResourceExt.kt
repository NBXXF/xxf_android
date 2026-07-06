package com.xxf.android.i18n

import android.content.res.ColorStateList
import android.content.res.XmlResourceParser
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.xxf.application.applicationContext
import java.io.InputStream

/**
 * ResourceExt.kt
 * Kotlin Int 扩展资源获取方法
 * --------------------------------------------
 * 使用全局 Application Context，无需传入调用方 Context
 * 所有方法均加 res 前缀，驼峰命名
 * 对应 Android 原始方法已在注释中标明
 */

// -------------------- Color --------------------
// 原始方法：ContextCompat.getColor(context, resId)
fun @receiver:ColorRes Int.resColor(): Int = ContextCompat.getColor(applicationContext, this)

// 原始方法：ContextCompat.getColorStateList(context, resId)
fun @receiver:ColorRes Int.resColorStateList(): ColorStateList? =
    ContextCompat.getColorStateList(applicationContext, this)

// -------------------- Drawable --------------------
// 原始方法：ContextCompat.getDrawable(context, resId)
fun @receiver:DrawableRes Int.resDrawable(): Drawable? = ContextCompat.getDrawable(applicationContext, this)

// -------------------- String --------------------
// 原始方法：context.getString(resId)
fun @receiver:StringRes Int.resString(): String = applicationContext.getLocalizedString(this)

// 原始方法：context.getString(resId, formatArgs)
fun @receiver:StringRes Int.resString(vararg formatArgs: Any?): String =
    applicationContext.getLocalizedString(this, *formatArgs)

// -------------------- Dimen --------------------
// 原始方法：resources.getDimension(resId)
fun @receiver:DimenRes Int.resDimen(): Float = applicationContext.getLocalizedDimension(this)

// 原始方法：resources.getDimensionPixelSize(resId)
fun @receiver:DimenRes Int.resDimenPixelSize(): Int = applicationContext.getLocalizedDimensionPixelSize(this)

// 原始方法：resources.getDimensionPixelOffset(resId)
fun @receiver:DimenRes Int.resDimenPixelOffset(): Int = applicationContext.getLocalizedDimensionPixelOffset(this)

// -------------------- Bool --------------------
// 原始方法：resources.getBoolean(resId)
fun @receiver:BoolRes Int.resBool(): Boolean = applicationContext.getLocalizedBoolean(this)

// -------------------- Integer --------------------
// 原始方法：resources.getInteger(resId)
fun @receiver:IntegerRes Int.resInteger(): Int = applicationContext.getLocalizedInteger(this)

// -------------------- Arrays --------------------
// 原始方法：resources.getStringArray(resId)
fun @receiver:ArrayRes Int.resStringArray(): Array<String> = applicationContext.getLocalizedStringArray(this)

// 原始方法：resources.getIntArray(resId)
fun @receiver:ArrayRes Int.resIntArray(): IntArray = applicationContext.getLocalizedIntArray(this)

// 原始方法：resources.getTextArray(resId)
fun @receiver:ArrayRes Int.resTextArray(): Array<CharSequence> = applicationContext.getLocalizedTextArray(this)

// -------------------- TypedArray --------------------
// 原始方法：context.obtainStyledAttributes(styleableRes, intArrayOf(resId))
fun @receiver:StyleableRes Int.resTypedArray(): android.content.res.TypedArray =
    applicationContext.obtainStyledAttributes(this, intArrayOf(this))

// -------------------- Animation --------------------
// 原始方法：R.anim.xxx，使用在 AnimationUtils.loadAnimation
fun @receiver:AnimRes Int.resAnim(): Int = this

// -------------------- Plurals --------------------
// 原始方法：resources.getQuantityString(resId, quantity, args)
fun @receiver:PluralsRes Int.resQuantityString(quantity: Int, vararg args: Any?): String =
    applicationContext.getLocalizedQuantityString(this, quantity, *args)

// -------------------- Font --------------------
// 原始方法：ResourcesCompat.getFont(context, resId)
fun @receiver:FontRes Int.resFont(): Typeface? = ResourcesCompat.getFont(applicationContext, this)

// -------------------- Xml --------------------
// 原始方法：resources.getXml(resId)
fun @receiver:XmlRes Int.resXml(): XmlResourceParser = applicationContext.getLocalizedXml(this)

// -------------------- Raw --------------------
// 原始方法：resources.openRawResource(resId)
fun @receiver:RawRes Int.resRaw(): InputStream = applicationContext.openLocalizedRawResource(this)

// -------------------- Fraction --------------------
// 原始方法：resources.getFraction(resId, base, pbase)
fun @receiver:FractionRes Int.resFraction(base: Int, pbase: Int): Float =
    applicationContext.getLocalizedFraction(this, base, pbase)
