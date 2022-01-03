package com.xxf.application.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * 处理参数获取 简化
 */
class ExtrasDelegate<out T>(private val key: String?, private val defaultValue: T) {

    private var extra: T? = null

    operator fun getValue(thisRef: Activity, property: KProperty<*>): T {
        extra = getExtra(extra, key ?: property.name, thisRef)
        return extra ?: defaultValue
    }

    operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        extra = getExtra(extra, key ?: property.name, thisRef)
        return extra ?: defaultValue
    }

    operator fun setValue(thisRef: Fragment, property: KProperty<*>, value: Any?) {
        this.extra = value as T
        if (thisRef.arguments == null) {
            try {
                thisRef.arguments = Bundle()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        thisRef.arguments?.putExtras((key ?: property.name) to value)
    }

    operator fun setValue(thisRef: Activity, property: KProperty<*>, value: Any?) {
        extra = value as T
        if (thisRef.intent != null) {
            thisRef.intent!!.putExtras((key ?: property.name) to value)
        }
    }
}

/**
 * 绑定获取参数
 */
fun <T> bindExtra(key: String? = null, defaultValue: T) = ExtrasDelegate(key, defaultValue)

/**
 * 绑定获取参数
 */
fun bindExtra(key: String? = null) = bindExtra(key, null)

/**
 * key value
 */
fun Fragment.putExtra(key: String, value: Any): Bundle {
    if (arguments == null) {
        arguments = Bundle()
    }
    return arguments!!.putExtras(key to value)
}

/**
 * putExtras(
 *          "Key1" to "Value",
 *          "Key2" to 123,
 *          "Key3" to false,
 *          "Key4" to arrayOf("4", "5", "6")
 *      )
 */
fun <T> Fragment.putExtras(vararg params: Pair<String, T>): Bundle {
    if (arguments == null) {
        arguments = Bundle()
    }
    return arguments!!.putExtras(*params)
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
fun <T> Intent.putExtras(vararg params: Pair<String, T>): Intent {
    if (params.isEmpty()) return this
    params.forEach { (key, value) ->
        if (value == null) {
            val nullString: String? = null
            putExtra(key, nullString)
        } else {
            when (value) {
                is Int -> putExtra(key, value)
                is Byte -> putExtra(key, value)
                is Char -> putExtra(key, value)
                is Long -> putExtra(key, value)
                is Float -> putExtra(key, value)
                is Short -> putExtra(key, value)
                is Double -> putExtra(key, value)
                is Boolean -> putExtra(key, value)
                is Bundle -> putExtra(key, value)
                is String -> putExtra(key, value)
                is IntArray -> putExtra(key, value)
                is ByteArray -> putExtra(key, value)
                is CharArray -> putExtra(key, value)
                is LongArray -> putExtra(key, value)
                is FloatArray -> putExtra(key, value)
                is Parcelable -> putExtra(key, value)
                is ShortArray -> putExtra(key, value)
                is DoubleArray -> putExtra(key, value)
                is BooleanArray -> putExtra(key, value)
                is CharSequence -> putExtra(key, value)
                is Serializable -> putExtra(key, value)
                is Array<*> -> {
                    when {
                        value.isArrayOf<String>() ->
                            putExtra(key, value as Array<String?>)
                        value.isArrayOf<Parcelable>() ->
                            putExtra(key, value as Array<Parcelable?>)
                        value.isArrayOf<CharSequence>() ->
                            putExtra(key, value as Array<CharSequence?>)
                        else -> putExtra(key, value)
                    }
                }
            }
        }
    }
    return this
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

@Suppress("UNCHECKED_CAST")
private fun <T> getExtra(oldExtra: T?, extraName: String, thisRef: Activity): T? =
    oldExtra ?: thisRef.intent?.extras?.get(extraName) as T?

@Suppress("UNCHECKED_CAST")
private fun <T> getExtra(oldExtra: T?, extraName: String, thisRef: Fragment): T? =
    oldExtra ?: thisRef.arguments?.get(extraName) as T?