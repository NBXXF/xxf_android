package com.xxf.wechat;

import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 与微信交互 监听 分发器
 */
public class WXEntryDispatcher {

    private static IWXAPIEventHandler WX_API_EVENT_HANDLER;

    private WXEntryDispatcher() {
    }

    public static void setEventHandler(IWXAPIEventHandler eventHandler) {
        WX_API_EVENT_HANDLER = eventHandler;
    }

    public static IWXAPIEventHandler getEventHandler() {
        return WX_API_EVENT_HANDLER;
    }
}
