package com.xxf.arch.rxjava.transformer;

import android.Manifest;

import com.xxf.arch.core.permission.RxPermissionTransformer;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description 相机权限
 * @date createTime：2018/9/3
 */
public class CameraPermissionTransformer extends RxPermissionTransformer {

    public CameraPermissionTransformer() {
        super(Manifest.permission.CAMERA);
    }
}
