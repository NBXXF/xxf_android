package com.xxf.arch.http.cookie

import com.google.gson.Gson
import com.xxf.json.Json
import com.xxf.json.Json.toJson
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description cookie管理
 * @date createTime：2018/9/7
 */
abstract class PersistentCookieJar : CookieJar {
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val list: MutableList<String> = ArrayList()
        for (cookie in cookies) {
            list.add(cookie.toString())
        }
        val cookieStr = toJson(list) { gson: Gson? -> gson!! }
        saveCookie(url.host, cookieStr)
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookiesResults: MutableList<Cookie> = ArrayList()
        val cookieStr = loadCookie(url.host)
        if (!cookieStr.isNullOrEmpty()) {
            try {
                val cookieList: List<String> = Json.fromJson<List<String>>(cookieStr)
                for (aCookieStr in cookieList) {
                    //将字符串解析成 Cookie 对象
                    try {
                        val cookie = Cookie.parse(url, aCookieStr)
                        if (cookie != null) {
                            cookiesResults.add(cookie)
                        }
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        //此方法返回 null 会引发异常
        return cookiesResults
    }

    abstract fun loadCookie(host: String?): String?
    abstract fun saveCookie(host: String?, cookie: String?)
}