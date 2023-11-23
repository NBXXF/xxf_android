package com.xxf.rxjava

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import autodispose2.*
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
import com.xxf.rxjava.auto.dispose.ScopesFactory
import com.xxf.rxjava.auto.dispose.XXFViewScopeProvider
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.parallel.ParallelFlowable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：8/7/21
 * Description :定义扩展 为rxjava 绑定生命周期
 */

/**
 * 绑定rxjava 生命周期
 */
fun <T> Observable<T>.bindLifecycle(
    lifecycleOwner: LifecycleOwner,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): ObservableSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable<T>(
            ScopesFactory.completableOf(
                AndroidLifecycleScopeProvider.from(
                    lifecycleOwner,
                    untilEvent
                )
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 */
fun <T> Flowable<T>.bindLifecycle(
    lifecycleOwner: LifecycleOwner,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): FlowableSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable<T>(
            ScopesFactory.completableOf(
                AndroidLifecycleScopeProvider.from(
                    lifecycleOwner,
                    untilEvent
                )
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 */
fun <T> ParallelFlowable<T>.bindLifecycle(
    lifecycleOwner: LifecycleOwner,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): ParallelFlowableSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable<T>(
            ScopesFactory.completableOf(
                AndroidLifecycleScopeProvider.from(
                    lifecycleOwner,
                    untilEvent
                )
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 */
fun <T> Maybe<T>.bindLifecycle(
    lifecycleOwner: LifecycleOwner,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): MaybeSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable<T>(
            ScopesFactory.completableOf(
                AndroidLifecycleScopeProvider.from(
                    lifecycleOwner,
                    untilEvent
                )
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 */
fun <T> Completable.bindLifecycle(
    lifecycleOwner: LifecycleOwner,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): CompletableSubscribeProxy {
    return this.to(
        AutoDispose.autoDisposable<T>(
            ScopesFactory.completableOf(
                AndroidLifecycleScopeProvider.from(
                    lifecycleOwner,
                    untilEvent
                )
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 */
fun <T> Single<T>.bindLifecycle(
    lifecycleOwner: LifecycleOwner,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): SingleSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable<T>(
            ScopesFactory.completableOf(
                AndroidLifecycleScopeProvider.from(
                    lifecycleOwner,
                    untilEvent
                )
            )
        )
    );
}


/**
 * 绑定rxjava 生命周期
 */
fun <T> Observable<T>.bindLifecycle(
    view: View,
    checkAttached: Boolean = true
): ObservableSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable(
            ScopesFactory.completableOf(
                XXFViewScopeProvider.from(
                    view,
                    checkAttached
                )
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 *  * @param checkAttached 默认检查是否attached  否则在view init 构造方法等 会出现 view is not attached!
 */
fun <T> Flowable<T>.bindLifecycle(
    view: View,
    checkAttached: Boolean = true
): FlowableSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable(
            ScopesFactory.completableOf(
                XXFViewScopeProvider.from(
                    view,
                    checkAttached
                )
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 *  @param checkAttached 默认检查是否attached  否则在view init 构造方法等 会出现 view is not attached!
 */
fun <T> ParallelFlowable<T>.bindLifecycle(
    view: View,
    checkAttached: Boolean = true
): ParallelFlowableSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable(
            ScopesFactory.completableOf(
                XXFViewScopeProvider.from(
                    view,
                    checkAttached
                )
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 * @param checkAttached 默认检查是否attached  否则在view init 构造方法等 会出现 view is not attached!
 */
fun <T> Maybe<T>.bindLifecycle(view: View, checkAttached: Boolean = true): MaybeSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable(
            ScopesFactory.completableOf(
                XXFViewScopeProvider.from(
                    view,
                    checkAttached
                )
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 */
fun <T> Completable.bindLifecycle(
    view: View,
    checkAttached: Boolean = true
): CompletableSubscribeProxy {
    return this.to(
        AutoDispose.autoDisposable<T>(
            ScopesFactory.completableOf(
                XXFViewScopeProvider.from(
                    view,
                    checkAttached
                )
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 * @param checkAttached 默认检查是否attached  否则在view init 构造方法等 会出现 view is not attached!
 */
fun <T> Single<T>.bindLifecycle(
    view: View,
    checkAttached: Boolean = true
): SingleSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable(
            ScopesFactory.completableOf(
                XXFViewScopeProvider.from(
                    view,
                    checkAttached
                )
            )
        )
    );
}