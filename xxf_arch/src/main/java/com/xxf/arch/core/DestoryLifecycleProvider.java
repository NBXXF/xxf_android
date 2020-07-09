package com.xxf.arch.core;

import androidx.annotation.CheckResult;
import androidx.lifecycle.Lifecycle;

import com.xxf.arch.rxjava.lifecycle.internal.LifecycleProvider;
import com.xxf.arch.rxjava.lifecycle.internal.LifecycleTransformer;
import com.xxf.arch.rxjava.lifecycle.internal.RxLifecycle;
import com.xxf.arch.rxjava.lifecycle.internal.RxLifecycleAndroidLifecycle;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @Description: 已经销毁
 * @Author: XGod
 * @CreateDate: 2020/6/25 23:50
 */
public class DestoryLifecycleProvider implements LifecycleProvider<Lifecycle.Event> {
    static volatile DestoryLifecycleProvider INSTANCE = new DestoryLifecycleProvider();
    final BehaviorSubject<Lifecycle.Event> lifecycleSubject = BehaviorSubject.create();

    public DestoryLifecycleProvider() {
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

