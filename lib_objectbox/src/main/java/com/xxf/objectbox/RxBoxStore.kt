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

import io.objectbox.BoxStore
import io.reactivex.rxjava3.core.Observable

/**
 * Static methods to Rx-ify ObjectBox queries.
 */
object RxBoxStore {
    /**
     * Using the returned Observable, you can be notified about data changes.
     * Once a transaction is committed, you will get info on classes with changed Objects.
     */
    fun <T> observable(boxStore: BoxStore): Observable<Class<*>> {
        return Observable.create { emitter ->
            val dataSubscription = boxStore.subscribe().observer { data ->
                if (!emitter.isDisposed) {
                    emitter.onNext(data)
                }
            }
            emitter.setCancellable { dataSubscription.cancel() }
        }
    }
}