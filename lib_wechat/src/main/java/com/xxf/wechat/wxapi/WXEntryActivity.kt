package com.xxf.wechat.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.xxf.wechat.WXEntryDispatcher

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
class WXEntryActivity : Activity(), IWXAPIEventHandler {
    var api: IWXAPI? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, null as String?)
        api!!.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api!!.handleIntent(getIntent(), this)
    }

    override fun onReq(baseReq: BaseReq) {
        if (WXEntryDispatcher.eventHandler != null) {
            WXEntryDispatcher.eventHandler!!.onReq(baseReq)
        }
        finish()
    }

    override fun onResp(baseResp: BaseResp) {
        if (WXEntryDispatcher.eventHandler != null) {
            WXEntryDispatcher.eventHandler!!.onResp(baseResp)
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (api != null) {
            api!!.detach()
        }
        api = null
    }
}