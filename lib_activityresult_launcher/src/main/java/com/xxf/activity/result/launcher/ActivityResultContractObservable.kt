package com.xxf.activity.result.launcher

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


class ActivityResultContractObservable<I,O>(
    private val owner:LifecycleOwner,
    private val contact: ActivityResultContract<I, O>,
    private val input:I,
    private val options: ActivityOptionsCompat?=null,
                             ) : Observable<O>(){
    private abstract class EventHandler<O> : ActivityResultCallback<O>,Disposable;
    private var eventHandler:EventHandler<O>?=null;

    var terminated = false
    override fun subscribeActual(observer: Observer<in O>) {
        owner.startActivityForResult(contact,input,options,object :EventHandler<O>(){
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
                this@ActivityResultContractObservable.eventHandler=null;
            }

            override fun isDisposed(): Boolean {
                return this@ActivityResultContractObservable.eventHandler==null;
            }

        }.also {
            this.eventHandler=it
            observer.onSubscribe(it)
        })
    }
}