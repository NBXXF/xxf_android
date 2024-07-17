@file:Suppress("unused")

package com.xxf.ktx

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.os.Build
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import java.util.Locale

private var appLanguageCache: Locale? = null

val systemLanguage: Locale
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        application.resources.configuration.locales[0]
    } else {
        @Suppress("DEPRECATION")
        application.resources.configuration.locale
    }

var appLanguage: Locale
    get() {
        if (appLanguageCache == null) {
            appLanguageCache = LanguageManager.appLanguage
        }
        return appLanguageCache!!
    }
    set(value) {
        appLanguageCache = value
        LanguageManager.appLanguage = value
    }

@SuppressLint("SuspiciousIndentation")
fun resetAppLanguage() {
    appLanguageCache = systemLanguage
    LanguageManager.reset()
}

fun Context.attachLanguage(): ContextWrapper = wrap(appLanguage)

fun Context.wrap(locale: Locale): ContextWrapper {
    if (systemLanguage != locale) {
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            resources.configuration.locale = locale
        }
    }
    return ContextWrapper(createConfigurationContext(resources.configuration))
}

private object LanguageManager {
    private const val KEY_LANGUAGE = "longan_language"
    private const val KEY_COUNTRY = "longan_country"
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(
            "${application.packageName}_preferences",
            Context.MODE_PRIVATE
        )

    var appLanguage: Locale
        get() = language?.let { Locale(it, country.orEmpty()) } ?: systemLanguage
        set(value) {
            sharedPreferences.edit {
                putString(KEY_LANGUAGE, value.language)
                putString(KEY_COUNTRY, value.country)
            }
        }

    fun reset() {
        sharedPreferences.edit {
            remove(KEY_LANGUAGE)
            remove(KEY_COUNTRY)
        }
    }

    private val language: String?
        get() = sharedPreferences.getString(KEY_LANGUAGE, null)

    private val country: String?
        get() = sharedPreferences.getString(KEY_COUNTRY, null)
}

/**
 * 可接受的语言 跟设置中心 设置的语言的顺序
 */
val acceptedLanguages: List<Locale>
    get() {
        return runCatching {
            val adjustedLocaleListCompat = LocaleListCompat.getAdjustedDefault()
            val preferredLocaleList = mutableListOf<Locale>()
            for (index in 0 until adjustedLocaleListCompat.size()) {
                preferredLocaleList.add(adjustedLocaleListCompat[index]!!)
            }
            return preferredLocaleList.ifEmpty {
                listOf(Locale.getDefault())
            }
        }.getOrDefault(listOf(Locale.getDefault()))
    }

/**
 * 可接受的语言 常用于http header
 */
val acceptedLanguage: String
    get() {
        var weight = 1.0F
        return acceptedLanguages
            .map { it.toLanguageTag() }
            // .map { "${it.language}-${it.country}" }
            .reduce { accumulator, languageTag ->
                weight -= 0.1F
                "$accumulator,$languageTag;q=$weight"
            }
    }