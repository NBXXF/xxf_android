package com.xxf.arch.presenter;

import com.xxf.arch.lifecycle.XXFFullLifecycleObserver;


/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description 支持Rxjava 并支持Lifecycle生命周期
 * @date createTime：2018/9/7
 */
public interface LifecyclePresenter<V> extends XXFFullLifecycleObserver {

    /**
     * 获取视图层
     *
     * @return
     */
    V getView();
}
