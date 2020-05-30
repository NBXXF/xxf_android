package com.xxf.arch.presenter;

import androidx.annotation.NonNull;
import androidx.core.app.ComponentActivity;
import androidx.fragment.app.Fragment;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description 支持Rxjava 并支持Lifecycle生命周期
 * @date createTime：2018/9/7
 */
public class XXFActivityPresenter<V> extends XXFLifecyclePresenter<V> {
    private ComponentActivity activity;

    public XXFActivityPresenter(@NonNull ComponentActivity activity,V view) {
        super(activity, view);
        this.activity = activity;
    }

    public final ComponentActivity getActivity() {
        return activity;
    }
}
