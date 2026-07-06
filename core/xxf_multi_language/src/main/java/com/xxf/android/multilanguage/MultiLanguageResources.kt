@file:JvmName("MultiLanguageKt")
@file:JvmMultifileClass
@file:Suppress("unused")

package com.xxf.android.multilanguage

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.XmlResourceParser
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.io.InputStream

/**
 * 使用当前应用语言读取字符串资源。
 *
 * 全局工具类不要直接使用 `application.getString(resId)` 读取语言敏感文案，优先使用本方法。
 */
fun Context.getLocalizedString(resId: Int): String {
    val context = localizedContext()
    return context.getString(resId)
}

/**
 * 使用当前应用语言读取带格式化参数的字符串资源。
 */
fun Context.getLocalizedString(resId: Int, vararg formatArgs: Any): String {
    val context = localizedContext()
    return context.getString(resId, *formatArgs)
}

/**
 * 使用当前应用语言读取文本资源。
 */
fun Context.getLocalizedText(resId: Int): CharSequence {
    val context = localizedContext()
    return context.getText(resId)
}

/**
 * 使用当前应用语言读取字符串数组资源。
 */
fun Context.getLocalizedStringArray(resId: Int): Array<String> {
    val resources = localizedContext().resources
    return resources.getStringArray(resId)
}

/**
 * 使用当前应用语言读取文本数组资源。
 */
fun Context.getLocalizedTextArray(resId: Int): Array<CharSequence> {
    val resources = localizedContext().resources
    return resources.getTextArray(resId)
}

/**
 * 使用当前应用语言读取复数资源。
 */
fun Context.getLocalizedQuantityString(resId: Int, quantity: Int): String {
    val resources = localizedContext().resources
    return resources.getQuantityString(resId, quantity)
}

/**
 * 使用当前应用语言读取带格式化参数的复数资源。
 */
fun Context.getLocalizedQuantityString(
    resId: Int,
    quantity: Int,
    vararg formatArgs: Any
): String {
    val resources = localizedContext().resources
    return resources.getQuantityString(resId, quantity, *formatArgs)
}

/**
 * 使用当前应用语言读取整型数组资源。
 *
 * 该资源通常不随语言变化，但如果业务使用 locale 资源目录覆盖数组，本方法会按当前应用语言读取。
 */
fun Context.getLocalizedIntArray(resId: Int): IntArray {
    val resources = localizedContext().resources
    return resources.getIntArray(resId)
}

/**
 * 使用当前应用语言读取布尔资源。
 */
fun Context.getLocalizedBoolean(resId: Int): Boolean {
    val resources = localizedContext().resources
    return resources.getBoolean(resId)
}

/**
 * 使用当前应用语言读取整数资源。
 */
fun Context.getLocalizedInteger(resId: Int): Int {
    val resources = localizedContext().resources
    return resources.getInteger(resId)
}

/**
 * 使用当前应用语言读取尺寸资源。
 */
fun Context.getLocalizedDimension(resId: Int): Float {
    val resources = localizedContext().resources
    return resources.getDimension(resId)
}

/**
 * 使用当前应用语言读取像素尺寸资源。
 */
fun Context.getLocalizedDimensionPixelSize(resId: Int): Int {
    val resources = localizedContext().resources
    return resources.getDimensionPixelSize(resId)
}

/**
 * 使用当前应用语言读取像素偏移尺寸资源。
 */
fun Context.getLocalizedDimensionPixelOffset(resId: Int): Int {
    val resources = localizedContext().resources
    return resources.getDimensionPixelOffset(resId)
}

/**
 * 使用当前应用语言读取颜色资源。
 */
fun Context.getLocalizedColor(resId: Int): Int {
    val context = localizedContext()
    return ContextCompat.getColor(context, resId)
}

/**
 * 使用当前应用语言读取颜色状态列表资源。
 */
fun Context.getLocalizedColorStateList(resId: Int): ColorStateList? {
    val context = localizedContext()
    return ContextCompat.getColorStateList(context, resId)
}

/**
 * 使用当前应用语言读取 Drawable 资源。
 */
fun Context.getLocalizedDrawable(resId: Int): Drawable? {
    val context = localizedContext()
    return ContextCompat.getDrawable(context, resId)
}

/**
 * 使用当前应用语言读取字体资源。
 */
fun Context.getLocalizedFont(resId: Int): Typeface? {
    val context = localizedContext()
    return ResourcesCompat.getFont(context, resId)
}

/**
 * 使用当前应用语言读取 XML 资源。
 */
fun Context.getLocalizedXml(resId: Int): XmlResourceParser {
    val resources = localizedContext().resources
    return resources.getXml(resId)
}

/**
 * 使用当前应用语言打开 raw 资源。
 */
fun Context.openLocalizedRawResource(resId: Int): InputStream {
    val resources = localizedContext().resources
    return resources.openRawResource(resId)
}

/**
 * 使用当前应用语言读取 fraction 资源。
 */
fun Context.getLocalizedFraction(resId: Int, base: Int, pbase: Int): Float {
    val resources = localizedContext().resources
    return resources.getFraction(resId, base, pbase)
}
