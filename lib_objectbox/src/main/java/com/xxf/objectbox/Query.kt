package com.xxf.objectbox

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/7/16 17:34
 * Description :
 */
import io.objectbox.query.Query
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Shortcut for [`RxQuery.flowableOneByOne(query, strategy)`][RxQuery.flowableOneByOne].
 */
fun <T> Query<T>.flowableOneByOne(strategy: BackpressureStrategy = BackpressureStrategy.BUFFER): Flowable<T> {
    return RxQuery.flowableOneByOne(this, strategy)
}

/**
 * Shortcut for [`RxQuery.observable(query)`][RxQuery.observable].
 */
fun <T> Query<T>.observable(): Observable<List<T>> {
    return RxQuery.observable(this)
}

/**
 * Shortcut for [`RxQuery.observable(query)`][RxQuery.observable].
 */
fun <T> Query<T>.observableChange(): Observable<List<T>> {
    return RxQuery.observableChange(this)
}

/**
 * Shortcut for [`RxQuery.single(query)`][RxQuery.single].
 */
fun <T> Query<T>.single(): Single<List<T>> {
    return RxQuery.single(this)
}