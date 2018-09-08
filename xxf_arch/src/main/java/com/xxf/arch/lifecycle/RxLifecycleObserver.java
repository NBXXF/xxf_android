package com.xxf.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description 绑定RxJava
 * @Company Beijing icourt
 * @date createTime：2018/9/7
 */
public class RxLifecycleObserver implements IRxLifecycleObserver {
    private static final LifecycleFunction LIFECYCLEFUNCTION = new LifecycleFunction();
    private final BehaviorSubject<Lifecycle.Event> lifecycleSubject = BehaviorSubject.create();


    @Override
    public Observable<Lifecycle.Event> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_ANY) {
            throw new IllegalArgumentException("event can not Lifecycle.Event.ON_ANY");
        }
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bind(lifecycleSubject, LIFECYCLEFUNCTION);
    }

    @Override
    public void onBindRxLifecycle(LifecycleOwner owner, Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                lifecycleSubject.onNext(Lifecycle.Event.ON_CREATE);
                break;
            case ON_START:
                lifecycleSubject.onNext(Lifecycle.Event.ON_START);
                break;
            case ON_RESUME:
                lifecycleSubject.onNext(Lifecycle.Event.ON_RESUME);
                break;
            case ON_PAUSE:
                lifecycleSubject.onNext(Lifecycle.Event.ON_PAUSE);
                break;
            case ON_STOP:
                lifecycleSubject.onNext(Lifecycle.Event.ON_STOP);
                break;
            case ON_DESTROY:
                lifecycleSubject.onNext(Lifecycle.Event.ON_DESTROY);
                break;
            default:
                break;
        }
    }
}
