package com.xxf.rxjava

import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/8/30
 * Description :kotlin 简化
 */
/********** Observable **********/
inline fun <reified T> Observable<T>.subscribeOnSingle(): Observable<T> {
    return this.subscribeOn(Schedulers.single())
}

inline fun <reified T> Observable<T>.observeOnSingle(delayError:Boolean=false): Observable<T> {
    return this.observeOn(Schedulers.single(),delayError)
}

/********** Flowable **********/
inline fun <reified T> Flowable<T>.subscribeOnSingle(): Flowable<T> {
    return this.subscribeOn(Schedulers.single())
}

inline fun <reified T> Flowable<T>.observeOnSingle(delayError:Boolean=false): Flowable<T> {
    return this.observeOn(Schedulers.single(),delayError)
}


/********** Maybe **********/
inline fun <reified T> Maybe<T>.subscribeOnSingle(): Maybe<T> {
    return this.subscribeOn(Schedulers.single())
}

inline fun <reified T> Maybe<T>.observeOnSingle(): Maybe<T> {
    return this.observeOn(Schedulers.single())
}


/********** Completable **********/
inline fun <reified T> Completable.subscribeOnSingle(): Completable {
    return this.subscribeOn(Schedulers.single())
}

inline fun <reified T> Completable.observeOnSingle(): Completable {
    return this.observeOn(Schedulers.single())
}


/********** Single **********/
inline fun <reified T> Single<T>.subscribeOnSingle(): Single<T> {
    return this.subscribeOn(Schedulers.single())
}

inline fun <reified T> Single<T>.observeOnSingle(): Single<T> {
    return this.observeOn(Schedulers.single())
}
