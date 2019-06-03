package com.xxf.arch.http.adapter.rxjava2;

import android.os.Build;
import android.support.annotation.RequiresApi;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;

import com.xxf.arch.http.cache.RxCache;

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
    private RxCache rxCache;
    private com.xxf.arch.annotation.RxCache.CacheType rxCacheType;

    CallEnqueueObservable(Call<T> originalCall, RxCache rxCache, com.xxf.arch.annotation.RxCache.CacheType rxCacheType) {
        this.originalCall = originalCall;
        this.rxCache = rxCache;
        this.rxCacheType = rxCacheType;
    }

    @Override
    protected void subscribeActual(Observer<? super Response<T>> observer) {
        // Since Call is a one-shot type, clone it for each new observer.
        Call<T> call = originalCall.clone();
        CallCallback<T> callback = new CallCallback<>(call, observer, this.rxCache);
        observer.onSubscribe(callback);
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
                enqueueCall(call, callback);
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
        RxCache rxCache;

        CallCallback(Call<?> call, Observer<? super Response<T>> observer, RxCache rxCache) {
            this.call = call;
            this.observer = observer;
            this.rxCache = rxCache;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (disposed) return;
            try {
                rxCache.put(response);
            } catch (Throwable e) {
                e.printStackTrace();
            }
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
