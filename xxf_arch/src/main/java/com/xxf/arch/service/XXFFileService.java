package com.xxf.arch.service;

/**
 * @Description: java类作用描述
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/14 13:20
 */
public interface XXFFileService extends StringFileService {

    static XXFFileService getDefault() {
        return XXFileServiceImpl.getInstance();
    }


}
