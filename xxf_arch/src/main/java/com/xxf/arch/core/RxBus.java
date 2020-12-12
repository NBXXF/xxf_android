package com.xxf.arch.core;

import androidx.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @version 2.3.0
 * @Description 增加Rxbus 提供全局事件收发
 * @date createTime：2018/1/4
 */
public final class RxBus {

    private final Subject<Object> bus;

    private RxBus() {
        bus = PublishSubject.create().toSerialized();
    }

    private static class SingletonHolder {
        private static final RxBus defaultRxBus = new RxBus();
    }

    public static RxBus getInstance() {
        return SingletonHolder.defaultRxBus;
    }

    /**
     * 发送 事件
     *
     * @param event
     * @return 是否发送成功, event==null false
     */
    public boolean postEvent(@NonNull Object event) {
        if (event == null) {
            return false;
        }
        bus.onNext(event);
        return true;
    }

    /**
     * 是否有Observable订阅
     *
     * @return
     */
    public boolean hasObservable() {
        return bus.hasObservers();
    }

    /**
     * 订阅特定类型的Event Observable,注意线程问题
     */
    public <T> Observable<T> subscribeEvent(Class<T> eventType) {
        return bus.ofType(eventType);
    }
}