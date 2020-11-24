package com.xxf.arch.http.cache.transformer;

import androidx.annotation.NonNull;

import com.xxf.arch.http.cache.RxHttpCache;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.OkHttpCallConvertor;
import retrofit2.Response;

/**
 * @Description: 缓存变换
 * @Author: XGod
 * @CreateDate: 2017/11/24 10:44
 */
public abstract class AbsCacheTransformer<R> implements ObservableTransformer<Response<R>, Response<R>> {
    final RxHttpCache rxHttpCache;
    final Call<R> call;

    public AbsCacheTransformer(@NonNull Call<R> call, RxHttpCache rxHttpCache) {
        this.call = call;
        this.rxHttpCache = rxHttpCache;
    }

    @Override
    public abstract ObservableSource<Response<R>> apply(Observable<Response<R>> remoteObservable);

    /**
     * 缓存网络数据
     *
     * @param remoteObservable
     * @return
     */
    @NonNull
    final Observable<Response<R>> cacheAfter(@NonNull Observable<Response<R>> remoteObservable) {
        return remoteObservable
                .doOnNext(new Consumer<Response<R>>() {
                    @Override
                    public void accept(Response<R> rResponse) throws Exception {
                        rxHttpCache.putAsync(rResponse);
                    }
                });
    }

    /**
     * 获取缓存 缓存获取不到 返回空 (Observable.<Response<R>>empty()
     *
     * @return
     */
    @NonNull
    final Observable<Response<R>> getCacheOrEmpty() {
        return Observable
                .fromCallable(new Callable<Response<R>>() {
                    @Override
                    public Response<R> call() throws Exception {
                        Response<R> response = (Response<R>) rxHttpCache.get(call.request(), new OkHttpCallConvertor<R>().apply(call));
                        return response;
                    }
                })
                .onErrorResumeNext(Observable.<Response<R>>empty());
    }
}
