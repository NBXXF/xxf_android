package com.xxf.arch.service;

import android.Manifest;

import androidx.annotation.RequiresPermission;

import java.io.File;

import io.reactivex.Observable;

/**
 * @Description: java类作用描述
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/14 13:04
 */
public interface UserFileService {

    static UserFileService getDefault() {
        return XXFileServiceImpl.getInstance();
    }

    Observable<File> getUserPrivateFileDir();

    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    Observable<File> getUserPublicFileDir();


    Observable<File> getPrivateFileDir();

    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    Observable<File> getPublicFileDir();
}
