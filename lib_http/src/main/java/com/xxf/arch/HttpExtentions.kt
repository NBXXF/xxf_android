package com.xxf.arch

import com.xxf.arch.http.XXFHttp
import io.reactivex.rxjava3.core.Observable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/2
 * Description ://http kotlin 扩展
 */

/**
 * 获取apiservice
 */
inline fun <T> getApiService(apiClazz: Class<T>): T {
    return XXFHttp.getApiService(apiClazz)
}

/**
 * 清除指定class 的api service
 */
inline fun <T> clearApiService(apiClazz: Class<T>) {
    XXFHttp.clearApiService(apiClazz)
}

/**
 * 清除所有缓存的apisevice
 */
inline fun <T> clearAllApiService() {
    XXFHttp.clearAllApiService()
}

/**
 * 获取对应class 生成的api
 */
inline fun <T> Class<T>.apiService(): T {
    return XXFHttp.getApiService(this)
}


