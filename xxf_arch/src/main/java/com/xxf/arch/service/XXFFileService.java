package com.xxf.arch.service;

/**
 * @Description: java类作用描述
 * @Author: XGod
 * @CreateDate: 2020/6/14 13:20
 */
public interface XXFFileService extends StringFileService {

    static XXFFileService getDefault() {
        return XXFileServiceImpl.getInstance();
    }


}
