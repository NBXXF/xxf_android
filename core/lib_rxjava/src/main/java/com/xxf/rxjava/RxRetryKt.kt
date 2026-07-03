package com.xxf.rxjava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Predicate
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * 每次重试 延迟
 * @param times 重试次数
 * @param delayTime 延迟重试的间隔
 */
fun <T : Any> Observable<T>.retryDelay(times: Long, delayTime: Long): Observable<T> {
    return this.observeOn(Schedulers.io())
        .retry(times) {
            try {
                Thread.sleep(delayTime)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            true
        }
}
/**
 * 每次重试 延迟
 * @param times 重试次数
 * @param delayTime 延迟重试的间隔
 */
fun <T:Any> Observable<T>.retryDelay(
    times: Long,
    delayTime: Long,
    predicate: Predicate<Throwable>
): Observable<T> {
    return this.observeOn(Schedulers.io())
        .retry(times) {
            try {
                Thread.sleep(delayTime)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            predicate.test(it)
        }
}