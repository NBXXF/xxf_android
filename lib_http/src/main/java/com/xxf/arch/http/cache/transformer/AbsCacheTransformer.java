package com.xxf.arch.http.cache.transformer;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.xxf.arch.http.cache.HttpCacheConfigProvider;
import com.xxf.arch.http.cache.RxHttpCacheFactory;
import com.xxf.arch.http.model.BaseHttpResult;

import java.util.concurrent.Callable;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.CacheType;
import retrofit2.Call;
import retrofit2.OkHttpCallConvertor;
import retrofit2.Response;

/**
 * @Description: 缓存变换
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2017/11/24 10:44
 */
public abstract class AbsCacheTransformer<R> implements ObservableTransformer<Response<R>, Response<R>> {
    private static final String KEY_HEADER_CACHE = "cache";
    final HttpCacheConfigProvider rxHttpCacheConfig;
    final Call<R> call;

    public AbsCacheTransformer(@NonNull Call<R> call, HttpCacheConfigProvider rxHttpCacheConfig) {
        this.call = call;
        this.rxHttpCacheConfig = rxHttpCacheConfig;
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
                        if (rxHttpCacheConfig.isCache(rResponse)) {
                            // Log.d("===============>","缓存成功");
                            RxHttpCacheFactory.getCache(rxHttpCacheConfig).putAsync(rResponse);
                        }
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
                        String cacheTime = call.request().header(KEY_HEADER_CACHE);
                        if (TextUtils.isEmpty(cacheTime)) {
                            cacheTime = String.valueOf(rxHttpCacheConfig.cacheTime());
                        }
                        Response<R> response = (Response<R>) RxHttpCacheFactory.getCache(rxHttpCacheConfig).get(call.request(), new OkHttpCallConvertor<R>().apply(call), Long.parseLong(cacheTime));
                        return response;
                    }
                })
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Response<R>>>() {
                    @Override
                    public ObservableSource<? extends Response<R>> apply(Throwable throwable) throws Throwable {
                        return Observable.<Response<R>>empty();
                    }
                });
    }

    /**
     * 同步缓存配置
     *
     * @param response
     * @param cacheType
     * @param isFromCache
     */
    protected void applyCacheConfig(Response<R> response, CacheType cacheType, boolean isFromCache) {
        if (response != null && response.body() instanceof BaseHttpResult) {
            ((BaseHttpResult) response.body()).attachCacheConfig(cacheType, isFromCache);
        }
    }
}
