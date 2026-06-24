package com.xxf.preferences.mmkv

import com.tencent.mmkv.MMKV
import com.xxf.application.application
import com.xxf.ktx.IPreferencesOwner
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * 默认共享的MMKV
 * 支持数据格式如下：
 * String
 * Int
 * Float
 * Long
 * Boolean
 * Set<String>
 * JSONObject
 * JSONArray
 */
interface MMKVPreferencesOwner : IPreferencesOwner {
    companion object {
        private val mMMKV: MMKV by lazy {
            if (MMKV.getRootDir() == null) {
                MMKV.initialize(application)
            }
            MMKV.mmkvWithID(MMKVPreferencesOwner::class.java.simpleName)
        }
    }

    fun getMMKV(): MMKV {
        return mMMKV
    }

    @Suppress("UNCHECKED_CAST")
    override fun getPreferencesValue(
        property: KProperty<*>,
        propertyType: KClass<*>,
        key: String?,
        default: Any?
    ): Any? {
        val rawKey = key ?: property.name
        val mmkv = getMMKV()
        return when (propertyType) {
            String::class -> mmkv.decodeString(rawKey, default as? String)
            Int::class -> mmkv.decodeInt(rawKey, (default as? Int) ?: 0)
            Float::class -> mmkv.decodeFloat(rawKey, (default as? Float) ?: 0.0f)
            Long::class -> mmkv.decodeLong(rawKey, (default as? Long) ?: 0L)
            Boolean::class -> mmkv.decodeBool(rawKey, (default as? Boolean) ?: false)
            Set::class -> mmkv.decodeStringSet(rawKey, (default as? Set<String>))
            JSONObject::class -> {
                val string = mmkv.decodeString(rawKey, null)
                if (!string.isNullOrEmpty()) {
                    try {
                        JSONObject(string)
                    } catch (_: Throwable) {
                        default
                    }
                } else {
                    default
                }
            }

            JSONArray::class -> {
                val string = mmkv.decodeString(rawKey, null)
                if (!string.isNullOrEmpty()) {
                    try {
                        JSONArray(string)
                    } catch (_: Throwable) {
                        default
                    }
                } else {
                    default
                }
            }

            else -> {
                throw IllegalAccessException("${key ?: property.name} property not support type $propertyType")
            }
        }
    }

    override fun setPreferencesValue(
        property: KProperty<*>,
        propertyType: KClass<*>,
        key: String?,
        value: Any?,
        default: Any?
    ) {
        val rawKey = key ?: property.name
        val mmkv = getMMKV()
        if (value == null) {
            mmkv.removeValueForKey(rawKey)
            return
        }

        when (value) {
            is String -> mmkv.encode(rawKey, value)
            is Int -> mmkv.encode(rawKey, value)
            is Float -> mmkv.encode(rawKey, value)
            is Long -> mmkv.encode(rawKey, value)
            is Boolean -> mmkv.encode(rawKey, value)
            is Set<*> -> {
                if (value.isEmpty()) {
                    mmkv.encode(rawKey, emptySet<String>())
                } else if (value.firstOrNull() is String) {
                    @Suppress("UNCHECKED_CAST")
                    mmkv.encode(rawKey, value as Set<String>)
                } else {
                    throw IllegalAccessException("${key ?: property.name} property not support type $propertyType")
                }
            }

            is JSONObject -> mmkv.encode(rawKey, value.toString())
            is JSONArray -> mmkv.encode(rawKey, value.toString())
            else -> {
                throw IllegalAccessException("${key ?: property.name} property not support type $propertyType")
            }
        }
    }
}

/**
 * 自定义的MMKV
 * 默认 是类名的简称
 * 支持数据格式如下：
 * String
 * Int
 * Float
 * Long
 * Boolean
 * Set<String>
 * JSONObject
 * JSONArray
 */
open class CustomMMKVPreferencesOwner : MMKVPreferencesOwner {
    private val mCustomMMKV: MMKV by lazy {
        if (MMKV.getRootDir() == null) {
            MMKV.initialize(application)
        }
        MMKV.mmkvWithID(this::class.java.simpleName)
    }

    override fun getMMKV(): MMKV {
        return mCustomMMKV
    }
}
