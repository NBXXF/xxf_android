package com.xxf.arch.arouter;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @Description: 提供user id
 * @Author: XGod
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
