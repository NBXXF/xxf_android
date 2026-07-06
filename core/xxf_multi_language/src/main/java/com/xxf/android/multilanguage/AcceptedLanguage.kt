@file:JvmName("MultiLanguageKt")
@file:JvmMultifileClass
@file:Suppress("unused")

package com.xxf.android.multilanguage

import androidx.core.os.LocaleListCompat
import java.util.Locale

private var cachedAcceptedLanguageTags: String? = null
private var cachedAcceptedLanguages: List<Locale>? = null
private var cachedAcceptedLanguage: String? = null

/**
 * 当前可接受语言列表。
 *
 * 如果应用内指定了语言，优先返回应用语言列表；否则返回系统首选语言列表。
 * 常用于构建 HTTP `Accept-Language` 请求头。
 */
val acceptedLanguages: List<Locale>
    get() {
        val appLocaleList = appLanguages
        val localeList = if (appLocaleList.isEmpty) {
            LocaleListCompat.getAdjustedDefault()
        } else {
            appLocaleList
        }
        val languageTags = localeList.toLanguageTags()
        cachedAcceptedLanguages?.let { cachedLanguages ->
            if (cachedAcceptedLanguageTags == languageTags) {
                return cachedLanguages
            }
        }

        val languages = localeList.toList().ifEmpty {
            listOf(Locale.getDefault())
        }

        cachedAcceptedLanguageTags = languageTags
        cachedAcceptedLanguages = languages
        cachedAcceptedLanguage = null
        return languages
    }

/**
 * HTTP `Accept-Language` 请求头值。
 *
 * 示例：`zh-CN,en-US;q=0.9,en;q=0.8`。
 */
val acceptedLanguage: String
    get() {
        val localeList = if (appLanguages.isEmpty) {
            LocaleListCompat.getAdjustedDefault()
        } else {
            appLanguages
        }
        val languageTags = localeList.toLanguageTags()

        cachedAcceptedLanguage?.let { cachedValue ->
            if (cachedAcceptedLanguageTags == languageTags) {
                return cachedValue
            }
        }

        val languages = localeList.toList().ifEmpty {
            listOf(Locale.getDefault())
        }
        val header = buildString {
            languages.forEachIndexed { index, locale ->
                if (index > 0) append(',')
                append(locale.toLanguageTag())
                if (index > 0) {
                    append(";q=")
                    append(1.0F - index * 0.1F)
                }
            }
        }

        cachedAcceptedLanguageTags = languageTags
        cachedAcceptedLanguages = languages
        cachedAcceptedLanguage = header
        return header
    }

internal fun clearAcceptedLanguageCache() {
    cachedAcceptedLanguageTags = null
    cachedAcceptedLanguages = null
    cachedAcceptedLanguage = null
}

private fun LocaleListCompat.toList(): List<Locale> {
    val locales = mutableListOf<Locale>()
    for (index in 0 until size()) {
        locales.add(this[index] ?: continue)
    }
    return locales
}
