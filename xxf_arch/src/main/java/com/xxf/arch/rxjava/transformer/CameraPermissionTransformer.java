package com.xxf.arch.rxjava.transformer;

import android.content.Context;
import androidx.annotation.NonNull;

import com.xxf.arch.core.permission.RxPermissionTransformer;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description 相机权限
 * @date createTime：2018/9/3
 */
public class CameraPermissionTransformer extends RxPermissionTransformer {

    private CameraPermissionTransformer(@NonNull Context context, @NonNull String rejectNotice) {
        super(context, rejectNotice);
    }

    public CameraPermissionTransformer(@NonNull Context context, boolean rejecctJumpPermissionSetting) {
        super(context, "请开启相机权限", rejecctJumpPermissionSetting);
    }

    public CameraPermissionTransformer(@NonNull Context context) {
        super(context, "请开启相机权限");
    }
}
