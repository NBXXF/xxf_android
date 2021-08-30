package com.xxf.rxjava

import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/8/30
 * Description :kotlin 简化
 */
/********** Observable **********/
inline fun <reified T> Observable<T>.subscribeOnIO(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
}

inline fun <reified T> Observable<T>.observeOnIO(): Observable<T> {
    return this.observeOn(Schedulers.io())
}

/********** Flowable **********/
inline fun <reified T> Flowable<T>.subscribeOnIO(): Flowable<T> {
    return this.subscribeOn(Schedulers.io())
}

inline fun <reified T> Flowable<T>.observeOnIO(): Flowable<T> {
    return this.observeOn(Schedulers.io())
}


/********** Maybe **********/
inline fun <reified T> Maybe<T>.subscribeOnIO(): Maybe<T> {
    return this.subscribeOn(Schedulers.io())
}

inline fun <reified T> Maybe<T>.observeOnIO(): Maybe<T> {
    return this.observeOn(Schedulers.io())
}


/********** Completable **********/
inline fun <reified T> Completable.subscribeOnIO(): Completable {
    return this.subscribeOn(Schedulers.io())
}

inline fun <reified T> Completable.observeOnIO(): Completable {
    return this.observeOn(Schedulers.io())
}


/********** Single **********/
inline fun <reified T> Single<T>.subscribeOnIO(): Single<T> {
    return this.subscribeOn(Schedulers.io())
}

inline fun <reified T> Single<T>.observeOnIO(): Single<T> {
    return this.observeOn(Schedulers.io())
}
