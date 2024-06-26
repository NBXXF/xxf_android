package com.xxf.rxjava

import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/8/30
 * Description :kotlin 简化
 */
/********** Observable **********/
inline fun <reified T : Any> Observable<T>.subscribeOnComputation(): Observable<T> {
    return this.subscribeOn(Schedulers.computation())
}

inline fun <reified T : Any> Observable<T>.observeOnComputation(delayError:Boolean=false): Observable<T> {
    return this.observeOn(Schedulers.computation(),delayError)
}

/********** Flowable **********/
inline fun <reified T : Any> Flowable<T>.subscribeOnComputation(): Flowable<T> {
    return this.subscribeOn(Schedulers.computation())
}

inline fun <reified T : Any> Flowable<T>.observeOnComputation(delayError:Boolean=false): Flowable<T> {
    return this.observeOn(Schedulers.computation(),delayError)
}


/********** Maybe **********/
inline fun <reified T> Maybe<T>.subscribeOnComputation(): Maybe<T> {
    return this.subscribeOn(Schedulers.computation())
}

inline fun <reified T> Maybe<T>.observeOnComputation(): Maybe<T> {
    return this.observeOn(Schedulers.computation())
}


/********** Completable **********/
inline fun <reified T> Completable.subscribeOnComputation(): Completable {
    return this.subscribeOn(Schedulers.computation())
}

inline fun <reified T> Completable.observeOnComputation(delayError:Boolean=false): Completable {
    return this.observeOn(Schedulers.computation())
}


/********** Single **********/
inline fun <reified T : Any> Single<T>.subscribeOnComputation(): Single<T> {
    return this.subscribeOn(Schedulers.computation())
}

inline fun <reified T : Any> Single<T>.observeOnComputation(): Single<T> {
    return this.observeOn(Schedulers.computation())
}
