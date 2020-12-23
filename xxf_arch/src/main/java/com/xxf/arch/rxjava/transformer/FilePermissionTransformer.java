package com.xxf.arch.rxjava.transformer;

import android.Manifest;

import com.xxf.arch.core.permission.RxPermissionTransformer;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 文件读写权限
 * @date createTime：2018/9/3
 */
public class FilePermissionTransformer extends RxPermissionTransformer {

    public FilePermissionTransformer() {
        super(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
