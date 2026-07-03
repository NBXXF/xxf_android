package com.xxf.ktx

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.xxf.ktx.standard.KeyValueDelegate
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
fun <T> Activity.argumentBinding(key: String? = null) = ActivityIntentDelegate<T?>(key, null)

fun <T> Activity.argumentBinding(key: String? = null, default: T) =
    ActivityIntentDelegate(key, default)

/**
 * 绑定参数
 */
fun <T> Fragment.argumentBinding(key: String? = null) = FragmentArgumentsDelegate<T?>(key, null)

fun <T> Fragment.argumentBinding(key: String? = null, default: T) =
    FragmentArgumentsDelegate(key, default)


open class ActivityIntentDelegate<V>(key: String?, default: V) :
    KeyValueDelegate<Activity, V>(key, default) {
    open val proxy: BundleDelegate<V> = BundleDelegate<V>(key, default)
    override fun getValue(thisRef: Activity, property: KProperty<*>): V {
        return thisRef.intent.extras?.run {
            proxy.getValue(this, property)
        } ?: default
    }

    override fun setValue(thisRef: Activity, property: KProperty<*>, value: V) {
        thisRef.intent?.makeSureNoNullExtras()
        thisRef.intent.extras?.let {
            proxy.setValue(it, property, value);
        }
    }
}

open class FragmentArgumentsDelegate<V>(key: String?, default: V) :
    KeyValueDelegate<Fragment, V>(key, default) {
    open val proxy: BundleDelegate<V> = BundleDelegate<V>(key, default)
    override fun getValue(thisRef: Fragment, property: KProperty<*>): V {
        return thisRef.arguments?.run {
            proxy.getValue(this, property)
        } ?: default
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: V) {
        thisRef.makeSureNoNullArguments()
        thisRef.arguments?.let {
            proxy.setValue(it, property, value);
        }
    }
}

