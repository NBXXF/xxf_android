package com.xxf.preferences.gson

import com.xxf.preferences.IPreferencesOwner
import com.xxf.preferences.PrefsDelegate
import kotlin.reflect.KProperty
import com.google.gson.JsonNull
import com.nbxxf.kpower.json.Json

/**
 * 支持可序列化的类型
 *   用法
 *  var user: User by preferencesBinding("key3", User()).useGson()
 */
inline fun <P : IPreferencesOwner, reified V> PrefsDelegate<P, out V>.useGson(): PrefsDelegate<P, V> {
    val delegate = this
    return object : PrefsDelegate<P, V>(this.key, this.default, V::class) {
        override fun getValue(thisRef: P, property: KProperty<*>): V {
            val value =
                PrefsDelegate<P, String?>(this.key, "", String::class).getValue(thisRef, property)
            return if (value.isNullOrEmpty()) {
                default
            } else {
                Json.fromJson<V>(value) ?: default
            }
        }

        override fun setValue(thisRef: P, property: KProperty<*>, value: Any?) {
            if (value is JsonNull || value == null) {
                delegate.setValue(thisRef, property, null)
            } else {
                delegate.setValue(thisRef, property, Json.toJson(value))
            }
        }
    }
}