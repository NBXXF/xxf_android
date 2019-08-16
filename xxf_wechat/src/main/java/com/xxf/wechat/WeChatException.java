package com.xxf.wechat;

import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
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
