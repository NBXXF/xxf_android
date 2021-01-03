package com.xxf.arch.http.adapter.rxjava2;

import androidx.annotation.Nullable;

import com.xxf.arch.http.cache.HttpCacheDirectoryProvider;
import com.xxf.arch.http.cache.RxHttpCacheFactory;
import com.xxf.arch.http.cache.transformer.FirstCacheTransformer;
import com.xxf.arch.http.cache.transformer.FirstRemoteTransformer;
import com.xxf.arch.http.cache.transformer.IfCacheTransformer;
import com.xxf.arch.http.cache.transformer.LastCacheTransformer;
import com.xxf.arch.http.cache.transformer.OnlyCacheTransformer;
import com.xxf.arch.http.cache.transformer.OnlyRemoteTransformer;

import java.lang.reflect.Type;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
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
        //参数传递的cacheType
        if (args != null) {
            for (Object arg : args) {
                if (arg != null && arg instanceof CacheType) {
                    this.rxCacheType = (CacheType) arg;
                    break;
                }
            }
        }
        if (this.rxCacheType == null) {
            this.rxCacheType = CacheType.onlyRemote;
        }
        Observable<Response<R>> responseObservable = null;
        ObservableTransformer<Response<R>, Response<R>> cacheTransformer;
        switch (this.rxCacheType) {
            case firstCache:
                responseObservable = isAsync ? new CallEnqueueObservable<>(call, true) : new CallExecuteObservable<>(call);
                cacheTransformer = new FirstCacheTransformer<>(call, RxHttpCacheFactory.getCache(this.rxHttpCache));
                break;
            case firstRemote:
                responseObservable = isAsync ? new CallEnqueueObservable<>(call, false) : new CallExecuteObservable<>(call);
                cacheTransformer = new FirstRemoteTransformer<>(call, RxHttpCacheFactory.getCache(this.rxHttpCache));
                break;
            case ifCache:
                responseObservable = isAsync ? new CallEnqueueObservable<>(call, false) : new CallExecuteObservable<>(call);
                cacheTransformer = new IfCacheTransformer<>(call, RxHttpCacheFactory.getCache(this.rxHttpCache));
                break;
            case onlyCache:
                responseObservable = isAsync ? new CallEnqueueObservable<>(call, false) : new CallExecuteObservable<>(call);
                cacheTransformer = new OnlyCacheTransformer<>(call, RxHttpCacheFactory.getCache(this.rxHttpCache));
                break;
            case lastCache:
                responseObservable = isAsync ? new CallEnqueueObservable<>(call, true) : new CallExecuteObservable<>(call);
                cacheTransformer = new LastCacheTransformer<>(call, RxHttpCacheFactory.getCache(this.rxHttpCache));
                break;
            case onlyRemote:
                responseObservable = isAsync ? new CallEnqueueObservable<>(call, false) : new CallExecuteObservable<>(call);
                cacheTransformer = new OnlyRemoteTransformer<>(call, RxHttpCacheFactory.getCache(this.rxHttpCache));
                break;
            default:
                responseObservable = isAsync ? new CallEnqueueObservable<>(call, false) : new CallExecuteObservable<>(call);
                cacheTransformer = new OnlyRemoteTransformer<>(call, RxHttpCacheFactory.getCache(this.rxHttpCache));
                break;
        }
        responseObservable = responseObservable.compose(cacheTransformer);

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
