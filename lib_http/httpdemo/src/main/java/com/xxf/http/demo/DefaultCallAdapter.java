package com.xxf.http.demo;

import android.util.Log;

import androidx.annotation.Nullable;

import com.xxf.arch.tracker.RxJavaCallAdapterTrackerInterceptor;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import retrofit2.Call;

/**
 * @Description: java类作用描述
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/7/1 15:47
 */
public class DefaultCallAdapter extends RxJavaCallAdapterTrackerInterceptor {
    @Override
    public Object adapt(Call call, @Nullable Object[] args, Object rxJavaObservable) {
        if (rxJavaObservable instanceof Observable) {
            Observable observable = (Observable) rxJavaObservable;
            return super.adapt(call, args, observable.doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    Log.d("", "==============>全局收到异常：" + call.request().url() + throwable);
                }
            }));
        }
        return super.adapt(call, args, rxJavaObservable);
    }
}
