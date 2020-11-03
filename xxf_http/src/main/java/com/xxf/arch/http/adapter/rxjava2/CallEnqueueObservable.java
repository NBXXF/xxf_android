package com.xxf.arch.http.adapter.rxjava2;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.xxf.arch.http.cache.RxHttpCache;

import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.CacheType;
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
    private CacheType rxCacheType;
    boolean readCache;

    CallEnqueueObservable(Call<T> originalCall, RxHttpCache rxHttpCache, CacheType rxCacheType) {
        this.originalCall = originalCall;
        this.rxHttpCache = rxHttpCache;
        this.rxCacheType = rxCacheType;
        this.readCache = this.rxCacheType != CacheType.onlyRemote;
    }

    @Override
    protected void subscribeActual(Observer<? super Response<T>> observer) {
        // Since Call is a one-shot type, clone it for each new observer.
        Call<T> call = originalCall.clone();
        CallCallback<T> callback = new CallCallback<>(call, observer, this.rxHttpCache, this.readCache);
        observer.onSubscribe(callback);
        switch (this.rxCacheType) {
            case firstCache: {
                //先拿缓存 onNext一次,不论成功失败
                try {
                    Response<T> response = (Response<T>) this.rxHttpCache.get(call.request(), new OkHttpCallConvertor<T>().apply(call));
                    if (response != null) {
                        observer.onNext(response);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    enqueueCall(call, callback,true);
                }
            }
            break;
            case lastCache: {
                //先拿缓存 onNext一次
                Response<T> response = null;
                try {
                    response = (Response<T>) this.rxHttpCache.get(call.request(), new OkHttpCallConvertor<T>().apply(call));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                if (response != null) {
                    try {
                        observer.onNext(response);
                        callback.setDispatchNext(false);
                        enqueueCall(call, callback,true);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        try {
                            observer.onError(e);
                        } catch (Exception inner) {
                            Exceptions.throwIfFatal(inner);
                            RxJavaPlugins.onError(new CompositeException(e, inner));
                        }
                    }
                } else {
                    callback.setDispatchNext(true);
                    enqueueCall(call, callback,true);
                }
            }
            break;
            case firstRemote: {
                enqueueCall(call, callback,false);
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
                        enqueueCall(call, callback,true);
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
                enqueueCall(call, callback,false);
            }
            break;
            default: {
                enqueueCall(call, callback,false);
            }
            break;
        }
    }

    private void enqueueCall(Call<T> call, CallCallback<T> callback, boolean forceSyncCache) {
        //执行网络请求
        /**
         * 第一次onNext 出现错误了 就Disposed了
         */
        if (!callback.isDisposed()) {
            call.enqueue(callback);
        } else if (forceSyncCache) {
            /**
             * 强制同步缓存
             */
            call.clone().enqueue(new SyncCacheCallBack<T>(this.rxHttpCache));
        }
    }

    /**
     * 永远同步缓存
     *
     * @param <T>
     */
    private static final class SyncCacheCallBack<T> implements Disposable, Callback<T> {
        private volatile boolean disposed;
        RxHttpCache rxHttpCache;

        public SyncCacheCallBack(RxHttpCache rxHttpCache) {
            this.rxHttpCache = rxHttpCache;
        }

        @Override
        public void dispose() {
            disposed = true;
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            /**
             * 不管是否结束都需要保存缓存
             */
            try {
                rxHttpCache.put(response);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {

        }
    }

    private static final class CallCallback<T> implements Disposable, Callback<T> {
        private final Call<?> call;
        private final Observer<? super Response<T>> observer;
        private volatile boolean disposed;
        boolean terminated = false;
        RxHttpCache rxHttpCache;
        boolean readCache;
        /**
         * 是否向下分发
         */
        boolean dispatchNext = true;

        public void setDispatchNext(boolean dispatchNext) {
            this.dispatchNext = dispatchNext;
        }

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
                if (dispatchNext) {
                    observer.onNext(response);
                }

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
            boolean noNet = (t instanceof ConnectException || t instanceof UnknownHostException);
            if (noNet && readCache) {
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
