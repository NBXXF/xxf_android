package com.xxf.wechat

import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.xxf.application.applicationContext
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.exceptions.CompositeException
import io.reactivex.rxjava3.exceptions.Exceptions
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import java.lang.RuntimeException

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 微信授权与分享
 */
/**
 * @param appId 对应开放平台注册的appId
 * @param baseReq 参考[com.xxf.wechat.WeChatUtils]
 */
class WeChatObservable(var appId: String, var baseReq: BaseReq) : Observable<BaseResp>() {
    private abstract class EventHandler : IWXAPIEventHandler, Disposable

    var terminated = false
    override fun subscribeActual(observer: Observer<in BaseResp>) {
        var eventHandler: EventHandler?
        WXEntryDispatcher.eventHandler = (object : EventHandler() {
            override fun dispose() {
                WXEntryDispatcher.eventHandler = (null)
            }

            override fun isDisposed(): Boolean {
                return WXEntryDispatcher.eventHandler == null
            }

            override fun onReq(baseReq: BaseReq) {
                /* if (observer != null) {
                    observer.onSubscribe(this);
                }*/
            }

            override fun onResp(resp: BaseResp) {
                if (observer == null) {
                    return
                }
                when (resp.errCode) {
                    BaseResp.ErrCode.ERR_OK -> try {
                        observer.onNext(resp)
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
                    else -> {
                        val t = WeChatException(resp)
                        try {
                            observer.onError(t)
                        } catch (inner: Throwable) {
                            Exceptions.throwIfFatal(inner)
                            RxJavaPlugins.onError(CompositeException(t, inner))
                        }
                    }
                }
            }
        }.also { eventHandler = it })
        observer.onSubscribe(eventHandler!!)
        val wxApi = WXAPIFactory.createWXAPI(applicationContext, appId,false)
        if(!wxApi.registerApp(appId)){
            val t=RuntimeException("微信注册失败:$appId");
            try {
                observer.onError(t)
            } catch (inner: Throwable) {
                Exceptions.throwIfFatal(inner)
                RxJavaPlugins.onError(CompositeException(t, inner))
            }
            return
        }
        if(!wxApi.sendReq(baseReq)){
            val t=RuntimeException("微信请求失败");
            try {
                observer.onError(t)
            } catch (inner: Throwable) {
                Exceptions.throwIfFatal(inner)
                RxJavaPlugins.onError(CompositeException(t, inner))
            }
            return
        }
    }
}