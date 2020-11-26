package com.xxf.arch.http.cache.transformer;

import androidx.annotation.NonNull;

import com.xxf.arch.http.cache.RxHttpCache;

import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description: 同步缓存
 * 读取上次的缓存, 上次没有缓存就返回网络的数据, 然后同步缓存;
 * 上次有缓存,也会同步网络数据 但不会onNext
 * <p>
 * 一次onNext
 * @Author: XGod
 * @CreateDate: 2020/11/24 10:53
 */
public class LastCacheTransformer<R> extends AbsCacheTransformer<R> {


    public LastCacheTransformer(@NonNull Call<R> call, RxHttpCache rxHttpCache) {
        super(call, rxHttpCache);
    }

    @Override
    public final ObservableSource<Response<R>> apply(final Observable<Response<R>> remoteObservable) {
        /**
         * .concatDelayError
         * 第一次执行中断 不会影响 第二次执行 但是下游报错后无法处理了,且上游无法感知下游报错
         *
         */

        return Observable.concatDelayError(
                Arrays.asList(
                        getCacheOrEmpty()
                                .switchIfEmpty(cacheAfter(remoteObservable))
                                .observeOn(Schedulers.io())
                        ,
                        Observable.create(new ObservableOnSubscribe<Response<R>>() {
                            @Override
                            public void subscribe(ObservableEmitter<Response<R>> emitter) throws Exception {
                                try {
                                    Response<R> rResponse = cacheAfter(remoteObservable).blockingFirst();
                                    emitter.onComplete();
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                    emitter.onError(e);
                                }
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                )
        );
    }
}
