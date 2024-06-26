package com.xxf.rxjava

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/8/30
 * Description :kotlin 简化
 */
/********** Observable **********/
inline fun <reified T : Any> Observable<T>.subscribeOnMain(): Observable<T> {
    return this.subscribeOn(AndroidSchedulers.mainThread())
}

inline fun <reified T : Any> Observable<T>.observeOnMain(delayError:Boolean=false): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread(),delayError)
}

/********** Flowable **********/
inline fun <reified T : Any> Flowable<T>.subscribeOnMain(): Flowable<T> {
    return this.subscribeOn(AndroidSchedulers.mainThread())
}

inline fun <reified T : Any> Flowable<T>.observeOnMain(delayError:Boolean=false): Flowable<T> {
    return this.observeOn(AndroidSchedulers.mainThread(),delayError)
}


/********** Maybe **********/
inline fun <reified T> Maybe<T>.subscribeOnMain(): Maybe<T> {
    return this.subscribeOn(AndroidSchedulers.mainThread())
}

inline fun <reified T> Maybe<T>.observeOnMain(): Maybe<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}


/********** Completable **********/
inline fun <reified T> Completable.subscribeOnMain(): Completable {
    return this.subscribeOn(AndroidSchedulers.mainThread())
}

inline fun <reified T> Completable.observeOnMain(): Completable {
    return this.observeOn(AndroidSchedulers.mainThread())
}


/********** Single **********/
inline fun <reified T : Any> Single<T>.subscribeOnMain(): Single<T> {
    return this.subscribeOn(AndroidSchedulers.mainThread())
}

inline fun <reified T : Any> Single<T>.observeOnMain(): Single<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}
