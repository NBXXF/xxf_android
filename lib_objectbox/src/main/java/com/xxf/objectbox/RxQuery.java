package com.xxf.objectbox;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/11/1
 * Description ://TODO
 */

import java.util.List;

import io.objectbox.query.Query;
import io.objectbox.reactive.DataSubscription;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 * Static methods to Rx-ify ObjectBox queries.
 */
public abstract class RxQuery {
    /**
     * The returned Flowable emits Query results one by one. Once all results have been processed, onComplete is called.
     * Uses BackpressureStrategy.BUFFER.
     */
    public static <T> Flowable<T> flowableOneByOne(final Query<T> query) {
        return flowableOneByOne(query, BackpressureStrategy.BUFFER);
    }

    /**
     * The returned Flowable emits Query results one by one. Once all results have been processed, onComplete is called.
     * Uses given BackpressureStrategy.
     */
    public static <T> Flowable<T> flowableOneByOne(final Query<T> query, BackpressureStrategy strategy) {
        return Flowable.create(emitter -> createListItemEmitter(query, emitter), strategy);
    }

    static <T> void createListItemEmitter(final Query<T> query, final FlowableEmitter<T> emitter) {
        final DataSubscription dataSubscription = query.subscribe().observer(data -> {
            for (T datum : data) {
                if (emitter.isCancelled()) {
                    return;
                } else {
                    emitter.onNext(datum);
                }
            }
            if (!emitter.isCancelled()) {
                emitter.onComplete();
            }
        });
        emitter.setCancellable(dataSubscription::cancel);
    }

    /**
     * The returned Observable emits Query results as Lists.
     * Never completes, so you will get updates when underlying data changes
     * (see {@link Query#subscribe()} for details).
     */
    public static <T> Observable<List<T>> observable(final Query<T> query) {
        return Observable.create(emitter -> {
            final DataSubscription dataSubscription = query.subscribe().observer(data -> {
                if (!emitter.isDisposed()) {
                    emitter.onNext(data);
                }
            });
            emitter.setCancellable(dataSubscription::cancel);
        });
    }

    /**
     * 只观察变化  .onlyChanges()
     * The returned Observable emits Query results as Lists.
     * Never completes, so you will get updates when underlying data changes
     * (see {@link Query#subscribe()} for details).
     */
    public static <T> Observable<List<T>> observableChange(final Query<T> query) {
        return Observable.create(emitter -> {
            final DataSubscription dataSubscription = query.subscribe()
                    .onlyChanges()
                    .observer(data -> {
                        if (!emitter.isDisposed()) {
                            emitter.onNext(data);
                        }
                    });
            emitter.setCancellable(dataSubscription::cancel);
        });
    }

    /**
     * The returned Single emits one Query result as a List.
     */
    public static <T> Single<List<T>> single(final Query<T> query) {
        return Single.create(emitter -> {
            query.subscribe().single().observer(data -> {
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(data);
                }
            });
            // no need to cancel, single never subscribes
        });
    }
}