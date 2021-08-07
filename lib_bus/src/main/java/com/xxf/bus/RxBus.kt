package com.xxf.bus

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import java.util.concurrent.ConcurrentHashMap

/**
 * @version 2.3.0
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 增加Rxbus 提供全局事件收发
 * @date createTime：2018/1/4
 */
object RxBus {
    private val mStickyEventMap: MutableMap<Class<*>, Any> = ConcurrentHashMap()
    private val bus: Subject<Any> by lazy {
        PublishSubject.create<Any>().toSerialized();
    }


    /**
     * 发送 事件
     *
     * @param event
     * @return 是否发送成功, event==null false
     */
    fun postEvent(event: Any?): Boolean {
        if (event == null) {
            return false
        }
        bus.onNext(event)
        mStickyEventMap[event.javaClass] = event
        return true
    }

    /**
     * 是否有Observable订阅
     *
     * @return
     */
    fun hasObservable(): Boolean {
        return bus.hasObservers()
    }

    /**
     * 订阅特定类型的Event Observable,注意线程问题
     */
    fun <T> subscribeEvent(eventType: Class<T>, sticky: Boolean = false): Observable<T> {
        if (sticky) {
            synchronized(mStickyEventMap) {
                val observable = bus.ofType(eventType)
                val event = mStickyEventMap[eventType]
                return if (event != null) {
                    return observable.startWith(Observable.just(eventType.cast(event)));
                } else {
                    return observable;
                }
            }
        } else {
            return bus.ofType(eventType);
        }
    }
}