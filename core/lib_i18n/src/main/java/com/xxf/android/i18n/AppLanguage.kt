@file:JvmName("MultiLanguageKt")
@file:JvmMultifileClass
@file:Suppress("unused")

package com.xxf.android.i18n

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

private var cachedAppLanguageTags: String? = null
private var cachedAppLanguageList: LocaleListCompat? = null

/**
 * 当前系统语言。
 *
 * 注意：这是系统/用户首选语言，不一定等于应用内当前指定语言。
 */
val systemLanguage: Locale
    get() = LocaleListCompat.getAdjustedDefault()[0] ?: Locale.getDefault()

/**
 * 当前应用语言。
 *
 * 设置该属性会同步写入本模块缓存，并调用 [AppCompatDelegate.setApplicationLocales]。
 * 如果当前没有指定应用语言，读取时返回 [systemLanguage]。
 */
var appLanguage: Locale
    get() = appLanguages[0] ?: systemLanguage
    set(value) {
        appLanguages = LocaleListCompat.create(value)
    }

/**
 * 当前应用语言列表。
 *
 * 优先读取 AppCompat 当前语言；如果 AppCompat 还没有恢复语言，则读取本模块通过
 * `SharedPreferencesOwner` 缓存的语言列表。
 */
var appLanguages: LocaleListCompat
    get() {
        val applicationLocales = AppCompatDelegate.getApplicationLocales()
        if (!applicationLocales.isEmpty) {
            return applicationLocales
        }
        return cachedAppLanguages
    }
    set(value) {
        val languageTags = value.toLanguageTags()
        if (MultiLanguagePreferences.languageTags == languageTags &&
            AppCompatDelegate.getApplicationLocales().toLanguageTags() == languageTags
        ) {
            return
        }

        MultiLanguagePreferences.languageTags = languageTags
        updateCachedAppLanguages(languageTags, value)
        clearLocalizedContextCache()
        clearAcceptedLanguageCache()
        AppCompatDelegate.setApplicationLocales(value)
    }

/**
 * 本模块缓存的应用语言列表。
 *
 * 仅表示本地持久化值，不代表 AppCompat 当前已经应用到 Activity Context。
 */
val cachedAppLanguages: LocaleListCompat
    get() {
        val languageTags = MultiLanguagePreferences.languageTags
        cachedAppLanguageList?.let { cachedList ->
            if (cachedAppLanguageTags == languageTags) {
                return cachedList
            }
        }
        return LocaleListCompat.forLanguageTags(languageTags).also {
            updateCachedAppLanguages(languageTags, it)
        }
    }

/**
 * 使用 BCP-47 language tag 设置应用语言。
 *
 * 示例：`zh-CN`、`en`、`en-US`。调用后 AppCompat 通常会重建当前
 * [androidx.appcompat.app.AppCompatActivity]。
 */
fun setAppLanguage(languageTag: String) {
    appLanguages = LocaleListCompat.forLanguageTags(languageTag)
}

/**
 * 清除应用内语言，恢复跟随系统语言。
 */
fun resetAppLanguage() {
    appLanguages = LocaleListCompat.getEmptyLocaleList()
}

/**
 * 从本模块缓存恢复 AppCompat 应用语言。
 *
 * 建议在 Application `onCreate` 或首个 Activity `onCreate` 早期调用一次。
 * Android 12 及以下没有系统级应用语言存储，本方法用于保证进程重启后能恢复应用内语言。
 */
fun restoreCachedAppLanguage() {
    val cachedLanguages = cachedAppLanguages
    if (!cachedLanguages.isEmpty && AppCompatDelegate.getApplicationLocales().isEmpty) {
        clearLocalizedContextCache()
        clearAcceptedLanguageCache()
        AppCompatDelegate.setApplicationLocales(cachedLanguages)
    }
}

private fun updateCachedAppLanguages(languageTags: String, languages: LocaleListCompat) {
    cachedAppLanguageTags = languageTags
    cachedAppLanguageList = languages
}
