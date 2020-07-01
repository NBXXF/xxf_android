package com.xxf.arch.http.adapter.rxjava2;

import retrofit2.Call;

/**
 * @Description: RxJava全局拦截器
 * @Author: XGod
 * @CreateDate: 2020/7/1 15:31
 */
public interface RxJavaCallAdapterInterceptor {

    /**
     * @param call
     * @param args
     * @param rxJavaObservable 返回的rxJava Observable,Flowable...
     * @return 返回对应的 Object rxJavaObservable
     */
    Object adapt(Call call, @androidx.annotation.Nullable Object[] args, Object rxJavaObservable);
}
