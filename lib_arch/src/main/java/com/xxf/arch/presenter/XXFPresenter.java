package com.xxf.arch.presenter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.application.ApplicationInitializer;
import com.xxf.arch.lifecycle.XXFLifecycleObserver;

import java.util.Objects;

/**
 * @Description: 能轻量感知感知生命周期
 * XXF.bindToLifecycle(this)
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2018/6/25 9:04
 */
public class XXFPresenter<V> implements LifecyclePresenter<V>, XXFLifecycleObserver {
    private LifecycleOwner lifecycleOwner;
    private V view;

    /**
     * @param lifecycleOwner
     * @param view           可以为空
     */
    public XXFPresenter(@NonNull LifecycleOwner lifecycleOwner, @Nullable V view) {
        this.lifecycleOwner = Objects.requireNonNull(lifecycleOwner);
        this.view = view;
        lifecycleOwner.getLifecycle().removeObserver(this);
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @NonNull
    @Override
    public final <T extends Application> T getApplication() {
        return (T) ApplicationInitializer.applicationContext;
    }

    @Override
    public final <T extends Context> T getContext() {
        if (this.lifecycleOwner instanceof Activity) {
            return (T) lifecycleOwner;
        } else if (lifecycleOwner instanceof Fragment) {
            return (T) ((Fragment) lifecycleOwner).getContext();
        }
        return null;
    }

    /**
     * 可空
     * 1. onDestroy执行后 view  就空了,为了不影响组件的正常释放,请严格遵从组件的生命周期
     * 2. 参数view 传递也可能为空,
     *
     * @return
     */
    @Nullable
    @CheckResult(suggest = "view maybe null,or lifecycle is destroy state!")
    @Override
    public final V getView() {
        return this.view;
    }

    @Override
    public final LifecycleOwner getLifecycleOwner() {
        return this.lifecycleOwner;
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
