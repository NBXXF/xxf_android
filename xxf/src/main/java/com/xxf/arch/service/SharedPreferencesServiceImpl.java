package com.xxf.arch.service;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.xxf.application.ApplicationProvider;
import com.xxf.arch.XXF;

import java.util.Map;
import java.util.Set;

/**
 * @Description: java类作用描述
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/26 9:50
 */
public class SharedPreferencesServiceImpl implements SharedPreferencesService {
    private static volatile SharedPreferencesService INSTANCE;

    public static SharedPreferencesService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SharedPreferencesServiceImpl(ApplicationProvider.applicationContext);
        }
        return INSTANCE;
    }

    private SharedPreferences mSharedPreferences;

    private SharedPreferencesServiceImpl(Context context) {
        this.mSharedPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), 0);
    }

    @Override
    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    @Nullable
    @Override
    public String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    @Override
    public void putString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).commit();
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return mSharedPreferences.getStringSet(key, defaultValue);
    }

    @Override
    public void putStringSet(String key, Set<String> value) {
        mSharedPreferences.edit().putStringSet(key, value).commit();
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    @Override
    public void putInt(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).commit();
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    @Override
    public void putLong(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).commit();
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    @Override
    public void putFloat(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).commit();
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).commit();
    }

    @Override
    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
