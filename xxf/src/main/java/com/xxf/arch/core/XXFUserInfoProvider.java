package com.xxf.arch.core;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @Description: 提供user id
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/14 13:27
 */
public interface XXFUserInfoProvider extends IProvider {
    /**
     * 登录id
     *
     * @return
     */
    String getUserId();
}
