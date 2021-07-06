/*
 * Copyright 2017 ObjectBox Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xxf.objectbox;

import java.util.List;

import io.objectbox.query.Query;
import io.objectbox.reactive.DataObserver;
import io.objectbox.reactive.DataSubscription;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.functions.Cancellable;

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
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(final FlowableEmitter<T> emitter) throws Exception {
                createListItemEmitter(query, emitter);
            }

        }, strategy);
    }

    static <T> void createListItemEmitter(final Query<T> query, final FlowableEmitter<T> emitter) {
        final DataSubscription dataSubscription = query.subscribe().observer(new DataObserver<List<T>>() {
            @Override
            public void onData(List<T> data) {
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
            }
        });
        emitter.setCancellable(new Cancellable() {
            @Override
            public void cancel() throws Exception {
                dataSubscription.cancel();
            }
        });
    }

    /**
     * The returned Observable emits Query results as Lists.
     * Never completes, so you will get updates when underlying data changes
     * (see {@link Query#subscribe()} for details).
     */
    public static <T> Observable<List<T>> observable(final Query<T> query) {
        return Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<T>> emitter) throws Exception {
                final DataSubscription dataSubscription = query.subscribe().observer(new DataObserver<List<T>>() {
                    @Override
                    public void onData(List<T> data) {
                        if (!emitter.isDisposed()) {
                            emitter.onNext(data);
                        }
                    }
                });
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        dataSubscription.cancel();
                    }
                });
            }
        });
    }

    /**
     * 只观察变化  .onlyChanges()
     * The returned Observable emits Query results as Lists.
     * Never completes, so you will get updates when underlying data changes
     * (see {@link Query#subscribe()} for details).
     */
    public static <T> Observable<List<T>> observableChange(final Query<T> query) {
        return Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<T>> emitter) throws Exception {
                final DataSubscription dataSubscription = query.subscribe()
                        .onlyChanges()
                        .observer(new DataObserver<List<T>>() {
                            @Override
                            public void onData(List<T> data) {
                                if (!emitter.isDisposed()) {
                                    emitter.onNext(data);
                                }
                            }
                        });
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        dataSubscription.cancel();
                    }
                });
            }
        });
    }

    /**
     * The returned Single emits one Query result as a List.
     */
    public static <T> Single<List<T>> single(final Query<T> query) {
        return Single.create(new SingleOnSubscribe<List<T>>() {
            @Override
            public void subscribe(final SingleEmitter<List<T>> emitter) throws Exception {
                query.subscribe().single().observer(new DataObserver<List<T>>() {
                    @Override
                    public void onData(List<T> data) {
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(data);
                        }
                    }
                });
                // no need to cancel, single never subscribes
            }
        });
    }
}
