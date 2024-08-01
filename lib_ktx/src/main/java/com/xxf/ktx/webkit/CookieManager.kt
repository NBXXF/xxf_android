package com.xxf.ktx.webkit

import android.os.Build
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import com.xxf.ktx.tryOrLog
import java.net.HttpCookie

/**
 * 获取Cookie
 */
fun <T : CookieManager> T.getCookieList(url: String): List<HttpCookie> {
    return kotlin.runCatching {
        val cookie = this.getCookie(url)
        HttpCookie.parse(cookie)
    }.getOrNull().orEmpty()
}

/**
 * 获取Cookie
 */
fun <T : CookieManager> T.getCookieMap(url: String): Map<String, HttpCookie> {
    return getCookieList(url)
        .associateBy { it.name.orEmpty() }
}


/**
 * 设置/替换 多个cookie
 * @param value 格式 "site=android"
 */
fun <T : CookieManager> T.setCookies(url: String, value: List<HttpCookie>) {
    tryOrLog {
        value.forEach {
            setCookie(url, "$it")
        }
        this.sync()
    }
}


/**
 * 设置/替换 多个cookie
 * @param value
 */
fun <T : CookieManager> T.setCookies(url: String, value: Map<String, String>) {
    tryOrLog {
        value.forEach {
            setCookie(url, "${it.key}=${it.value}")
        }
        this.sync()
    }
}


/**
 * 确保当前通过 getCookie API 访问的所有 Cookie 都已写入持久性存储。此调用将阻止调用方，直到完成，并可能执行 I/ O。
 */
fun <T : CookieManager> T.sync() {
    tryOrLog {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
    }
}