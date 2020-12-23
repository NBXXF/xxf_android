package com.xxf.wechat;

import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public class WeChatException extends RuntimeException {
    private BaseResp baseResp;

    public WeChatException(BaseResp baseResp) {
        super(String.format("%s:%s", baseResp.errCode, baseResp.errStr));
        this.baseResp = baseResp;
    }

    public BaseResp getBaseResp() {
        return baseResp;
    }

    @Override
    public String toString() {
        return String.format("code:%s;errorDes:%s", baseResp.errCode, baseResp.errStr);
    }
}
