package com.xxf.arch.http.adapter.rxjava2;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;

import com.xxf.arch.http.cache.RxCache;

import retrofit2.Call;
import retrofit2.OkHttpCallConvertor;
import retrofit2.Response;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 增加rxcache控制模式
 */
final class CallExecuteObservable<T> extends Observable<Response<T>> {
    private final Call<T> originalCall;
    private RxCache rxCache;
    private com.xxf.arch.annotation.RxCache.CacheType rxCacheType;

    CallExecuteObservable(Call<T> originalCall, RxCache rxCache, com.xxf.arch.annotation.RxCache.CacheType rxCacheType) {
        this.originalCall = originalCall;
        this.rxCache = rxCache;
        this.rxCacheType = rxCacheType;
    }

    @Override
    protected void subscribeActual(Observer<? super Response<T>> observer) {
        // Since Call is a one-shot type, clone it for each new observer.
        Call<T> call = originalCall.clone();
        CallDisposable disposable = new CallDisposable(call);
        observer.onSubscribe(disposable);
        if (disposable.isDisposed()) {
            return;
        }
        switch (this.rxCacheType) {
            case firstCache: {
                //先拿缓存 onNext一次
                try {
                    Response<T> response = (Response<T>) this.rxCache.get(call.request(), new OkHttpCallConvertor<T>().apply(call));
                    if (response != null) {
                        observer.onNext(response);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                //执行网络请求
                executeCall(observer, call, disposable);
            }
            break;
            case onlyCache: {
                try {
                    Response<T> response = (Response<T>) this.rxCache.get(call.request(), new OkHttpCallConvertor<T>().apply(call));
                    if (response != null) {
                        observer.onNext(response);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    observer.onError(e);
                }
            }
            break;
            case onlyRemote: {
                executeCall(observer, call, disposable);
            }
            break;
            default: {
                executeCall(observer, call, disposable);
            }
            break;
        }
    }

    private void executeCall(Observer<? super Response<T>> observer, Call<T> call, CallDisposable disposable) {
        boolean terminated = false;
        try {
            Response<T> response = call.execute();
            try {
                rxCache.put(response);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (!disposable.isDisposed()) {
                observer.onNext(response);
            }
            if (!disposable.isDisposed()) {
                terminated = true;
                observer.onComplete();
            }
        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            if (terminated) {
                RxJavaPlugins.onError(t);
            } else if (!disposable.isDisposed()) {
                try {
                    observer.onError(t);
                } catch (Throwable inner) {
                    Exceptions.throwIfFatal(inner);
                    RxJavaPlugins.onError(new CompositeException(t, inner));
                }
            }
        }
    }

    private static final class CallDisposable implements Disposable {
        private final Call<?> call;
        private volatile boolean disposed;

        CallDisposable(Call<?> call) {
            this.call = call;
        }

        @Override
        public void dispose() {
            disposed = true;
            call.cancel();
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }
    }
}
