package com.xxf.arch.tracker;


import androidx.annotation.Nullable;

import com.xxf.arch.http.adapter.rxjava2.RxJavaCallAdapterInterceptor;
import com.xxf.arch.http.interceptor.HttpExceptionTrackerInterceptor;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.Request;
import retrofit2.Call;

/**
 * @Description: http RxJava错误采集
 * @Author: XGod
 * @CreateDate: 2018/7/1 15:47
 */
public class TrackerJavaCallAdapterInterceptor implements RxJavaCallAdapterInterceptor {

    @Override
    public Object adapt(Call call, @Nullable Object[] args, Object rxJavaObservable) {
        if (rxJavaObservable instanceof Observable<?>) {
            //noinspection unchecked
            Observable<Object> observable = (Observable<Object>) rxJavaObservable;
            return observable.doOnError(throwable -> {
                if (call != null) {
                    onHandleExceptionTracker(call.request().newBuilder().build(), throwable);
                }
            });
        }
        return rxJavaObservable;
    }

    void onHandleExceptionTracker(Request request, Throwable throwable) {
        new HttpExceptionTrackerInterceptor().onFeedHttpException(request, null, throwable, -1);
    }
}
