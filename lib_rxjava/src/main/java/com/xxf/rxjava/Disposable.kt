package com.xxf.rxjava

import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.disposables.DisposableContainer

/**
 * 绑定到容器中
 * @param destination 容器 [io.reactivex.rxjava3.disposables.CompositeDisposable] 或者[io.reactivex.rxjava3.internal.disposables.ListCompositeDisposable]
 */
fun Disposable.collectTo(destination: DisposableContainer): Boolean {
    return destination.add(this)
}

/**
 * 绑定到容器中
 * @param destination 容器 [io.reactivex.rxjava3.disposables.CompositeDisposable] 或者[io.reactivex.rxjava3.internal.disposables.ListCompositeDisposable]
 */
fun Disposable.bindDisposableContainer(destination: DisposableContainer): Boolean {
    return this.collectTo(destination)
}