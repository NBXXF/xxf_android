package com.xxf.arch.http.cache.transformer;

import androidx.annotation.NonNull;

import com.xxf.arch.http.cache.RxHttpCache;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description: 先从服务器获取, 没有网络 读取本地缓存
 * @Author: XGod
 * @CreateDate: 2020/11/24 10:53
 */
public class FirstRemoteTransformer<R> extends AbsCacheTransformer<R> {

    public FirstRemoteTransformer(@NonNull Call<R> call, RxHttpCache rxHttpCache) {
        super(call, rxHttpCache);
    }

    @Override
    public final ObservableSource<Response<R>> apply(Observable<Response<R>> remoteObservable) {
        /**
         * .concatDelayError
         * 第一次执行中断 不会影响 第二次执行
         */
        return cacheAfter(remoteObservable)
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Response<R>>>() {
                    @Override
                    public ObservableSource<? extends Response<R>> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof java.net.UnknownHostException
                                || throwable instanceof ConnectException
                                || throwable instanceof SocketTimeoutException) {
                            return getCacheOrEmpty()
                                    .switchIfEmpty(Observable.<Response<R>>error(throwable));
                        }
                        return Observable.error(throwable);
                    }
                });
    }
}
