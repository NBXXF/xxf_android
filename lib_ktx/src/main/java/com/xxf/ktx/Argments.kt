package com.xxf.ktx

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


@Deprecated("废弃了", replaceWith = ReplaceWith("argumentBinding"))
fun <T> Activity.bindExtra(key: String? = null, defaultValue: T) =
    argumentBinding(key, defaultValue)

@Deprecated("废弃了", replaceWith = ReplaceWith("argumentBinding"))
fun Activity.bindExtra(key: String? = null) = argumentBinding(key, null)

@Deprecated("废弃了", replaceWith = ReplaceWith("argumentBinding"))
fun <T> Fragment.bindExtra(key: String? = null, defaultValue: T) =
    argumentBinding(key, defaultValue)

@Deprecated("废弃了", replaceWith = ReplaceWith("argumentBinding"))
fun Fragment.bindExtra(key: String? = null) = argumentBinding(key, null)


/**
 * 绑定参数
 */
fun Activity.argumentBinding(key: String? = null) = ActivityIntentDelegate(key, null)

fun <T> Activity.argumentBinding(key: String? = null, defaultValue: T) =
    ActivityIntentDelegate(key, defaultValue)

/**
 * 绑定参数
 */
fun Fragment.argumentBinding(key: String? = null) = FragmentArgumentsDelegate(key, null)

fun <T> Fragment.argumentBinding(key: String? = null, defaultValue: T) =
    FragmentArgumentsDelegate(key, defaultValue)


open class ActivityIntentDelegate<V>(val key: String?, val defaultValue: V) :
    ReadWriteProperty<Activity, V> {
    open val proxy: BundleDelegate<V> = BundleDelegate<V>(key, defaultValue)
    override fun getValue(thisRef: Activity, property: KProperty<*>): V {
        return thisRef.intent.extras?.run {
            proxy.getValue(this, property)
        } ?: defaultValue
    }

    override fun setValue(thisRef: Activity, property: KProperty<*>, value: V) {
        thisRef.intent?.makeSureNoNullExtras()
        thisRef.intent.extras?.let {
            proxy.setValue(it, property, value);
        }
    }
}

open class FragmentArgumentsDelegate<V>(val key: String?, val defaultValue: V) :
    ReadWriteProperty<Fragment, V> {
    open val proxy: BundleDelegate<V> = BundleDelegate<V>(key, defaultValue)
    override fun getValue(thisRef: Fragment, property: KProperty<*>): V {
        return thisRef.arguments?.run {
            proxy.getValue(this, property)
        } ?: defaultValue
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: V) {
        thisRef.makeSureNoNullArguments()
        thisRef.arguments?.let {
            proxy.setValue(it, property, value);
        }
    }
}


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
