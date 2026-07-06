@file:JvmName("MultiLanguageKt")
@file:JvmMultifileClass
@file:Suppress("unused")

package com.xxf.android.i18n

import com.xxf.preferences.SharedPreferencesOwner
import com.xxf.preferences.preferencesBinding

private const val KEY_LANGUAGE_TAGS = "xxf_multi_language_tags"

/**
 * 多语言模块内部缓存。
 *
 * 通过 `lib_preferences` 的 [SharedPreferencesOwner] 落盘，业务侧不应直接访问。
 */
internal object MultiLanguagePreferences : SharedPreferencesOwner {
    var languageTags: String by preferencesBinding(KEY_LANGUAGE_TAGS, "")
}
