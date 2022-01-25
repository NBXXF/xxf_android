package com.xxf.arch.service

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.text.TextUtils
import com.google.gson.JsonPrimitive
import com.google.gson.reflect.TypeToken
import com.xxf.application.applicationContext
import com.xxf.arch.XXF
import com.xxf.arch.json.JsonUtils
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @Description:  key value 处理
 *  默认是懒加载初始化 如果想快可以 先调用一下
 *  1. 支持rxjava 监听
 *  2. 底层逻辑是MMKV 加速
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/26 9:50
 */
object SpService : SharedPreferencesService, OnSharedPreferenceChangeListener {
    private val mSharedPreferences: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            XXF.getSharedPreferencesName(),
            Context.MODE_PRIVATE
        )
            .apply {
                this.registerOnSharedPreferenceChangeListener(this@SpService);
            }
    }

    private val bus: Subject<Any> by lazy {
        PublishSubject.create<Any>().toSerialized();
    }

    override fun getAll(): Map<String, *>? {
        return mSharedPreferences.all
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return mSharedPreferences.getString(key, defaultValue)
    }

    override fun putString(key: String, value: String?) {
        mSharedPreferences.edit().putString(key, value).commit()
    }

    override fun getStringSet(key: String, defaultValue: Set<String>?): Set<String>? {
        return mSharedPreferences.getStringSet(key, defaultValue)
    }

    override fun putStringSet(key: String, value: Set<String>?) {
        mSharedPreferences.edit().putStringSet(key, value).commit()
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return mSharedPreferences.getInt(key, defaultValue!!)
    }

    override fun putInt(key: String, value: Int?) {
        mSharedPreferences.edit().putInt(key, value!!).commit()
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return mSharedPreferences.getLong(key, defaultValue!!)
    }

    override fun putLong(key: String, value: Long?) {
        mSharedPreferences.edit().putLong(key, value!!).commit()
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return mSharedPreferences.getFloat(key, defaultValue!!)
    }

    override fun putFloat(key: String, value: Float?) {
        mSharedPreferences.edit().putFloat(key, value!!).commit()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defaultValue!!)
    }

    override fun putBoolean(key: String, value: Boolean?) {
        mSharedPreferences.edit().putBoolean(key, value!!).commit()
    }

    override fun putObject(key: String, value: Any?) {
        putString(key, JsonUtils.toJsonString(value))
    }

    override fun <T : Any?> getObject(key: String, typeOfT: Type, defaultValue: T?): T? {
        val string = getString(key, null)
        try {
            return JsonUtils.toType(JsonPrimitive(string).asString, typeOfT) ?: defaultValue
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return defaultValue
    }


    override fun contains(key: String): Boolean {
        return mSharedPreferences.contains(key)
    }

    override fun remove(key: String) {
        mSharedPreferences.edit().remove(key).commit()
    }

    override fun observeChange(key: String): Observable<String> {
        return bus.ofType(String::class.java)
            .filter {
                TextUtils.equals(it, key)
            }
    }

    override fun observeAllChange(): Observable<String> {
        return bus.ofType(String::class.java)
    }


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key != null) {
            bus.onNext(key);
        }
    }
}

/**
 * 不用担心主线程效率 底层用的mmkv,大数据存储 请转移到数据库
 */
open class SpServiceDelegate {
    fun getSharedPreferencesService(): SharedPreferencesService {
        return SpService
    }
}

class KeyValueDelegate<out T>(
    private val key: String?,
    private val defaultValue: T,
    private val getter: (String, T) -> T?,
    private val setter: (String, T?) -> Unit
) {

    operator fun <F : SpServiceDelegate> getValue(thisRef: F, property: KProperty<*>): T {
        return getter(key ?: property.name, defaultValue) ?: defaultValue
    }


    operator fun <F : SpServiceDelegate> setValue(thisRef: F, property: KProperty<*>, value: Any?) {
        setter(key ?: property.name, value as T)
    }
}

fun SpServiceDelegate.bindString(
    key: String? = null,
    defaultValue: String
) = KeyValueDelegate(
    key,
    defaultValue,
    getSharedPreferencesService()::getString,
    getSharedPreferencesService()::putString
)

fun SpServiceDelegate.bindString(
    key: String? = null
) = KeyValueDelegate(
    key,
    null,
    getSharedPreferencesService()::getString,
    getSharedPreferencesService()::putString
)


fun SpServiceDelegate.bindStringSet(
    key: String? = null
) = KeyValueDelegate(
    key,
    null,
    getSharedPreferencesService()::getStringSet,
    getSharedPreferencesService()::putStringSet
)

fun SpServiceDelegate.bindStringSet(
    key: String? = null,
    defaultValue: Set<String>
) = KeyValueDelegate(
    key,
    defaultValue,
    getSharedPreferencesService()::getStringSet,
    getSharedPreferencesService()::putStringSet
)

fun SpServiceDelegate.bindInt(
    key: String? = null,
    defaultValue: Int = 0
) = KeyValueDelegate<Int>(
    key,
    defaultValue,
    getSharedPreferencesService()::getInt,
    getSharedPreferencesService()::putInt,
)


fun SpServiceDelegate.bindLong(
    key: String? = null,
    defaultValue: Long = 0L
) = KeyValueDelegate(
    key,
    defaultValue,
    getSharedPreferencesService()::getLong,
    getSharedPreferencesService()::putLong
)


fun SpServiceDelegate.bindFloat(
    key: String? = null,
    defaultValue: Float = 0.0F
) = KeyValueDelegate(
    key,
    defaultValue,
    getSharedPreferencesService()::getFloat,
    getSharedPreferencesService()::putFloat
)

fun SpServiceDelegate.bindBoolean(
    key: String? = null,
    defaultValue: Boolean = false
) = KeyValueDelegate(
    key,
    defaultValue,
    getSharedPreferencesService()::getBoolean,
    getSharedPreferencesService()::putBoolean
)

inline fun <reified T> SpServiceDelegate.bindObject(key: String? = null) =
    object : ReadWriteProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return getSharedPreferencesService().getObject(
                key ?: property.name,
                object : TypeToken<T>() {}.type,
                null
            )
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
            getSharedPreferencesService().putObject(key ?: property.name, value)
        }
    }

inline fun <reified T> SpServiceDelegate.bindObject(key: String? = null, typeOfT: Type) =
    object : ReadWriteProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return getSharedPreferencesService().getObject(
                key ?: property.name,
                typeOfT,
                null
            )
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
            getSharedPreferencesService().putObject(key ?: property.name, value)
        }
    }