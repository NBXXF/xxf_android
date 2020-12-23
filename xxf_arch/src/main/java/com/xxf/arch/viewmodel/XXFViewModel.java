package com.xxf.arch.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;


import com.xxf.arch.rxjava.lifecycle.internal.LifecycleProvider;
import com.xxf.arch.rxjava.lifecycle.internal.LifecycleTransformer;
import com.xxf.arch.rxjava.lifecycle.internal.RxLifecycle;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 具有RxJava生命周期管理, XXF.bindToLifecycle(this);
 * @Company Beijing icourt
 * @date createTime：2018/9/7
 */
public class XXFViewModel extends AndroidViewModel implements LifecycleProvider<Event> {
    private final BehaviorSubject<Event> lifecycleSubject = BehaviorSubject.create();

    public XXFViewModel(@NonNull Application application) {
        super(application);
        lifecycleSubject.onNext(Event.ON_CREATE);
    }

    @Override
    public final Observable<Event> lifecycle() {
        return lifecycleSubject.hide();
    }

    @NonNull
    @Override
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull Event event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bind(lifecycleSubject, LifecycleFunction.INSTANCE);
    }

    @CallSuper
    @Override
    protected void onCleared() {
        super.onCleared();
        lifecycleSubject.onNext(Event.ON_CLEARED);
    }
}
