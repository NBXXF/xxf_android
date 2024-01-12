package com.xxf.arch.service

import io.reactivex.rxjava3.core.Observable
import java.lang.reflect.Type

/**
 * @Description: java类作用描述
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/26 9:44
 */
@Deprecated("过时了", replaceWith = ReplaceWith("IPreferencesOwner 和 IPreferencesOwner.preferencesBinding 委托"))
interface SharedPreferencesService {
    fun getAll(): Map<String, *>?

    /**
     * 这个key 是否区分了user
     */
    fun isDifferUser(key: String):Boolean

    /**
     * 生成key
     */
    fun generateKey(key: String,differUser: Boolean = false):String

    /**
     * @param differUser 是否区分用户
     */
    fun getString(key: String, defaultValue: String?, differUser: Boolean = false): String?

    /**
     * @param differUser 是否区分用户
     */
    fun putString(key: String, value: String?, differUser: Boolean = false)

    /**
     * @param differUser 是否区分用户
     */
    fun getStringSet(
        key: String,
        defaultValue: Set<String>?,
        differUser: Boolean = false
    ): Set<String>?

    /**
     * @param differUser 是否区分用户
     */
    fun putStringSet(key: String, value: Set<String>?, differUser: Boolean = false)

    /**
     * @param differUser 是否区分用户
     */
    fun getInt(key: String, defaultValue: Int, differUser: Boolean = false): Int

    /**
     * @param differUser 是否区分用户
     */
    fun putInt(key: String, value: Int?, differUser: Boolean = false)

    /**
     * @param differUser 是否区分用户
     */
    fun getLong(key: String, defaultValue: Long, differUser: Boolean = false): Long

    /**
     * @param differUser 是否区分用户
     */
    fun putLong(key: String, value: Long?, differUser: Boolean = false)

    /**
     * @param differUser 是否区分用户
     */
    fun getFloat(key: String, defaultValue: Float, differUser: Boolean = false): Float

    /**
     * @param differUser 是否区分用户
     */
    fun putFloat(key: String, value: Float?, differUser: Boolean = false)

    /**
     * @param differUser 是否区分用户
     */
    fun getBoolean(key: String, defaultValue: Boolean, differUser: Boolean = false): Boolean

    /**
     * @param differUser 是否区分用户
     */
    fun putBoolean(key: String, value: Boolean?, differUser: Boolean = false)


    /**
     * @param differUser 是否区分用户
     */
    fun contains(key: String, differUser: Boolean = false): Boolean

    /**
     * @param differUser 是否区分用户
     */
    fun remove(key: String, differUser: Boolean = false)

    /**
     * 返回对应的key
     *
     * @param key
     * @param differUser 是否区分用户
     * @return
     */
    fun observeChange(key: String, differUser: Boolean = false): Observable<String>

    /**
     * 返回对应的key
     *
     * @return
     */
    fun observeAllChange(): Observable<String>
}