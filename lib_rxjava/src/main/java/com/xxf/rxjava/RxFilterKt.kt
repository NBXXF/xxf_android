/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xxf.rxjava

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.core.*

/**
 * 筛选等同于filter 不会真的取消事件流
 */
fun <T> Observable<T>.filterWhen(
    owner: LifecycleOwner,
    vararg states: Lifecycle.State
): Observable<T> =
    this.filter {
        states.firstOrNull {
            owner.lifecycle.currentState.isAtLeast(it)
        } != null
    }

/**
 * 筛选等同于filter 不会真的取消事件流
 */
fun <T> Flowable<T>.filterWhen(owner: LifecycleOwner, vararg states: Lifecycle.State): Flowable<T> =
    this.filter {
        states.firstOrNull {
            owner.lifecycle.currentState.isAtLeast(it)
        } != null
    }


/**
 * 筛选等同于filter 不会真的取消事件流
 */
fun <T> Maybe<T>.filterWhen(owner: LifecycleOwner, vararg states: Lifecycle.State): Maybe<T> =
    this.filter {
        states.firstOrNull {
            owner.lifecycle.currentState.isAtLeast(it)
        } != null
    }
