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
package com.xxf.objectbox

import io.objectbox.query.Query
import io.objectbox.reactive.DataObserver
import io.reactivex.rxjava3.core.*

/**
 * Static methods to Rx-ify ObjectBox queries.
 */
object RxQuery {
    /**
     * The returned Flowable emits Query results one by one. Once all results have been processed, onComplete is called.
     * Uses BackpressureStrategy.BUFFER.
     */
    fun <T> flowableOneByOne(query: Query<T>): Flowable<T> {
        return flowableOneByOne(query, BackpressureStrategy.BUFFER)
    }

    /**
     * The returned Flowable emits Query results one by one. Once all results have been processed, onComplete is called.
     * Uses given BackpressureStrategy.
     */
    fun <T> flowableOneByOne(query: Query<T>, strategy: BackpressureStrategy?): Flowable<T> {
        return Flowable.create({ emitter -> createListItemEmitter(query, emitter) }, strategy)
    }

    fun <T> createListItemEmitter(query: Query<T>, emitter: FlowableEmitter<T>) {
        val dataSubscription = query.subscribe().observer(DataObserver { data ->
            for (datum in data) {
                if (emitter.isCancelled) {
                    return@DataObserver
                } else {
                    emitter.onNext(datum)
                }
            }
            if (!emitter.isCancelled) {
                emitter.onComplete()
            }
        })
        emitter.setCancellable { dataSubscription.cancel() }
    }

    /**
     * The returned Observable emits Query results as Lists.
     * Never completes, so you will get updates when underlying data changes
     * (see [Query.subscribe] for details).
     */
    fun <T> observable(query: Query<T>): Observable<List<T>> {
        return Observable.create { emitter ->
            val dataSubscription = query.subscribe().observer { data ->
                if (!emitter.isDisposed) {
                    emitter.onNext(data)
                }
            }
            emitter.setCancellable { dataSubscription.cancel() }
        }
    }

    /**
     * 只观察变化  .onlyChanges()
     * The returned Observable emits Query results as Lists.
     * Never completes, so you will get updates when underlying data changes
     * (see [Query.subscribe] for details).
     */
    fun <T> observableChange(query: Query<T>): Observable<List<T>> {
        return Observable.create { emitter ->
            val dataSubscription = query.subscribe()
                    .onlyChanges()
                    .observer { data ->
                        if (!emitter.isDisposed) {
                            emitter.onNext(data)
                        }
                    }
            emitter.setCancellable { dataSubscription.cancel() }
        }
    }

    /**
     * The returned Single emits one Query result as a List.
     */
    fun <T> single(query: Query<T>): Single<List<T>> {
        return Single.create { emitter ->
            query.subscribe().single().observer { data ->
                if (!emitter.isDisposed) {
                    emitter.onSuccess(data)
                }
            }
            // no need to cancel, single never subscribes
        }
    }
}