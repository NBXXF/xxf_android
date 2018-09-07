package com.xxf.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description 讲rxjava和Lifecycle结合起来
 * @Company Beijing icourt
 * @date createTime：2018/9/7
 */
public interface IRxLifecycleObserver
        extends LifecycleProvider<Lifecycle.Event>, LifecycleObserver {
    
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onBindRxLifecycle(LifecycleOwner owner, Lifecycle.Event event);
}
