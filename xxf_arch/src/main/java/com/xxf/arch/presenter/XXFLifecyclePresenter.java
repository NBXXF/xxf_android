package com.xxf.arch.presenter;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.arch.lifecycle.XXFFullLifecycleObserverAdapter;

import java.util.Objects;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description 支持Rxjava 并支持Lifecycle生命周期
 * @date createTime：2018/9/7
 */
public class XXFLifecyclePresenter<V> implements LifecyclePresenter<V> {
    private LifecycleOwner lifecycleOwner;
    private V view;

    public XXFLifecyclePresenter(@NonNull LifecycleOwner lifecycleOwner, V view) {
        this.lifecycleOwner = Objects.requireNonNull(lifecycleOwner);
        this.view = Objects.requireNonNull(view);
        //默认注册观察者
        lifecycleOwner.getLifecycle().addObserver(new XXFFullLifecycleObserverAdapter(this));
    }

    /**
     * 获取  LifecycleOwner
     *
     * @return
     */
    public LifecycleOwner getLifecycleOwner() {
        return lifecycleOwner;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public final V getView() {
        return this.view;
    }
}
