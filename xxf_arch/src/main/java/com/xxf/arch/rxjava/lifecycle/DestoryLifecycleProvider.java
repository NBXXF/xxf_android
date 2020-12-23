package com.xxf.arch.rxjava.lifecycle;

import androidx.annotation.CheckResult;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.arch.lifecycle.XXFFullLifecycleObserverAdapter;
import com.xxf.arch.rxjava.lifecycle.internal.LifecycleProvider;
import com.xxf.arch.rxjava.lifecycle.internal.LifecycleTransformer;
import com.xxf.arch.rxjava.lifecycle.internal.RxLifecycle;
import com.xxf.arch.rxjava.lifecycle.internal.RxLifecycleAndroidLifecycle;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @Description: 已经销毁
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/25 23:50
 */
class DestoryLifecycleProvider implements LifecycleProvider<Lifecycle.Event> {
    static volatile DestoryLifecycleProvider INSTANCE = new DestoryLifecycleProvider();
    final BehaviorSubject<Lifecycle.Event> lifecycleSubject = BehaviorSubject.create();

    private DestoryLifecycleProvider() {
        lifecycleSubject.onNext(Lifecycle.Event.ON_DESTROY);
    }

    @NonNull
    @Override
    @CheckResult
    public Observable<Lifecycle.Event> lifecycle() {
        return lifecycleSubject.hide();
    }

    @NonNull
    @Override
    @CheckResult
    public <T> LifecycleTransformer<T> bindUntilEvent(@NonNull Lifecycle.Event event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @NonNull
    @Override
    @CheckResult
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroidLifecycle.bindLifecycle(lifecycleSubject);
    }

}

