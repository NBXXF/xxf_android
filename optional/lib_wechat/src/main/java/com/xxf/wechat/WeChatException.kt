package com.xxf.wechat

import com.tencent.mm.opensdk.modelbase.BaseResp

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
class WeChatException(val baseResp: BaseResp) : RuntimeException(String.format("%s:%s", baseResp.errCode, baseResp.errStr)) {
    override fun toString(): String {
        return String.format("code:%s;errorDes:%s", baseResp.errCode, baseResp.errStr)
    }
}