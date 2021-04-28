package com.xxf.arch.http.cookie;

import androidx.annotation.NonNull;

import com.xxf.arch.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description cookie管理
 * @date createTime：2018/9/7
 */
public abstract class PersistentCookieJar implements CookieJar {

    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        List<String> list = new ArrayList<>();
        for (Cookie cookie : cookies) {
            list.add(cookie.toString());
        }
        String cookieStr = JsonUtils.toJsonString(list);
        saveCookie(url.host(), cookieStr);
    }

    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        List<Cookie> cookiesResults = new ArrayList<>();
        String cookieStr = loadCookie(url.host());
        if (cookieStr != null && cookieStr.length() > 0) {
            try {
                List<String> cookieList = JsonUtils.toBeanList(cookieStr, String.class);
                for (String aCookieStr : cookieList) {
                    //将字符串解析成 Cookie 对象
                    try {
                        Cookie cookie = Cookie.parse(url, aCookieStr);
                        if (cookie != null) {
                            cookiesResults.add(cookie);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        //此方法返回 null 会引发异常
        return cookiesResults;
    }

    public abstract String loadCookie(String host);

    public abstract void saveCookie(String host, String cookie);
}
