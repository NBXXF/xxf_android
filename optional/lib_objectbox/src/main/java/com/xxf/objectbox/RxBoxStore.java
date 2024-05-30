package com.xxf.objectbox;

import io.objectbox.BoxStore;
import io.objectbox.reactive.DataSubscription;
import io.reactivex.rxjava3.core.Observable;

/**
 * Static methods to Rx-ify ObjectBox queries.
 */
public abstract class RxBoxStore {
    /**
     * Using the returned Observable, you can be notified about data changes.
     * Once a transaction is committed, you will get info on classes with changed Objects.
     */
    @SuppressWarnings("rawtypes") // BoxStore observer may return any (entity) type.
    public static Observable<Class> observable(BoxStore boxStore) {
        return Observable.create(emitter -> {
            final DataSubscription dataSubscription = boxStore.subscribe().observer(data -> {
                if (!emitter.isDisposed()) {
                    emitter.onNext(data);
                }
            });
            emitter.setCancellable(dataSubscription::cancel);
        });
    }

    /**
     * Using the returned Observable, you can be notified about data changes.
     * Once a transaction is committed, you will get info on classes with changed Objects.
     */
    @SuppressWarnings("rawtypes") // BoxStore observer may return any (entity) type.
    public static <T> Observable<Class<T>> observable(BoxStore boxStore, Class<T> tClass) {
        return Observable.create(emitter -> {
            final DataSubscription dataSubscription = boxStore.subscribe(tClass).observer(data -> {
                if (!emitter.isDisposed()) {
                    emitter.onNext(data);
                }
            });
            emitter.setCancellable(dataSubscription::cancel);
        });
    }
}