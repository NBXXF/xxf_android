package com.xxf.arch.http.adapter.rxjava2;

import com.xxf.arch.http.cache.HttpCacheDirectoryProvider;
import com.xxf.arch.http.cache.RxHttpCache;
import com.xxf.arch.http.cache.RxHttpCacheFactory;

import java.lang.reflect.Type;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.Nullable;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.CacheType;
import retrofit2.Call;
import retrofit2.OkHttpRxJavaCallAdapter;
import retrofit2.Response;

final class RxJava2CallAdapter<R> extends OkHttpRxJavaCallAdapter<R, Object> {
    private final Type responseType;
    private final @Nullable
    Scheduler scheduler;
    private final boolean isAsync;
    private HttpCacheDirectoryProvider rxHttpCache;
    private CacheType rxCacheType;
    private final boolean isResult;
    private final boolean isBody;
    private final boolean isFlowable;
    private final boolean isSingle;
    private final boolean isMaybe;
    private final boolean isCompletable;
    RxJavaCallAdapterInterceptor rxJavaCallAdapterInterceptor;

    RxJava2CallAdapter(Type responseType, @Nullable Scheduler scheduler, boolean isAsync,
                       HttpCacheDirectoryProvider rxHttpCache,
                       CacheType rxCacheType,
                       RxJavaCallAdapterInterceptor rxJavaCallAdapterInterceptor,
                       boolean isResult, boolean isBody, boolean isFlowable, boolean isSingle, boolean isMaybe,
                       boolean isCompletable) {
        this.responseType = responseType;
        this.scheduler = scheduler;
        this.isAsync = isAsync;
        this.rxHttpCache = rxHttpCache;
        this.rxCacheType = rxCacheType;
        this.rxJavaCallAdapterInterceptor = rxJavaCallAdapterInterceptor;
        this.isResult = isResult;
        this.isBody = isBody;
        this.isFlowable = isFlowable;
        this.isSingle = isSingle;
        this.isMaybe = isMaybe;
        this.isCompletable = isCompletable;
    }

    @Override
    public Type responseType() {
        return responseType;
    }


    @Override
    public Object adapt(Call<R> call, @androidx.annotation.Nullable Object[] args) {
        if (rxJavaCallAdapterInterceptor != null) {
            return rxJavaCallAdapterInterceptor.adapt(call, args, defaultAdapt(call, args));
        } else {
            return defaultAdapt(call, args);
        }
    }

    public Object defaultAdapt(Call<R> call, @androidx.annotation.Nullable Object[] args) {
        Observable<Response<R>> responseObservable = isAsync
                ? new CallEnqueueObservable<>(call, RxHttpCacheFactory.getCache(this.rxHttpCache), this.rxCacheType)
                : new CallExecuteObservable<>(call, RxHttpCacheFactory.getCache(this.rxHttpCache), this.rxCacheType);
        //参数传递的cacheType
        if (args != null) {
            for (Object arg : args) {
                if (arg != null && arg instanceof CacheType) {
                    responseObservable = isAsync
                            ? new CallEnqueueObservable<>(call, RxHttpCacheFactory.getCache(this.rxHttpCache), (CacheType) arg)
                            : new CallExecuteObservable<>(call, RxHttpCacheFactory.getCache(this.rxHttpCache), (CacheType) arg);
                    break;
                }
            }
        }

        Observable<?> observable;
        if (isResult) {
            observable = new ResultObservable<>(responseObservable);
        } else if (isBody) {
            observable = new BodyObservable<>(responseObservable);
        } else {
            observable = responseObservable;
        }

        if (scheduler != null) {
            observable = observable.subscribeOn(scheduler);
        }

        if (isFlowable) {
            return observable.toFlowable(BackpressureStrategy.LATEST);
        }
        if (isSingle) {
            return observable.singleOrError();
        }
        if (isMaybe) {
            return observable.singleElement();
        }
        if (isCompletable) {
            return observable.ignoreElements();
        }
        return RxJavaPlugins.onAssembly(observable);
    }
}
