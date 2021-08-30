package com.xxf.rxjava

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import autodispose2.*
import autodispose2.android.ViewScopeProvider
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
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
inline fun <reified T> Observable<T>.bindLifecycle(
    lifecycleOwner: LifecycleOwner,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): ObservableSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable<T>(
            AndroidLifecycleScopeProvider.from(
                lifecycleOwner,
                untilEvent
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 */
inline fun <reified T> Flowable<T>.bindLifecycle(
    lifecycleOwner: LifecycleOwner,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): FlowableSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable<T>(
            AndroidLifecycleScopeProvider.from(
                lifecycleOwner,
                untilEvent
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 */
inline fun <reified T> ParallelFlowable<T>.bindLifecycle(
    lifecycleOwner: LifecycleOwner,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): ParallelFlowableSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable<T>(
            AndroidLifecycleScopeProvider.from(
                lifecycleOwner,
                untilEvent
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 */
inline fun <reified T> Maybe<T>.bindLifecycle(
    lifecycleOwner: LifecycleOwner,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): MaybeSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable<T>(
            AndroidLifecycleScopeProvider.from(
                lifecycleOwner,
                untilEvent
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 */
inline fun <reified T> Completable.bindLifecycle(
    lifecycleOwner: LifecycleOwner,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): CompletableSubscribeProxy {
    return this.to(
        AutoDispose.autoDisposable<T>(
            AndroidLifecycleScopeProvider.from(
                lifecycleOwner,
                untilEvent
            )
        )
    );
}

/**
 * 绑定rxjava 生命周期
 */
inline fun <reified T> Single<T>.bindLifecycle(
    lifecycleOwner: LifecycleOwner,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): SingleSubscribeProxy<T> {
    return this.to(
        AutoDispose.autoDisposable<T>(
            AndroidLifecycleScopeProvider.from(
                lifecycleOwner,
                untilEvent
            )
        )
    );
}


/**
 * 绑定rxjava 生命周期
 */
inline fun <reified T> Observable<T>.bindLifecycle(view: View): ObservableSubscribeProxy<T> {
    return this.to(AutoDispose.autoDisposable(ViewScopeProvider.from(view)));
}

/**
 * 绑定rxjava 生命周期
 */
inline fun <reified T> Flowable<T>.bindLifecycle(view: View): FlowableSubscribeProxy<T> {
    return this.to(AutoDispose.autoDisposable(ViewScopeProvider.from(view)));
}

/**
 * 绑定rxjava 生命周期
 */
inline fun <reified T> ParallelFlowable<T>.bindLifecycle(view: View): ParallelFlowableSubscribeProxy<T> {
    return this.to(AutoDispose.autoDisposable(ViewScopeProvider.from(view)));
}

/**
 * 绑定rxjava 生命周期
 */
inline fun <reified T> Maybe<T>.bindLifecycle(view: View): MaybeSubscribeProxy<T> {
    return this.to(AutoDispose.autoDisposable(ViewScopeProvider.from(view)));
}

/**
 * 绑定rxjava 生命周期
 */
inline fun <reified T> Completable.bindLifecycle(view: View): CompletableSubscribeProxy {
    return this.to(AutoDispose.autoDisposable<T>(ViewScopeProvider.from(view)));
}

/**
 * 绑定rxjava 生命周期
 */
inline fun <reified T> Single<T>.bindLifecycle(view: View): SingleSubscribeProxy<T> {
    return this.to(AutoDispose.autoDisposable(ViewScopeProvider.from(view)));
}