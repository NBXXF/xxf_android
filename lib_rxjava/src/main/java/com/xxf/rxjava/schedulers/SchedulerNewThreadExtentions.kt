package com.xxf.rxjava

import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/8/30
 * Description :kotlin 简化
 */
/********** Observable **********/
inline fun <reified T> Observable<T>.subscribeOnNewThread(): Observable<T> {
    return this.subscribeOn(Schedulers.newThread())
}

inline fun <reified T> Observable<T>.observeOnNewThread(delayError:Boolean=false): Observable<T> {
    return this.observeOn(Schedulers.newThread(),delayError)
}

/********** Flowable **********/
inline fun <reified T> Flowable<T>.subscribeOnNewThread(delayError:Boolean=false): Flowable<T> {
    return this.subscribeOn(Schedulers.newThread(),delayError)
}

inline fun <reified T> Flowable<T>.observeOnNewThread(): Flowable<T> {
    return this.observeOn(Schedulers.newThread())
}


/********** Maybe **********/
inline fun <reified T> Maybe<T>.subscribeOnNewThread(): Maybe<T> {
    return this.subscribeOn(Schedulers.newThread())
}

inline fun <reified T> Maybe<T>.observeOnNewThread(): Maybe<T> {
    return this.observeOn(Schedulers.newThread())
}


/********** Completable **********/
inline fun <reified T> Completable.subscribeOnNewThread(): Completable {
    return this.subscribeOn(Schedulers.newThread())
}

inline fun <reified T> Completable.observeOnNewThread(): Completable {
    return this.observeOn(Schedulers.newThread())
}


/********** Single **********/
inline fun <reified T> Single<T>.subscribeOnNewThread(): Single<T> {
    return this.subscribeOn(Schedulers.newThread())
}

inline fun <reified T> Single<T>.observeOnNewThread(): Single<T> {
    return this.observeOn(Schedulers.newThread())
}
