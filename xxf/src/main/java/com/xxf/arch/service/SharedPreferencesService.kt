package com.xxf.arch.service

import io.reactivex.rxjava3.core.Observable
import java.lang.reflect.Type

/**
 * @Description: java类作用描述
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/26 9:44
 */
interface SharedPreferencesService {
    fun getAll(): Map<String, *>?
    fun getString(key: String, defaultValue: String?): String?
    fun putString(key: String, value: String?)
    fun getStringSet(key: String, defaultValue: Set<String>?): Set<String>?
    fun putStringSet(key: String, value: Set<String>?)
    fun getInt(key: String, defaultValue: Int): Int
    fun putInt(key: String, value: Int?)
    fun getLong(key: String, defaultValue: Long): Long
    fun putLong(key: String, value: Long?)
    fun getFloat(key: String, defaultValue: Float): Float
    fun putFloat(key: String, value: Float?)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun putBoolean(key: String, value: Boolean?)

    /**
     * 以json 存储对象
     *
     * @param key
     * @param value
     */
    fun putObject(key: String, value: Any?)

    /**
     * 将存储的json 转换成对应的模型
     *
     * @param key
     * @param typeOfT
     * @param defaultValue
     * @param <T>
     * @return
    </T> */
    fun <T> getObject(key: String, typeOfT: Type, defaultValue: T?): T?

    operator fun contains(key: String): Boolean

    fun remove(key: String)

    /**
     * 返回对应的key
     *
     * @param key
     * @return
     */
    fun observeChange(key: String): Observable<String>

    /**
     * 返回对应的key
     *
     * @return
     */
    fun observeAllChange(): Observable<String>
}