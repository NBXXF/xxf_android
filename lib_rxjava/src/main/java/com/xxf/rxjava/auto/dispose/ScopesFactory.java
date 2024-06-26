/*
 * Copyright (C) 2019. Uber Technologies
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
package com.xxf.rxjava.auto.dispose;

import autodispose2.AutoDisposePlugins;
import autodispose2.OutsideScopeException;
import autodispose2.ScopeProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * Utilities for dealing with AutoDispose scopes.
 */
public final class ScopesFactory {

    private ScopesFactory() {
    }

    /**
     * Returns a {@link Completable} representation of the given {@code scopeProvider}. This will be
     * deferred appropriately and handle {@link OutsideScopeException OutsideScopeExceptions}.
     */
    public static Completable completableOf(ScopeProvider scopeProvider) {
        return Completable.defer(
                () -> {
                    try {
                        return scopeProvider.requestScope();
                    } catch (OutsideScopeException e) {
                        Consumer<? super OutsideScopeException> handler =
                                AutoDisposePlugins.getOutsideScopeHandler();
                        if (handler != null) {
                            handler.accept(e);
                            return Completable.complete();
                        } else {
                            return Completable.error(e);
                        }
                    }
                })
                //解决lifecycle 不能在主线的bug 2022/3/31
                .subscribeOn(AndroidSchedulers.mainThread());
    }
}
