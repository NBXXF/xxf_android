package com.xxf.arch.http.cookie

import com.xxf.ktx.CustomPreferencesOwner
import com.xxf.ktx.preferencesBinding

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description SharedPreferences+Cookie持久化
 */
class SharedPreferencePersistentCookieJar : PersistentCookieJar() {
    object CookieJarSpServiceDelegate : CustomPreferencesOwner() {
        var cookie: String by preferencesBinding(
            key = "cookie",
            default = ""
        )
    }

    override fun loadCookie(host: String): String? {
        return CookieJarSpServiceDelegate.cookie
    }

    override fun saveCookie(host: String, cookie: String?) {
        CookieJarSpServiceDelegate.cookie = cookie.orEmpty()
    }

}