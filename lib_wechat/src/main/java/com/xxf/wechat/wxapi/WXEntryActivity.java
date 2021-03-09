package com.xxf.wechat.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xxf.wechat.WXEntryDispatcher;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, (String) null);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        if (WXEntryDispatcher.getEventHandler() != null) {
            WXEntryDispatcher.getEventHandler().onReq(baseReq);
        }
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (WXEntryDispatcher.getEventHandler() != null) {
            WXEntryDispatcher.getEventHandler().onResp(baseResp);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (api != null) {
            api.detach();
        }
        api = null;
    }
}
