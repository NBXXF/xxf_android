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
import retrofit2.Callback;
import retrofit2.OkHttpCallConvertor;
import retrofit2.Response;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 增加rxcache控制模式
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
final class CallEnqueueObservable<T> extends Observable<Response<T>> {
    private final Call<T> originalCall;
    private RxHttpCache rxHttpCache;
    private com.xxf.arch.annotation.RxHttpCache.CacheType rxCacheType;

    CallEnqueueObservable(Call<T> originalCall, RxHttpCache rxHttpCache, com.xxf.arch.annotation.RxHttpCache.CacheType rxCacheType) {
        this.originalCall = originalCall;
        this.rxHttpCache = rxHttpCache;
        this.rxCacheType = rxCacheType;
    }

    @Override
    protected void subscribeActual(Observer<? super Response<T>> observer) {
        // Since Call is a one-shot type, clone it for each new observer.
        Call<T> call = originalCall.clone();
        CallCallback<T> callback = new CallCallback<>(call, observer, this.rxHttpCache, this.rxCacheType == com.xxf.arch.annotation.RxHttpCache.CacheType.firstRemote);
        observer.onSubscribe(callback);
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
                    enqueueCall(call, callback);
                }
            }
            break;
            case firstRemote: {
                enqueueCall(call, callback);
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
                        enqueueCall(call, callback);
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
                enqueueCall(call, callback);
            }
            break;
            default: {
                enqueueCall(call, callback);
            }
            break;
        }
    }

    private void enqueueCall(Call<T> call, CallCallback<T> callback) {
        //执行网络请求
        if (!callback.isDisposed()) {
            call.enqueue(callback);
        }
    }


    private static final class CallCallback<T> implements Disposable, Callback<T> {
        private final Call<?> call;
        private final Observer<? super Response<T>> observer;
        private volatile boolean disposed;
        boolean terminated = false;
        RxHttpCache rxHttpCache;
        boolean readCache;

        CallCallback(Call<?> call, Observer<? super Response<T>> observer, RxHttpCache rxHttpCache, boolean readCache) {
            this.call = call;
            this.observer = observer;
            this.rxHttpCache = rxHttpCache;
            this.readCache = readCache;
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

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (disposed) return;
            cacheRxSafe(response);
            dispatchResponse(response);
        }

        /**
         * 分发
         *
         * @param response
         */
        private void dispatchResponse(Response<T> response) {
            try {
                observer.onNext(response);

                if (!disposed) {
                    terminated = true;
                    observer.onComplete();
                }
            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                if (terminated) {
                    RxJavaPlugins.onError(t);
                } else if (!disposed) {
                    try {
                        observer.onError(t);
                    } catch (Throwable inner) {
                        Exceptions.throwIfFatal(inner);
                        RxJavaPlugins.onError(new CompositeException(t, inner));
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            if (call.isCanceled()) return;
            //读取缓存
            if (t instanceof ConnectException && readCache) {
                Response<T> response = null;
                try {
                    response = (Response<T>) this.rxHttpCache.get(call.request(), new OkHttpCallConvertor<T>().apply(call));
                } catch (Throwable tx) {
                    tx.printStackTrace();
                }
                //有缓存使用缓存
                if (response != null) {
                    dispatchResponse(response);
                    return;
                }
            }
            try {
                observer.onError(t);
            } catch (Throwable inner) {
                Exceptions.throwIfFatal(inner);
                RxJavaPlugins.onError(new CompositeException(t, inner));
            }
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
