package com.xxf.activityresult

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.exceptions.CompositeException
import io.reactivex.rxjava3.exceptions.Exceptions
import io.reactivex.rxjava3.plugins.RxJavaPlugins

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  用新方式来 处理activityForResult和 permissionForResult
 * @date createTime：2020/9/4
 */
class ActivityResultContractObservable<I, O>(
    private val owner: LifecycleOwner,
    private val contact: ActivityResultContract<I, O>,
    private val input: I,
    private val options: ActivityOptionsCompat? = null,
) : Observable<O>() {
    private abstract class EventHandler<O> : ActivityResultCallback<O>, Disposable;
    private var eventHandler: EventHandler<O>? = null;

    private var terminated = false
    override fun subscribeActual(observer: Observer<in O>) {
        this.eventHandler=object : EventHandler<O>() {
            override fun onActivityResult(result: O) {
                try {
                    observer.onNext(result)
                    if (!isDisposed) {
                        terminated = true
                        observer.onComplete()
                    }
                } catch (t: Throwable) {
                    Exceptions.throwIfFatal(t)
                    if (terminated) {
                        RxJavaPlugins.onError(t)
                    } else if (!isDisposed) {
                        try {
                            observer.onError(t)
                        } catch (inner: Throwable) {
                            Exceptions.throwIfFatal(inner)
                            RxJavaPlugins.onError(CompositeException(t, inner))
                        }
                    }
                }
            }

            override fun dispose() {
                this@ActivityResultContractObservable.eventHandler = null;
            }

            override fun isDisposed(): Boolean {
                return this@ActivityResultContractObservable.eventHandler == null;
            }

        }.also {
            this.eventHandler = it
            observer.onSubscribe(it)
        }
        try {
            owner.startActivityForResult(contact, input, options,this.eventHandler!!)
        }catch (t:Throwable){
            t.printStackTrace()
            Exceptions.throwIfFatal(t)
            if (terminated) {
                RxJavaPlugins.onError(t)
            } else if (this.eventHandler?.isDisposed != true) {
                try {
                    observer.onError(t)
                } catch (inner: Throwable) {
                    Exceptions.throwIfFatal(inner)
                    RxJavaPlugins.onError(CompositeException(t, inner))
                }
            }
        }
    }
}