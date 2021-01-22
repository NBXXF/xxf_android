package com.xxf.arch.http.cache.transformer;

import androidx.annotation.NonNull;

import com.xxf.arch.http.cache.HttpCacheConfigProvider;

import java.util.Arrays;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description: 先从本地缓存拿取, 然后从服务器拿取, 可能会onNext两次, 如果本地没有缓存 最少执行oNext一次
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/24 10:53
 */
public class FirstCacheTransformer<R> extends AbsCacheTransformer<R> {


    public FirstCacheTransformer(@NonNull Call<R> call, HttpCacheConfigProvider rxHttpCacheConfig) {
        super(call, rxHttpCacheConfig);
    }

    @Override
    public final ObservableSource<Response<R>> apply(final Observable<Response<R>> remoteObservable) {
        /**
         * .concatDelayError
         * 第一次执行中断 不会影响 第二次执行 但是下游报错后无法处理了,且上游无法感知下游报错
         *  避免 onError 覆盖onNext 需要指定 .observeOn(AndroidSchedulers.mainThread()
         */

        return Observable.concatDelayError(
                Arrays.asList(
                        getCacheOrEmpty().observeOn(AndroidSchedulers.mainThread()),
                        cacheAfter(remoteObservable).observeOn(AndroidSchedulers.mainThread()))
        ).observeOn(Schedulers.io());
    }

}
