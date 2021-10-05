package com.xxf.arch.http.cache.transformer

import com.xxf.arch.http.cache.HttpCacheConfigProvider
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

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
        /**
         * .concatDelayError
         * 第一次执行中断 不会影响 第二次执行 但是下游报错后无法处理了,且上游无法感知下游报错
         */
        return Observable
            .concatEagerDelayError(
                listOf(
                    cacheOrEmpty,
                    cacheAfter(remoteObservable)
                        .onErrorResumeNext { throwable ->
                            if (throwable is UnknownHostException
                                || throwable is ConnectException
                                || throwable is SocketTimeoutException
                            ) {
                                //延迟一下错误 避免无网络来得太快
                                Observable.error<Response<R>>(throwable)
                                    .delaySubscription(500L, TimeUnit.MILLISECONDS)
                            } else {
                                Observable.error(throwable)
                            }
                        }
                )
            )
            //下游都需要observeOn(xxx,true)
            .observeOn(Schedulers.io(), true)
            .take(1)
    }
}