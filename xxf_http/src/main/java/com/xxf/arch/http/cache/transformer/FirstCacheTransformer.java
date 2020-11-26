package com.xxf.arch.http.cache.transformer;

import androidx.annotation.NonNull;

import com.xxf.arch.http.cache.RxHttpCache;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description: 先从本地缓存拿取, 然后从服务器拿取, 可能会onNext两次, 如果本地没有缓存 最少执行oNext一次
 * @Author: XGod
 * @CreateDate: 2020/11/24 10:53
 */
public class FirstCacheTransformer<R> extends AbsCacheTransformer<R> {


    public FirstCacheTransformer(@NonNull Call<R> call, RxHttpCache rxHttpCache) {
        super(call, rxHttpCache);
    }

    @Override
    public final ObservableSource<Response<R>> apply(final Observable<Response<R>> remoteObservable) {
        /**
         * .concatDelayError
         * 第一次执行中断 不会影响 第二次执行 但是下游报错后无法处理了,且上游无法感知下游报错
         *
         */
        return Observable.create(new ObservableOnSubscribe<Response<R>>() {
            @Override
            public void subscribe(ObservableEmitter<Response<R>> emitter) throws Exception {
                try {
                    Response<R> rResponse = getCacheOrEmpty().blockingFirst();
                    if (rResponse != null) {
                        emitter.onNext(rResponse);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    try {
                        /**
                         * 一定要保证执行一遍 否则缓存数据报错 永远不能请求新数据了
                         */
                        Response<R> rResponse = cacheAfter(remoteObservable).blockingFirst();
                        emitter.onNext(rResponse);
                        emitter.onComplete();
                    } catch (Throwable e) {
                        e.printStackTrace();
                        emitter.onError(e);
                    }
                }
            }
        });
    }
}
