package com.xxf.arch.http.model;

import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description 请求结果 基础模型分发
 * @date createTime：2018/9/7
 */
public interface IResponseBodyDTO {

    /**
     * 获取响应body失败原因
     *
     * @return
     */
    @Nullable
    IOException getResponseBodyException();

}
