package com.xxf.arch.rxjava.transformer;

import android.Manifest;

import com.xxf.arch.core.permission.RxPermissionTransformer;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description 文件读写权限
 * @date createTime：2018/9/3
 */
public class FilePermissionTransformer extends RxPermissionTransformer {

    public FilePermissionTransformer() {
        super(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
