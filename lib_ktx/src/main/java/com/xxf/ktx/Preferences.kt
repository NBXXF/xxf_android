package com.xxf.ktx

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * 默认实现有 SharedPreferencesOwner
 * 十分方便扩展,可以扩展为MMKV,sqlite,file等等
 */
interface IPreferencesOwner {
    fun getPreferencesValue(
        property: KProperty<*>,
        propertyType: KClass<*>,
        key: String?,
        default: Any?
    ): Any?;

    fun setPreferencesValue(
        property: KProperty<*>,
        propertyType: KClass<*>,
        key: String?,
        value: Any?,
        default: Any?
    );
}

/**
 * 默认SharedPreferences
 */
interface SharedPreferencesOwner : IPreferencesOwner {
    companion object {
        private val mSharedPreferences: SharedPreferences by lazy {
            application.getSharedPreferences(
                SharedPreferencesOwner::class.java.simpleName,
                Context.MODE_PRIVATE
            )
        }
    }

    fun getSharedPreferences(): SharedPreferences {
        return mSharedPreferences;
    }


    @Suppress("UNCHECKED_CAST")
    override fun getPreferencesValue(
        property: KProperty<*>,
        propertyType: KClass<*>,
        key: String?,
        default: Any?
    ): Any? {
        val rawKey = key ?: property.name
        getSharedPreferences().run {
            when (propertyType) {
                String::class -> {
                    return getString(rawKey, default as? String)
                }

                Int::class -> {
                    return getInt(rawKey, (default as? Int) ?: 0)
                }

                Float::class -> {
                    return getFloat(rawKey, (default as? Float) ?: 0.0f)
                }

                Long::class -> {
                    return getLong(rawKey, (default as? Long) ?: 0L)
                }

                Boolean::class -> {
                    return getBoolean(rawKey, (default as? Boolean) ?: false)
                }

                Set::class -> {
                    return getStringSet(rawKey, (default as? Set<String>))
                }

                else -> {
                    throw IllegalAccessException("${key ?: property.name} property not support type $propertyType")
                }
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    @SuppressLint("ApplySharedPref")
    override fun setPreferencesValue(
        property: KProperty<*>,
        propertyType: KClass<*>,
        key: String?,
        value: Any?,
        default: Any?
    ) {
        val rawKey = key ?: property.name
        getSharedPreferences().edit().apply {
            if (value == null) {
                remove(rawKey)
            } else {
                //这里一定要用value::class
                when (value::class) {
                    String::class -> {
                        putString(rawKey, value as String?)
                    }

                    Int::class -> {
                        putInt(rawKey, value as Int)
                    }

                    Float::class -> {
                        putFloat(rawKey, value as Float)
                    }

                    Long::class -> {
                        putLong(rawKey, value as Long)
                    }

                    Boolean::class -> {
                        putBoolean(rawKey, value as Boolean)
                    }

                    Set::class -> {
                        (value as Set<*>).let {
                            if (it.isEmpty()) {
                                putStringSet(rawKey, setOf())
                            } else {
                                if (it.firstOrNull() is String) {
                                    putStringSet(rawKey, it as Set<String>)
                                } else {
                                    throw IllegalAccessException("${key ?: property.name} property not support type $propertyType")
                                }
                            }
                        }
                    }

                    else -> {
                        throw IllegalAccessException("${key ?: property.name} property not support type $propertyType")
                    }
                }
            }
        }.commit()
    }
}

open class PrefsDelegate<P : IPreferencesOwner, V>(
    open val key: String?,
    open val default: V,
    open val propertyType: KClass<*>
) :
    ReadOnlyProperty<P, V> {
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: P, property: KProperty<*>): V {
        return (thisRef.getPreferencesValue(property, propertyType, key, default) as? V) ?: default
    }

    open operator fun setValue(thisRef: P, property: KProperty<*>, value: Any?) {
        thisRef.setPreferencesValue(property, propertyType, key, value, default)
    }
}

/**
 *读写 key value 委托,内部具体用什么缓存 由IPreferencesOwner决定,十分方便扩展,可以扩展为MMKV,sqlite,file等等
 * 用法如下:
 * object PreferencesDemo : SharedPreferencesOwner {
 *     var name: String by preferencesBinding("key", "xxx")
 *     //可以监听
 *     var name2: String by preferencesBinding("key2", "xxx").observable { property, newValue ->
 *         println("=============>PrefsDemo3:$newValue")
 *     }
 * }
 *
 * 对于不支持的类型 可以自己重写IPreferencesOwner 或者继续包装代理,如下:
 * 拓展gson[gson 默认不继承在框架里面,业务只需要申明这个拓展就行了]
 *
 * inline fun <P : IPreferencesOwner, reified V> PrefsDelegate<P, out V>.useGson(): PrefsDelegate<P, V> {
 *     val delegate = this
 *     return object : PrefsDelegate<P, V>(this.key, this.default, V::class) {
 *
 *         override fun getValue(thisRef: P, property: KProperty<*>): V {
 *             val value =
 *                 PrefsDelegate<P, String?>(this.key, "", String::class).getValue(thisRef, property)
 *             return if (value.isNullOrEmpty()) {
 *                 default
 *             } else {
 *                 Json.fromJson<V>(value) ?: default
 *             }
 *         }
 *
 *         override fun setValue(thisRef: P, property: KProperty<*>, value: Any?) {
 *             if (value is JsonNull || value == null) {
 *                 delegate.setValue(thisRef, property, null)
 *             } else {
 *                 delegate.setValue(thisRef, property, Json.toJson(value))
 *             }
 *         }
 *     }
 * }
 *
 * 用法
 *  var user: User by preferencesBinding("key3", User()).useGson()
 */
inline fun <T : IPreferencesOwner, reified V> T.preferencesBinding(key: String?, default: V) =
    PrefsDelegate<T, V>(key, default, V::class)

/**
 * 用法介绍参考 preferencesBinding(key: String?, default: V)
 */
inline fun <T : IPreferencesOwner, reified V> T.preferencesBinding(key: String?) =
    PrefsDelegate<T, V?>(key, null, V::class)

/**
 * 异步写入
 */
inline fun <P : IPreferencesOwner, reified V> PrefsDelegate<P, V>.writeAsync(): PrefsDelegate<P, V> {
    val delegate = this
    return object : PrefsDelegate<P, V>(this.key, this.default, V::class) {

        override fun getValue(thisRef: P, property: KProperty<*>): V {
            return delegate.getValue(thisRef, property)
        }

        override fun setValue(thisRef: P, property: KProperty<*>, value: Any?) {
            GlobalScope.launch(Dispatchers.IO) {
                delegate.setValue(thisRef, property, value)
            }
        }
    }
}


/**
 * 观察值改变
 */
fun <P : IPreferencesOwner, V> PrefsDelegate<P, V>.observable(
    beforeChange: (property: KProperty<*>, newValue: V?) -> Unit = { _, _ -> },
    afterChange: (property: KProperty<*>, newValue: V?) -> Unit
): PrefsDelegate<P, V> {
    val delegate = this
    return object : PrefsDelegate<P, V>(delegate.key, delegate.default, delegate.propertyType) {
        override fun getValue(thisRef: P, property: KProperty<*>): V {
            return delegate.getValue(thisRef, property)
        }

        @Suppress("UNCHECKED_CAST")
        override fun setValue(thisRef: P, property: KProperty<*>, value: Any?) {
            beforeChange(property, value as? V)
            delegate.setValue(thisRef, property, value)
            afterChange(property, value as? V)
        }
    }
}