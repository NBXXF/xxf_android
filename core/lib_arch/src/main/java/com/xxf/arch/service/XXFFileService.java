package com.xxf.arch.service;

import com.xxf.arch.model.DownloadTask;

import java.io.File;

import io.reactivex.rxjava3.core.Observable;

/**
 * @Description: java类作用描述
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/14 13:20
 */
public interface XXFFileService extends StringFileService {

    static XXFFileService getDefault() {
        return XXFileServiceImpl.getInstance();
    }

    /**
     * 下载文件
     *
     * @param url
     * @param filePath
     * @return
     */
    Observable<File> download(String url, String filePath);

    /**
     * 下载任务
     *
     * @param url
     * @param filePath
     * @return
     */
    Observable<DownloadTask> downloadTask(String url, String filePath);

}
