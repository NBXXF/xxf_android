package com.xxf.bus

import io.reactivex.rxjava3.core.Observable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：8/7/21
 * Description :// rxbus事件扩展
 */

/**
 * 发送事件
 */
inline fun <reified T> T.postEvent(): Boolean {
    return RxBus.postEvent(this);
}

/**
 * 订阅事件
 * @param sticky 是否订阅粘性事件
 */
fun <T> Class<T>.subscribeEvent(sticky: Boolean = false): Observable<T> {
    return RxBus.subscribeEvent(this,sticky);
}
