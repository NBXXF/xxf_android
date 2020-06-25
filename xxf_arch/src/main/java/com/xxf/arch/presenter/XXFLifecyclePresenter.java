package com.xxf.arch.presenter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.arch.XXF;
import com.xxf.arch.lifecycle.XXFFullLifecycleObserver;
import com.xxf.arch.lifecycle.XXFFullLifecycleObserverAdapter;

import java.util.Objects;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description 支持Rxjava 并支持Lifecycle生命周期,
 * 在Presenter层使用[权限]
 * 或者[startActivityForResult]
 * 或者[loading]
 * 或者[Toast]@{@link XXF}
 * <p>
 * LifecycleOwner官方issus,杀掉App不会执行onDestory,暂时无伤大雅！
 * @date createTime：2018/9/7
 */
public class XXFLifecyclePresenter<V> extends XXFPresenter<V> implements XXFFullLifecycleObserver {
    private XXFFullLifecycleObserverAdapter lifecycleObserverAdapter;

    /**
     * @param lifecycleOwner
     * @param view           可以为空
     */
    public XXFLifecyclePresenter(@NonNull LifecycleOwner lifecycleOwner, @Nullable V view) {
        super(lifecycleOwner,view);
        registerSingleObserver(lifecycleOwner);
    }

    /**
     * 管理生命周期
     *
     * @param lifecycleOwner
     */
    private final void registerSingleObserver(@NonNull LifecycleOwner lifecycleOwner) {
        Objects.requireNonNull(lifecycleOwner);
        if (lifecycleObserverAdapter != null) {
            lifecycleOwner.getLifecycle().removeObserver(lifecycleObserverAdapter);
        } else {
            lifecycleObserverAdapter = new XXFFullLifecycleObserverAdapter(this);
            //默认注册观察者
            lifecycleOwner.getLifecycle().addObserver(lifecycleObserverAdapter);
        }
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
}
