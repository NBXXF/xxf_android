package com.xxf.arch.http.cache.transformer

import com.xxf.arch.http.cache.HttpCacheConfigProvider
import com.xxf.arch.http.cache.transformer.AbsCacheTransformer
import com.xxf.rxjava.schedulers.concatMapEagerDelayError
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import retrofit2.Call
import retrofit2.Response

/**
 * @Description: 同步缓存
 * 读取上次的缓存, 上次没有缓存就返回网络的数据, 然后同步缓存;
 * 上次有缓存,也会同步网络数据 但不会onNext
 *
 *
 * 一次onNext
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/24 10:53
 */
class LastCacheTransformer<R>(call: Call<R>, rxHttpCacheConfig: HttpCacheConfigProvider?) :
    AbsCacheTransformer<R>(call, rxHttpCacheConfig) {
    override fun apply(remoteObservable: Observable<Response<R>>): ObservableSource<Response<R>> {
        return Observable
            .fromArray(
                cacheOrEmpty,
                cacheAfter(remoteObservable)
            )
            .concatMapEagerDelayError()
            .take(1)
    }
}