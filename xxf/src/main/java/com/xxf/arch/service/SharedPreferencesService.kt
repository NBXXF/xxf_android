package com.xxf.arch.service;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.util.Map;
import java.util.Set;

import io.reactivex.rxjava3.core.Observable;

/**
 * @Description: java类作用描述
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/26 9:44
 */
public interface SharedPreferencesService {
    Map<String, ?> getAll();

    @Nullable
    String getString(String key, @Nullable String defaultValue);

    void putString(String key, @Nullable String value);

    @Nullable
    Set<String> getStringSet(String key, @Nullable Set<String> defaultValue);

    void putStringSet(String key, @Nullable Set<String> value);

    int getInt(String key, int defaultValue);

    void putInt(String key, int value);

    long getLong(String key, long defaultValue);

    void putLong(String key, long value);

    float getFloat(String key, float defaultValue);

    void putFloat(String key, float value);

    boolean getBoolean(String key, boolean defaultValue);

    void putBoolean(String key, boolean value);

    boolean contains(String key);

    void remove(String key);

    /**
     * 返回对应的key
     *
     * @param key
     * @return
     */
    Observable<String> observeChange(String key);

    /**
     * 返回对应的key
     *
     * @return
     */
    Observable<String> observeAllChange();

}
