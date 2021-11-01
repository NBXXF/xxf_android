package com.xxf.objectbox

import io.objectbox.BoxStore
import io.reactivex.rxjava3.core.Observable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/7/16 17:34
 * Description :
 */

/**
 * 观察整个数据库
 */
fun BoxStore.observable(): Observable<Class<Any>> {
    return RxBoxStore.observable(this)
}

/**
 * 观察某个模型 就是某张表
 */
fun <T> BoxStore.observable(tClass: Class<T>): Observable<Class<T>> {
    return RxBoxStore.observable(this, tClass)
}