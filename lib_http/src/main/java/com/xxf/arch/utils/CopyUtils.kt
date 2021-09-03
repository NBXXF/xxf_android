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
    return CopyUtils.copy(this)
}

/**
 * 通过json方式简单粗暴
 */
object CopyUtils {
    inline fun <reified T> copy(t: T): T {
        val toJsonString = JsonUtils.toJsonString(t);
        return JsonUtils.toBean(toJsonString, T::class.java)
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
}