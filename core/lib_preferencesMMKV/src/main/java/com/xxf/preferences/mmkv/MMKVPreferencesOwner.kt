package com.xxf.preferences.mmkv

import android.content.SharedPreferences
import com.tencent.mmkv.MMKV
import com.xxf.application.application
import com.xxf.preferences.SharedPreferencesOwner
import com.xxf.preferences.SharedPreferencesOwner.Companion.SharedPreferencesFactory

/**
 * 默认共享的MMKV
 * 支持数据格式如下：
 * String
 * Int
 * Float
 * Long
 * Boolean
 * Set<String>
 * JSONObject
 * JSONArray
 */
interface MMKVPreferencesOwner : SharedPreferencesOwner {
    companion object {
        var sharedPreferencesFactory: SharedPreferencesFactory =
            object : SharedPreferencesFactory {
                override fun createSharedPreferences(): SharedPreferences {
                    if (MMKV.getRootDir() == null) {
                        MMKV.initialize(application)
                    }
                    return MMKV.mmkvWithID(MMKVPreferencesOwner::class.java.simpleName)
                }
            }

        private val mSharedPreferences: SharedPreferences by lazy {
            sharedPreferencesFactory.createSharedPreferences()
        }
    }

    override fun getSharedPreferences(): SharedPreferences {
        return mSharedPreferences
    }
}
