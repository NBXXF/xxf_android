@file:JvmName("ComposeResourceExtKt")
@file:Suppress("unused")

package com.xxf.android.i18n

import androidx.annotation.ArrayRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

/**
 * Compose 场景下的多语言资源读取封装。
 *
 * 用法与 `androidx.compose.ui.res.stringResource(...)` 类似，但读取的是本模块的
 * localizedContext 结果，因此会跟随应用内语言设置。
 */
@Composable
@ReadOnlyComposable
fun localizedStringResource(@StringRes id: Int): String {
    return LocalContext.current.getLocalizedString(id)
}

/**
 * Compose 场景下读取带格式化参数的字符串资源。
 */
@Composable
@ReadOnlyComposable
fun localizedStringResource(
    @StringRes id: Int,
    vararg formatArgs: Any?
): String {
    return LocalContext.current.getLocalizedString(id, *formatArgs)
}

/**
 * Compose 场景下读取复数资源。
 */
@Composable
@ReadOnlyComposable
fun localizedPluralStringResource(
    @PluralsRes id: Int,
    quantity: Int,
    vararg formatArgs: Any?
): String {
    return LocalContext.current.getLocalizedQuantityString(id, quantity, *formatArgs)
}

/**
 * Compose 场景下读取字符串数组资源。
 */
@Composable
@ReadOnlyComposable
fun localizedStringArrayResource(@ArrayRes id: Int): Array<String> {
    return LocalContext.current.getLocalizedStringArray(id)
}
