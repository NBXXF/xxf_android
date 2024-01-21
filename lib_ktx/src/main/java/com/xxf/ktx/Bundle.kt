package com.xxf.ktx

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import com.xxf.ktx.standard.KeyValueDelegate
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
operator fun <T> Bundle?.get(key: String): T? = this?.get(key) as? T

/**
 * bundle 委托
 */
open class BundleDelegate<V>(override val key: String?, override val default: V) :
    KeyValueDelegate<Bundle, V>(key, default) {
    override fun getValue(thisRef: Bundle, property: KProperty<*>): V {
        return thisRef.get<V>(key ?: property.name) ?: default
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
    val bundleOf = bundleOf(*params)
    putAll(bundleOf)
    return this
}

