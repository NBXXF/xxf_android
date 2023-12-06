package com.xxf.arch.tracker

import com.xxf.arch.exceptions.rawResponseRequest
import com.xxf.arch.http.adapter.rxjava2.RxJavaCallAdapterInterceptor
import com.xxf.arch.http.interceptor.HttpExceptionTrackerInterceptor
import io.reactivex.rxjava3.core.Observable
import okhttp3.Request
import retrofit2.Call
import retrofit2.HttpException

/**
 * @Description: http RxJava错误采集
 * @Author: XGod
 * @CreateDate: 2018/7/1 15:47
 */
open class RxJavaCallAdapterTrackerInterceptor : RxJavaCallAdapterInterceptor {
    override fun adapt(call: Call<*>?, args: Array<Any>?, rxJavaObservable: Any): Any {
        if (rxJavaObservable is Observable<*>) {
            val observable = rxJavaObservable as Observable<Any>
            return observable.doOnError { throwable: Throwable? ->
                if (call != null && throwable != null) {
                    if (throwable is HttpException && throwable.rawResponseRequest != null) {
                        onHandleExceptionTracker(throwable.rawResponseRequest!!, throwable)
                    } else {
                        onHandleExceptionTracker(call.request().newBuilder().build(), throwable)
                    }
                }
            }
        }
        return rxJavaObservable
    }

    protected open fun onHandleExceptionTracker(request: Request, throwable: Throwable) {
        HttpExceptionTrackerInterceptor().onFeedHttpException(request, null, throwable, -1)
    }
}