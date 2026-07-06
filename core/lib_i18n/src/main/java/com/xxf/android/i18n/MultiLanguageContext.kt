@file:JvmName("MultiLanguageKt")
@file:JvmMultifileClass
@file:Suppress("unused")

package com.xxf.android.i18n

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import androidx.core.os.LocaleListCompat
import java.util.Locale

private val localizedContextCacheLock = Any()
private var cachedLocalizedContext: Context? = null
private var cachedLocalizedContextKey: String? = null

/**
 * 使用当前应用语言包装 Context。
 *
 * 这是高级/特殊场景 API。使用 [androidx.appcompat.app.AppCompatDelegate.setApplicationLocales]
 * 后，继承 [androidx.appcompat.app.AppCompatActivity] 的页面通常不需要在
 * `attachBaseContext` 中调用本方法。
 *
 * 仅当非 AppCompat 页面、第三方页面、Service/Receiver 等特殊 Context 链路没有走
 * AppCompatDelegate，且你明确需要一个 ContextWrapper 时再使用。
 *
 * 全局资源读取优先使用 [localizedContext] 或 `getLocalizedXxx(...)` 系列方法。
 */
fun Context.attachLanguage(): ContextWrapper = ContextWrapper(localizedContext())

/**
 * 使用指定语言包装 Context。
 *
 * 该方法不会修改当前应用语言缓存，只用于一次性按指定 [locale] 读取资源。
 */
fun Context.wrap(locale: Locale): ContextWrapper {
    return ContextWrapper(createLocalizedContext(LocaleListCompat.create(locale)))
}

/**
 * 创建一个按当前应用语言读取资源的 Context。
 *
 * 适合 Application Context、Service、Worker、BroadcastReceiver、通知文案、Toast
 * 等不一定走 AppCompatActivity Context 的场景。
 *
 * 性能说明：
 *
 * - 如果当前 Context 的资源配置已经匹配应用语言，直接返回当前 Context。
 * - 如果不匹配，会按当前语言 tag 和关键 Configuration 缓存一个基于 applicationContext 创建的 localized Context。
 * - 因此 `getLocalizedXxx(...)` 连续调用不会每次都重新创建 Configuration Context。
 */
fun Context.localizedContext(): Context {
    val languages = appLanguages
    if (languages.isEmpty || resources.configuration.matchesLanguages(languages)) {
        return this
    }

    val languageTags = languages.toLanguageTags()
    val applicationContext = applicationContext
    val cacheKey = buildLocalizedContextCacheKey(
        languageTags = languageTags,
        configuration = applicationContext.resources.configuration
    )

    synchronized(localizedContextCacheLock) {
        cachedLocalizedContext?.let { cachedContext ->
            if (cachedLocalizedContextKey == cacheKey) return cachedContext
        }

        return applicationContext.createLocalizedContext(languages).also {
            cachedLocalizedContextKey = cacheKey
            cachedLocalizedContext = it
        }
    }
}

internal fun clearLocalizedContextCache() {
    synchronized(localizedContextCacheLock) {
        cachedLocalizedContext = null
        cachedLocalizedContextKey = null
    }
}

internal fun Context.createLocalizedContext(locales: LocaleListCompat): Context {
    if (locales.isEmpty) {
        return this
    }

    val configuration = Configuration(resources.configuration)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        configuration.setLocales(locales.unwrap() as android.os.LocaleList)
    } else {
        @Suppress("DEPRECATION")
        configuration.setLocale(locales[0])
    }
    return createConfigurationContext(configuration)
}

private fun Configuration.matchesLanguages(locales: LocaleListCompat): Boolean {
    if (locales.isEmpty) {
        return true
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val currentLocales = this.locales
        if (currentLocales.size() != locales.size()) {
            return false
        }
        for (index in 0 until locales.size()) {
            if (currentLocales[index] != locales[index]) {
                return false
            }
        }
        return true
    }

    @Suppress("DEPRECATION")
    return locale == locales[0]
}

private fun buildLocalizedContextCacheKey(languageTags: String, configuration: Configuration): String {
    return buildString {
        append(languageTags)
        append('|')
        append(configuration.uiMode)
        append('|')
        append(configuration.orientation)
        append('|')
        append(configuration.screenLayout)
        append('|')
        append(configuration.fontScale)
        append('|')
        append(configuration.densityDpi)
        append('|')
        append(configuration.smallestScreenWidthDp)
        append('|')
        append(configuration.screenWidthDp)
        append('|')
        append(configuration.screenHeightDp)
    }
}
