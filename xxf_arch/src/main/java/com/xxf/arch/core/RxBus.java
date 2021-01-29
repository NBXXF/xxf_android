package com.xxf.arch.core;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;


/**
 * @version 2.3.0
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 增加Rxbus 提供全局事件收发
 * @date createTime：2018/1/4
 */
public final class RxBus {
    private final Map<Class<?>, Object> mStickyEventMap = new ConcurrentHashMap<>();
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
        mStickyEventMap.put(event.getClass(), event);
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


    /**
     * 订阅粘性事件
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> subscribeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = bus.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);
            if (event != null) {
                return observable.startWith(Observable.just(eventType.cast(event)));
            } else {
                return observable;
            }
        }
    }
}