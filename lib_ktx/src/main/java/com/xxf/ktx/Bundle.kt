package com.xxf.ktx

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
operator fun <T> Bundle?.get(key: String): T? = this?.get(key) as? T

/**
 * bundle 委托
 */
open class BundleDelegate<V>(open val key: String?, open val defaultValue: V) :
    ReadWriteProperty<Bundle, V> {
    override fun getValue(thisRef: Bundle, property: KProperty<*>): V {
        return thisRef.get<V>(key ?: property.name) ?: defaultValue
    }

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: V) {
        thisRef.putExtras((key ?: property.name) to value)
    }
}


/**
 *  [Intent]的扩展方法，用来批量put键值对
 *  示例：
 *  <pre>
 *      intent.putExtras(
 *          "Key1" to "Value",
 *          "Key2" to 123,
 *          "Key3" to false,
 *          "Key4" to arrayOf("4", "5", "6")
 *      )
 * </pre>
 *
 * @param params 键值对
 */
fun <T> Bundle.putExtras(vararg params: Pair<String, T>): Bundle {
    if (params.isEmpty()) return this
    params.forEach { (key, value) ->
        if (value == null) {
            putString(key, value)
        } else {
            when (value) {
                is Int -> putInt(key, value)
                is Byte -> putByte(key, value)
                is Char -> putChar(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Short -> putShort(key, value)
                is Double -> putDouble(key, value)
                is Boolean -> putBoolean(key, value)
                is Bundle -> putBundle(key, value)
                is String -> putString(key, value)
                is IntArray -> putIntArray(key, value)
                is ByteArray -> putByteArray(key, value)
                is CharArray -> putCharArray(key, value)
                is LongArray -> putLongArray(key, value)
                is FloatArray -> putFloatArray(key, value)
                is Parcelable -> putParcelable(key, value)
                is ShortArray -> putShortArray(key, value)
                is DoubleArray -> putDoubleArray(key, value)
                is BooleanArray -> putBooleanArray(key, value)
                is CharSequence -> putCharSequence(key, value)
                is Serializable -> putSerializable(key, value)
                is Array<*> -> {
                    when {
                        value.isArrayOf<String>() ->
                            putStringArray(key, value as Array<String>)

                        value.isArrayOf<Parcelable>() ->
                            putParcelableArray(key, value as Array<Parcelable?>)

                        value.isArrayOf<CharSequence>() ->
                            putCharSequenceArray(key, value as Array<CharSequence?>)

                        value.isArrayOf<Parcelable>() -> {
                            putParcelableArray(key, value as Array<out Parcelable>)
                        }

                        else -> {
                            throw IllegalArgumentException("此类型不支持")
                        }
                    }
                }

                else -> throw IllegalArgumentException("此类型不支持")
            }
        }
    }
    return this
}

