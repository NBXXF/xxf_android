package com.xxf.arch.presenter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description 支持Rxjava 并支持Lifecycle生命周期
 * @date createTime：2018/9/7
 */
public class XXFFragmentPresenter<V> extends XXFLifecyclePresenter<V> {
    private Fragment fragment;

    public XXFFragmentPresenter(@NonNull Fragment fragment, V view) {
        super(fragment, view);
        this.fragment = fragment;
    }

    public final Fragment getFragment() {
        return fragment;
    }
}
