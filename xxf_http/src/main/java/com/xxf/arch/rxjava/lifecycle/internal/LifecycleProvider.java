package com.xxf.arch.rxjava.lifecycle.internal;


import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import io.reactivex.rxjava3.core.Observable;


public interface LifecycleProvider<E> {
    /**
     * @return a sequence of lifecycle events
     */
    @NonNull
    @CheckResult
    Observable<E> lifecycle();

    /**
     * Binds a source until a specific event occurs.
     *
     * @param event the event that triggers unsubscription
     * @return a reusable {@link LifecycleTransformer} which unsubscribes when the event triggers.
     */
    @NonNull
    @CheckResult
    <T> LifecycleTransformer<T> bindUntilEvent(@NonNull E event);

    /**
     * Binds a source until the next reasonable event occurs.
     *
     * @return a reusable {@link LifecycleTransformer} which unsubscribes at the correct time.
     */
    @NonNull
    @CheckResult
    <T> LifecycleTransformer<T> bindToLifecycle();
}
