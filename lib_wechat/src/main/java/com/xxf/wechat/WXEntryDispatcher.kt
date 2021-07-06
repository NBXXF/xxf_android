package com.xxf.wechat

import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 与微信交互 监听 分发器
 */
object WXEntryDispatcher {
   open var eventHandler: IWXAPIEventHandler? = null
}