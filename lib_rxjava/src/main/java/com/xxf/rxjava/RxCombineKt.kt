package com.xxf.rxjava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Function3

/**
 * 并行合并
 */
fun <T:Any, V:Any, R:Any> Observable<T>.combineLatestDelayError(
    with: ObservableSource<V>,
    combiner: BiFunction<T, V, R>
): Observable<R> {
    return Observable.combineLatestDelayError(listOf(this, with)) {
        it
    }.map {
        combiner.apply(it.first() as T, it.last() as V)
    }
}


/**
 * 并行合并
 */
fun <T : Any, T1 : Any, T2 : Any, R : Any> Observable<T>.combineLatestDelayError(
    with1: ObservableSource<T1>,
    with2: ObservableSource<T2>,
    combiner: Function3<T, T1, T2, R>
): Observable<R> {
    return Observable.combineLatestDelayError(listOf(this, with1, with2)) {
        it
    }.map {
        combiner.apply(it[0] as T, it[1] as T1, it[2] as T2)
    }
}
