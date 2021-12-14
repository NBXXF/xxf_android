package com.xxf.application.activity

import android.app.Activity
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

/**
 * 处理参数获取 简化
 */
class ExtrasDelegate<out T>(private val extraName: String, private val defaultValue: T) {

    private var extra: T? = null

    operator fun getValue(thisRef: Activity, property: KProperty<*>): T {
        extra = getExtra(extra, extraName, thisRef)
        return extra ?: defaultValue
    }

    operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        extra = getExtra(extra, extraName, thisRef)
        return extra ?: defaultValue
    }
}

fun <T> bindExtra(extra: String, default: T) = ExtrasDelegate(extra, default)

fun bindExtra(extra: String) = bindExtra(extra, null)

@Suppress("UNCHECKED_CAST")
private fun <T> getExtra(oldExtra: T?, extraName: String, thisRef: Activity): T? =
    oldExtra ?: thisRef.intent?.extras?.get(extraName) as T?

@Suppress("UNCHECKED_CAST")
private fun <T> getExtra(oldExtra: T?, extraName: String, thisRef: Fragment): T? =
    oldExtra ?: thisRef.arguments?.get(extraName) as T?