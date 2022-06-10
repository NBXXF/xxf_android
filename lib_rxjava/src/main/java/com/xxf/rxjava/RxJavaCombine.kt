package com.xxf.rxjava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.BiFunction

/**
 * 并行合并
 */
fun <T, V, R> Observable<T>.combineLatestDelayError(
    with: ObservableSource<V>,
    combiner: BiFunction<T, V, R>
): Observable<R> {
    return Observable.combineLatestDelayError(listOf(this, with)) {
        it
    }.map {
        combiner.apply(it.first() as T, it.last() as V)
    }
}

