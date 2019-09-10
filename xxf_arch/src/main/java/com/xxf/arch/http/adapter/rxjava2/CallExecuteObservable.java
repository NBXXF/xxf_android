package com.xxf.arch.http.adapter.rxjava2;

import android.os.Build;
import android.support.annotation.RequiresApi;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;

import com.xxf.arch.http.cache.RxHttpCache;

import java.net.ConnectException;

import retrofit2.Call;
import retrofit2.OkHttpCallConvertor;
import retrofit2.Response;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 增加rxcache控制模式
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
final class CallExecuteObservable<T> extends Observable<Response<T>> {
    private final Call<T> originalCall;
    private RxHttpCache rxHttpCache;
    private com.xxf.arch.annotation.RxHttpCache.CacheType rxCacheType;

    CallExecuteObservable(Call<T> originalCall, RxHttpCache rxHttpCache, com.xxf.arch.annotation.RxHttpCache.CacheType rxCacheType) {
        this.originalCall = originalCall;
        this.rxHttpCache = rxHttpCache;
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
                    Response<T> response = (Response<T>) this.rxHttpCache.get(call.request(), new OkHttpCallConvertor<T>().apply(call));
                    if (response != null) {
                        observer.onNext(response);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    //执行网络请求
                    executeCall(observer, call, disposable, false);
                }
            }
            break;
            case firstRemote: {
                executeCall(observer, call, disposable, true);
            }
            break;
            case onlyCache: {
                try {
                    Response<T> response = (Response<T>) this.rxHttpCache.get(call.request(), new OkHttpCallConvertor<T>().apply(call));
                    observer.onNext(response);
                    observer.onComplete();
                } catch (Throwable e) {
                    e.printStackTrace();
                    try {
                        observer.onError(e);
                    } catch (Exception inner) {
                        Exceptions.throwIfFatal(inner);
                        RxJavaPlugins.onError(new CompositeException(e, inner));
                    }
                }
            }
            break;
            case ifCache: {
                try {
                    Response<T> response = (Response<T>) this.rxHttpCache.get(call.request(), new OkHttpCallConvertor<T>().apply(call));
                    if (response != null) {
                        observer.onNext(response);
                        observer.onComplete();
                    } else {
                        executeCall(observer, call, disposable, false);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    try {
                        observer.onError(e);
                    } catch (Exception inner) {
                        Exceptions.throwIfFatal(inner);
                        RxJavaPlugins.onError(new CompositeException(e, inner));
                    }
                }
            }
            break;
            case onlyRemote: {
                executeCall(observer, call, disposable, false);
            }
            break;
            default: {
                executeCall(observer, call, disposable, false);
            }
            break;
        }
    }

    /**
     * 缓存
     *
     * @param response
     */
    private void cacheRxSafe(Response<T> response) {
        try {
            rxHttpCache.put(response);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void executeCall(Observer<? super Response<T>> observer, Call<T> call, CallDisposable disposable, boolean readCache) {
        boolean terminated = false;
        try {
            Response<T> response = null;
            try {
                response = call.execute();
                cacheRxSafe(response);
            } catch (ConnectException e) {
                //链接错误,没网了,读取缓存
                if (readCache) {
                    try {
                        response = (Response<T>) this.rxHttpCache.get(call.request(), new OkHttpCallConvertor<T>().apply(call));
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                    //有缓存
                    if (response != null && !disposable.isDisposed()) {
                        observer.onNext(response);
                        terminated = true;
                        observer.onComplete();
                        //不继续抛异常
                        return;
                    }
                }
                //抛出去
                throw e;
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
