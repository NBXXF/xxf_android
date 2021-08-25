package com.xxf.arch.service

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.tencent.mmkv.MMKV
import com.xxf.application.applicationContext

/**
 * @Description:  key value 处理
 *  默认是懒加载初始化 如果想快可以 先调用一下
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/26 9:50
 */
object SpService : SharedPreferencesService {
    init {
        //初始化
        val rootDir = MMKV.initialize(applicationContext)
    }
    private val mSharedPreferences: SharedPreferences=MMKV.defaultMMKV();
    override fun getAll(): Map<String, *> {
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
        return mSharedPreferences.getInt(key, defaultValue)
    }

    override fun putInt(key: String, value: Int) {
        mSharedPreferences.edit().putInt(key, value).commit()
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return mSharedPreferences.getLong(key, defaultValue)
    }

    override fun putLong(key: String, value: Long) {
        mSharedPreferences.edit().putLong(key, value).commit()
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return mSharedPreferences.getFloat(key, defaultValue)
    }

    override fun putFloat(key: String, value: Float) {
        mSharedPreferences.edit().putFloat(key, value).commit()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        mSharedPreferences.edit().putBoolean(key, value).commit()
    }

    override fun contains(key: String): Boolean {
        return mSharedPreferences.contains(key)
    }

    override fun registerOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}