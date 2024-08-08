package com.xxf.arch.http.cookie

import com.xxf.ktx.CustomPreferencesOwner
import com.xxf.ktx.preferencesBinding
import org.json.JSONObject

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description SharedPreferences+Cookie持久化
 */
class SharedPreferencePersistentCookieJar : PersistentCookieJar() {
    companion object {
        val INSTANCE = SharedPreferencePersistentCookieJar()
    }

    object CookieJarSpServiceDelegate : CustomPreferencesOwner() {
        var cookie: JSONObject by preferencesBinding(
            key = "cookie",
            default = JSONObject()
        )
    }

    override fun onLoadCookieFromCache(host: String): String? {
        return CookieJarSpServiceDelegate.cookie.optString(host)
    }

    override fun onSaveCookieToCache(host: String, cookie: String?) {
        val cookieJson = CookieJarSpServiceDelegate.cookie
        cookieJson.put(host, cookie)
        CookieJarSpServiceDelegate.cookie = cookieJson
    }

}