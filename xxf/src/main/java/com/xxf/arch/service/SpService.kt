package com.xxf.arch.service

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.text.TextUtils
import com.tencent.mmkv.MMKV
import com.xxf.application.applicationContext
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

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
        MMKV.defaultMMKV();
    }
    private val bus: Subject<Any> by lazy {
        PublishSubject.create<Any>().toSerialized();
    }

    init {
        //初始化
        val rootDir = MMKV.initialize(applicationContext)
        if (mSharedPreferences !is MMKV) {
            mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }
    }

    override fun getAll(): Map<String, *> {
        return mSharedPreferences.all
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return mSharedPreferences.getString(key, defaultValue)
    }

    override fun putString(key: String, value: String?) {
        mSharedPreferences.edit().putString(key, value).commit().apply {
            fixMMKVListen(key)
        }

    }

    override fun getStringSet(key: String, defaultValue: Set<String>?): Set<String>? {
        return mSharedPreferences.getStringSet(key, defaultValue)
    }

    override fun putStringSet(key: String, value: Set<String>?) {
        mSharedPreferences.edit().putStringSet(key, value).commit().apply {
            fixMMKVListen(key)
        }
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return mSharedPreferences.getInt(key, defaultValue)
    }

    override fun putInt(key: String, value: Int) {
        mSharedPreferences.edit().putInt(key, value).commit().apply {
            fixMMKVListen(key)
        }
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return mSharedPreferences.getLong(key, defaultValue)
    }

    override fun putLong(key: String, value: Long) {
        mSharedPreferences.edit().putLong(key, value).commit().apply {
            fixMMKVListen(key)
        }
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return mSharedPreferences.getFloat(key, defaultValue)
    }

    override fun putFloat(key: String, value: Float) {
        mSharedPreferences.edit().putFloat(key, value).commit().apply {
            fixMMKVListen(key)
        }
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        mSharedPreferences.edit().putBoolean(key, value).commit().apply {
            fixMMKVListen(key)
        }
    }

    override fun contains(key: String): Boolean {
        return mSharedPreferences.contains(key)
    }

    override fun remove(key: String) {
        mSharedPreferences.edit().remove(key).commit().apply {
            fixMMKVListen(key)
        }
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

    /**
     * NMKV 没有监听 主动发一下
     */
    private fun fixMMKVListen(key: String) {
        if (key != null && (mSharedPreferences is MMKV)) {
            bus.onNext(key);
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key != null) {
            bus.onNext(key);
        }
    }
}