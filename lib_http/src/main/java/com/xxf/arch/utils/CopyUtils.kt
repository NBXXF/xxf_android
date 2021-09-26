package com.xxf.arch.utils

import com.xxf.arch.json.JsonUtils

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/9/3
 * Description ://深拷贝
 */

/**
 * 深拷贝
 */
inline fun <reified T> T.copy(): T {
    return CopyUtils.copy(this, T::class.java)
}

/**
 * 深拷贝
 * 父类和子类对象 可以双向转换复制
 */
inline fun <reified T, R> T.copy(toClass: Class<R>): R {
    return CopyUtils.copy(this, toClass)
}

/**
 * 通过json方式简单粗暴
 */
object CopyUtils {
    inline fun <reified T> copy(t: T): T {
        return copy(t, T::class.java)
    }

    fun <T, R> copy(t: T, toClass: Class<R>): R {
        val toJsonString = JsonUtils.toJsonString(t);
        return JsonUtils.toBean(toJsonString, toClass)
    }

    inline fun <reified T> copySafe(t: T): T? {
        try {
            val toJsonString = JsonUtils.toJsonString(t);
            return JsonUtils.toBean(toJsonString, T::class.java)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    fun <T, R> copySafe(t: T, toClass: Class<R>): R? {
        try {
            val toJsonString = JsonUtils.toJsonString(t);
            return JsonUtils.toBean(toJsonString, toClass)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }
}