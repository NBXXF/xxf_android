@file:Suppress("unused")

package com.xxf.ktx

import android.content.Context
import java.lang.reflect.Modifier

/**
 * @version 1.0.0
 * @Author: XXF  https://github.com/NBXXF
 * @Description BuildConfig reflection helpers.
 * @date createTime：2018/7/3
 */

/**
 * 通过Context读取宿主App BuildConfig中的所有静态常量。
 *
 * 默认优先使用`${packageName}.BuildConfig`查找, 再尝试Application类所在包下的BuildConfig。
 * 若仍找不到, 请使用[getBuildConfigFields]传入真实的BuildConfig类名。
 */
val Context.buildConfigFields: Map<String, Any?>
    get() = getBuildConfigFields()

/**
 * 通过Context读取指定BuildConfig类中的所有静态常量。
 */
fun Context.getBuildConfigFields(
    buildConfigClassName: String? = null
): Map<String, Any?> {
    val classNames = buildConfigClassName?.let(::listOf) ?: defaultBuildConfigClassNames
    return classNames.firstNotNullOfOrNull { className ->
        runCatching { Class.forName(className).getStaticFinalFields() }.getOrNull()
    }.orEmpty()
}

/**
 * 通过Context读取指定BuildConfig类中的单个静态常量。
 */
fun Context.getBuildConfigValue(
    name: String,
    buildConfigClassName: String? = null
): Any? = getBuildConfigFields(buildConfigClassName)[name]

private val Context.defaultBuildConfigClassNames: List<String>
    get() = listOfNotNull(
        "$packageName.BuildConfig",
        applicationInfo.className
            ?.takeUnless { it.startsWith(".") }
            ?.substringBeforeLast('.', missingDelimiterValue = "")
            ?.takeIf { it.isNotEmpty() }
            ?.let { "$it.BuildConfig" }
    ).distinct()

private fun Class<*>.getStaticFinalFields(): Map<String, Any?> {
    return declaredFields
        .filter { field ->
            Modifier.isStatic(field.modifiers) && Modifier.isFinal(field.modifiers)
        }
        .associate { field ->
            field.isAccessible = true
            field.name to field.get(null)
        }
}
