package com.xxf.wechat;

import android.accounts.AuthenticatorException;
import android.content.Context;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 微信授权与分享
 */
public class WeChatObservable extends Observable<BaseResp> {

    private abstract static class EventHandler
            implements IWXAPIEventHandler, Disposable {

    }

    String clientId;
    boolean terminated = false;
    BaseReq baseReq;
    Context context;

    public WeChatObservable(Context context, String clientId, BaseReq baseReq) {
        this.context = context;
        this.clientId = clientId;
        this.baseReq = baseReq;
    }

    @Override
    protected void subscribeActual(final Observer<? super BaseResp> observer) {
        EventHandler eventHandler;
        WXEntryDispatcher.setEventHandler(eventHandler = new EventHandler() {
            @Override
            public void dispose() {
                WXEntryDispatcher.setEventHandler(null);
            }

            @Override
            public boolean isDisposed() {
                return WXEntryDispatcher.getEventHandler() == null;
            }

            @Override
            public void onReq(BaseReq baseReq) {
               /* if (observer != null) {
                    observer.onSubscribe(this);
                }*/
            }

            @Override
            public void onResp(BaseResp resp) {
                if (observer == null) {
                    return;
                }
                switch (resp.errCode) {
                    //成功
                    case BaseResp.ErrCode.ERR_OK:
                        try {
                            observer.onNext(resp);
                            if (!isDisposed()) {
                                terminated = true;
                                observer.onComplete();
                            }
                        } catch (Throwable t) {
                            Exceptions.throwIfFatal(t);
                            if (terminated) {
                                RxJavaPlugins.onError(t);
                            } else if (!isDisposed()) {
                                try {
                                    observer.onError(t);
                                } catch (Throwable inner) {
                                    Exceptions.throwIfFatal(inner);
                                    RxJavaPlugins.onError(new CompositeException(t, inner));
                                }
                            }
                        }
                        break;
                    default:
                        WeChatException t = new WeChatException(resp);
                        try {
                            observer.onError(t);
                        } catch (Throwable inner) {
                            Exceptions.throwIfFatal(inner);
                            RxJavaPlugins.onError(new CompositeException(t, inner));
                        }
                        break;
                }
            }
        });
        observer.onSubscribe(eventHandler);
        IWXAPI wxApi = WXAPIFactory.createWXAPI(this.context.getApplicationContext(), this.clientId);
        wxApi.registerApp(clientId);
        wxApi.sendReq(this.baseReq);
    }
}
