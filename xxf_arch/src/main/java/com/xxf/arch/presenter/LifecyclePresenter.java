package com.xxf.arch.presenter;

import android.app.Application;
import android.content.Context;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xxf.arch.lifecycle.LifecycleOwnerProvider;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 支持Rxjava
 * @date createTime：2018/9/7
 */
public interface LifecyclePresenter<V> extends LifecycleOwnerProvider {
    @NonNull
    <T extends Application> T getApplication();


    <T extends Context> T getContext();

    /**
     * 获取视图层
     *
     * @return
     */
    @Nullable
    @CheckResult
    V getView();
}
