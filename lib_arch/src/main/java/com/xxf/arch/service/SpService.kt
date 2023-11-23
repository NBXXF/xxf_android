package com.xxf.arch.service

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.text.TextUtils
import com.google.gson.JsonPrimitive
import com.google.gson.reflect.TypeToken
import com.xxf.application.applicationContext
import com.xxf.arch.XXF
import com.xxf.json.JsonUtils
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
    /**
     * 在key 上面区分user存储,业务不要用这个
     * [注意 这个不能随便改] 会影响业务
     * [注意 这个不能随便改]
     */
    private const val DIFFERUSER_PATH_SEGEMENT = "_D_I_F_F_E_R_U_S_E_R_"

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

    override fun isDifferUser(key: String): Boolean {
        return key.contains(DIFFERUSER_PATH_SEGEMENT)
    }

    override fun generateKey(key: String, differUser: Boolean): String {
        /**
         * 生成是否区分user的 真实key
         */
        return if (differUser) "${key}_${DIFFERUSER_PATH_SEGEMENT}_${XXF.getUserInfoProvider()?.userId.orEmpty()}" else key
    }

    override fun getString(key: String, defaultValue: String?, differUser: Boolean): String? {
        return mSharedPreferences.getString(
            generateKey(key, differUser),
            defaultValue
        )
    }

    override fun putString(key: String, value: String?, differUser: Boolean) {
        mSharedPreferences.edit().putString(
            generateKey(key, differUser),
            value
        ).commit()
    }

    override fun getStringSet(
        key: String,
        defaultValue: Set<String>?,
        differUser: Boolean
    ): Set<String>? {
        return mSharedPreferences.getStringSet(
            generateKey(key, differUser),
            defaultValue
        )
    }

    override fun putStringSet(key: String, value: Set<String>?, differUser: Boolean) {
        mSharedPreferences.edit().putStringSet(
            generateKey(key, differUser),
            value
        ).commit()
    }

    override fun getInt(key: String, defaultValue: Int, differUser: Boolean): Int {
        return mSharedPreferences.getInt(
            generateKey(key, differUser),
            defaultValue!!
        )
    }

    override fun putInt(key: String, value: Int?, differUser: Boolean) {
        mSharedPreferences.edit()
            .putInt(generateKey(key, differUser), value!!)
            .commit()
    }

    override fun getLong(key: String, defaultValue: Long, differUser: Boolean): Long {
        return mSharedPreferences.getLong(
            generateKey(key, differUser),
            defaultValue!!
        )
    }

    override fun putLong(key: String, value: Long?, differUser: Boolean) {
        mSharedPreferences.edit().putLong(
            generateKey(key, differUser),
            value!!
        ).commit()
    }

    override fun getFloat(key: String, defaultValue: Float, differUser: Boolean): Float {
        return mSharedPreferences.getFloat(
            generateKey(key, differUser),
            defaultValue!!
        )
    }

    override fun putFloat(key: String, value: Float?, differUser: Boolean) {
        mSharedPreferences.edit().putFloat(
            generateKey(key, differUser),
            value!!
        ).commit()
    }

    override fun getBoolean(key: String, defaultValue: Boolean, differUser: Boolean): Boolean {
        return mSharedPreferences.getBoolean(
            generateKey(key, differUser),
            defaultValue!!
        )
    }

    override fun putBoolean(key: String, value: Boolean?, differUser: Boolean) {
        mSharedPreferences.edit().putBoolean(
            generateKey(key, differUser),
            value!!
        ).commit()
    }

    override fun putObject(key: String, value: Any?, differUser: Boolean) {
        putString(
            generateKey(key, differUser),
            com.xxf.json.JsonUtils.toJsonString(value)
        )
    }

    override fun <T : Any?> getObject(
        key: String,
        typeOfT: Type,
        defaultValue: T?,
        differUser: Boolean
    ): T? {
        val string =
            getString(generateKey(key, differUser), null)
        try {
            return com.xxf.json.JsonUtils.toType(JsonPrimitive(string).asString, typeOfT) ?: defaultValue
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return defaultValue
    }


    override fun contains(key: String, differUser: Boolean): Boolean {
        return mSharedPreferences.contains(generateKey(key, differUser))
    }

    override fun remove(key: String, differUser: Boolean) {
        mSharedPreferences.edit()
            .remove(generateKey(key, differUser)).commit()
    }

    override fun observeChange(key: String, differUser: Boolean): Observable<String> {
        return bus.ofType(String::class.java)
            .filter {
                TextUtils.equals(
                    it,
                    generateKey(key, differUser)
                )
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
    private val differUser: Boolean,
    private val getter: (String, T,Boolean) -> T?,
    private val setter: (String, T?,Boolean) -> Unit
) {

    operator fun <F : SpServiceDelegate> getValue(thisRef: F, property: KProperty<*>): T {
        return getter(key ?: property.name, defaultValue,differUser) ?: defaultValue
    }


    operator fun <F : SpServiceDelegate> setValue(thisRef: F, property: KProperty<*>, value: Any?) {
        setter(key ?: property.name, value as T,differUser)
    }
}

fun SpServiceDelegate.bindString(
    key: String? = null,
    defaultValue: String,
    differUser: Boolean = false
) = KeyValueDelegate(
    key,
    defaultValue,
    differUser,
    getSharedPreferencesService()::getString,
    getSharedPreferencesService()::putString
)

fun SpServiceDelegate.bindString(
    key: String? = null,
    differUser: Boolean = false
) = KeyValueDelegate(
    key,
    null,
    differUser,
    getSharedPreferencesService()::getString,
    getSharedPreferencesService()::putString
)


fun SpServiceDelegate.bindStringSet(
    key: String? = null,
    differUser: Boolean = false
) = KeyValueDelegate(
    key,
    null,
    differUser,
    getSharedPreferencesService()::getStringSet,
    getSharedPreferencesService()::putStringSet
)

fun SpServiceDelegate.bindStringSet(
    key: String? = null,
    defaultValue: Set<String>,
    differUser: Boolean = false
) = KeyValueDelegate(
    key,
    defaultValue,
    differUser,
    getSharedPreferencesService()::getStringSet,
    getSharedPreferencesService()::putStringSet
)

fun SpServiceDelegate.bindInt(
    key: String? = null,
    defaultValue: Int = 0,
    differUser: Boolean = false
) = KeyValueDelegate<Int>(
    key,
    defaultValue,
    differUser,
    getSharedPreferencesService()::getInt,
    getSharedPreferencesService()::putInt,
)


fun SpServiceDelegate.bindLong(
    key: String? = null,
    defaultValue: Long = 0L,
    differUser: Boolean = false
) = KeyValueDelegate(
    key,
    defaultValue,
    differUser,
    getSharedPreferencesService()::getLong,
    getSharedPreferencesService()::putLong
)


fun SpServiceDelegate.bindFloat(
    key: String? = null,
    defaultValue: Float = 0.0F,
    differUser: Boolean = false
) = KeyValueDelegate(
    key,
    defaultValue,
    differUser,
    getSharedPreferencesService()::getFloat,
    getSharedPreferencesService()::putFloat
)

fun SpServiceDelegate.bindBoolean(
    key: String? = null,
    defaultValue: Boolean = false,
    differUser: Boolean = false
) = KeyValueDelegate(
    key,
    defaultValue,
    differUser,
    getSharedPreferencesService()::getBoolean,
    getSharedPreferencesService()::putBoolean
)

inline fun <reified T> SpServiceDelegate.bindObject(key: String? = null,
                                                    defaultValue:T?=null,
                                                    differUser: Boolean = false) =
    object : ReadWriteProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return getSharedPreferencesService().getObject(
                key ?: property.name,
                object : TypeToken<T>() {}.type,
                defaultValue,
                differUser
            )
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
            getSharedPreferencesService().putObject(key ?: property.name, value,differUser=differUser)
        }
    }

inline fun <reified T> SpServiceDelegate.bindObject(key: String? = null,
                                                    typeOfT: Type,
                                                    defaultValue:T?=null,
                                                    differUser: Boolean = false) =
    object : ReadWriteProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return getSharedPreferencesService().getObject(
                key ?: property.name,
                typeOfT,
                defaultValue,
                differUser
            )
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
            getSharedPreferencesService().putObject(key ?: property.name, value,differUser=differUser)
        }
    }