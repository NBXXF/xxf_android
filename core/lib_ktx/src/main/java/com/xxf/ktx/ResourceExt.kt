package com.xxf.ktx

import android.content.res.ColorStateList
import android.content.res.XmlResourceParser
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.io.InputStream

/**
 * ResourceExt.kt
 * Kotlin Int 扩展资源获取方法
 * --------------------------------------------
 * 使用全局 Application，无需传入 Context
 * 所有方法均加 res 前缀，驼峰命名
 * 对应 Android 原始方法已在注释中标明
 */


// -------------------- Color --------------------
// 原始方法：ContextCompat.getColor(context, resId)
fun @receiver:ColorRes Int.resColor(): Int = ContextCompat.getColor(app, this)

// 原始方法：ContextCompat.getColorStateList(context, resId)
fun @receiver:ColorRes Int.resColorStateList(): ColorStateList? =
    ContextCompat.getColorStateList(app, this)

// -------------------- Drawable --------------------
// 原始方法：ContextCompat.getDrawable(context, resId)
fun @receiver:DrawableRes Int.resDrawable(): Drawable? = ContextCompat.getDrawable(app, this)

// -------------------- String --------------------
// 原始方法：context.getString(resId)
fun @receiver:StringRes Int.resString(): String = app.getString(this)

// 原始方法：context.getString(resId, formatArgs)
fun @receiver:StringRes Int.resString(vararg formatArgs: Any): String =
    app.getString(this, *formatArgs)

// -------------------- Dimen --------------------
// 原始方法：resources.getDimension(resId)
fun @receiver:DimenRes Int.resDimen(): Float = app.resources.getDimension(this)

// 原始方法：resources.getDimensionPixelSize(resId)
fun @receiver:DimenRes Int.resDimenPixelSize(): Int = app.resources.getDimensionPixelSize(this)

// 原始方法：resources.getDimensionPixelOffset(resId)
fun @receiver:DimenRes Int.resDimenPixelOffset(): Int = app.resources.getDimensionPixelOffset(this)

// -------------------- Bool --------------------
// 原始方法：resources.getBoolean(resId)
fun @receiver:BoolRes Int.resBool(): Boolean = app.resources.getBoolean(this)

// -------------------- Integer --------------------
// 原始方法：resources.getInteger(resId)
fun @receiver:IntegerRes Int.resInteger(): Int = app.resources.getInteger(this)

// -------------------- Arrays --------------------
// 原始方法：resources.getStringArray(resId)
fun @receiver:ArrayRes Int.resStringArray(): Array<String> = app.resources.getStringArray(this)

// 原始方法：resources.getIntArray(resId)
fun @receiver:ArrayRes Int.resIntArray(): IntArray = app.resources.getIntArray(this)

// 原始方法：resources.getTextArray(resId)
fun @receiver:ArrayRes Int.resTextArray(): Array<CharSequence> = app.resources.getTextArray(this)

// -------------------- TypedArray --------------------
// 原始方法：context.obtainStyledAttributes(styleableRes, intArrayOf(resId))
fun @receiver:StyleableRes Int.resTypedArray(): android.content.res.TypedArray =
    app.obtainStyledAttributes(this, intArrayOf(this))

// -------------------- Animation --------------------
// 原始方法：R.anim.xxx，使用在 AnimationUtils.loadAnimation
fun @receiver:AnimRes Int.resAnim(): Int = this

// -------------------- Plurals --------------------
// 原始方法：resources.getQuantityString(resId, quantity, args)
fun @receiver:PluralsRes Int.resQuantityString(quantity: Int, vararg args: Any): String =
    app.resources.getQuantityString(this, quantity, *args)

// -------------------- Font --------------------
// 原始方法：ResourcesCompat.getFont(context, resId)
fun @receiver:FontRes Int.resFont(): Typeface? = ResourcesCompat.getFont(app, this)

// -------------------- Xml --------------------
// 原始方法：resources.getXml(resId)
fun @receiver:XmlRes Int.resXml(): XmlResourceParser = app.resources.getXml(this)

// -------------------- Raw --------------------
// 原始方法：resources.openRawResource(resId)
fun @receiver:RawRes Int.resRaw(): InputStream = app.resources.openRawResource(this)

// -------------------- Fraction --------------------
// 原始方法：resources.getFraction(resId, base, pbase)
fun @receiver:FractionRes Int.resFraction(base: Int, pbase: Int): Float =
    app.resources.getFraction(this, base, pbase)
