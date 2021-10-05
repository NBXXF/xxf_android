package com.xxf.http.demo;

import android.util.Log;

import androidx.annotation.Nullable;

import com.xxf.arch.http.adapter.rxjava2.RxJavaCallAdapterInterceptor;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import retrofit2.Call;

/**
 * @Description: java类作用描述
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/7/1 15:47
 */
public class DefaultCallAdapter implements RxJavaCallAdapterInterceptor {
    @Override
    public Object adapt(Call call, @Nullable Object[] args, Object rxJavaObservable) {
        if (rxJavaObservable instanceof Observable) {
            Observable observable = (Observable) rxJavaObservable;
            return observable.map(new Function() {
                @Override
                public Object apply(Object o) throws Exception {
                    return o;
                }
            }).doOnNext(new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    //  Log.d("==============>全局收到结果：" + call.request().url() + o);
                }
            }).doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    Log.d("","==============>全局收到异常：" + call.request().url() + throwable);
                }
            });
        }
        return rxJavaObservable;
    }
}
