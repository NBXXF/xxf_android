package com.xxf.arch

import com.xxf.arch.http.XXFHttp
import kotlin.reflect.KClass

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/2
 * Description ://http kotlin 扩展
 */

/**
 * 获取apiService
 */
inline fun <reified T> getApiService(): T {
    return XXFHttp.getApiService(T::class.java)
}

/**
 * 获取对应class 生成的api
 */
inline fun <reified T : Any> KClass<T>.apiService(): T {
    return XXFHttp.getApiService(T::class.java)
}

/**
 * 获取对应class 生成的api
 */
fun <T> Class<T>.apiService(): T {
    return XXFHttp.getApiService(this)
}


