package com.xxf.arch.presenter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description 支持Rxjava 并支持Lifecycle生命周期
 * @date createTime：2018/9/7
 */
public class XXFActivityPresenter<V> extends XXFLifecyclePresenter<V> {
    private FragmentActivity activity;

    public XXFActivityPresenter(@NonNull FragmentActivity activity, V view) {
        super(activity, view);
        this.activity = activity;
    }

    public final FragmentActivity getActivity() {
        return activity;
    }
}
