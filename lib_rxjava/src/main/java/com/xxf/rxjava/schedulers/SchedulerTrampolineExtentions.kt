package com.xxf.rxjava

import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/8/30
 * Description :kotlin 简化
 */
/********** Observable **********/
inline fun <reified T> Observable<T>.subscribeOnTrampoline(): Observable<T> {
    return this.subscribeOn(Schedulers.trampoline())
}

inline fun <reified T> Observable<T>.observeOnTrampoline(delayError: Boolean = false): Observable<T> {
    return this.observeOn(Schedulers.trampoline(), delayError)
}

/********** Flowable **********/
inline fun <reified T> Flowable<T>.subscribeOnTrampoline(): Flowable<T> {
    return this.subscribeOn(Schedulers.trampoline())
}

inline fun <reified T> Flowable<T>.observeOnTrampoline(delayError: Boolean = false): Flowable<T> {
    return this.observeOn(Schedulers.trampoline(), delayError)
}


/********** Maybe **********/
inline fun <reified T> Maybe<T>.subscribeOnTrampoline(): Maybe<T> {
    return this.subscribeOn(Schedulers.trampoline())
}

inline fun <reified T> Maybe<T>.observeOnTrampoline(): Maybe<T> {
    return this.observeOn(Schedulers.trampoline())
}


/********** Completable **********/
inline fun <reified T> Completable.subscribeOnTrampoline(): Completable {
    return this.subscribeOn(Schedulers.trampoline())
}

inline fun <reified T> Completable.observeOnTrampoline(): Completable {
    return this.observeOn(Schedulers.trampoline())
}


/********** Single **********/
inline fun <reified T> Single<T>.subscribeOnTrampoline(): Single<T> {
    return this.subscribeOn(Schedulers.trampoline())
}

inline fun <reified T> Single<T>.observeOnTrampoline(): Single<T> {
    return this.observeOn(Schedulers.trampoline())
}
