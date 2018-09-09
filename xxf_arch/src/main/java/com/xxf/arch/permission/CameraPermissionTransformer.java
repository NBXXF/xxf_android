package com.xxf.arch.permission;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description 相机权限
 * @date createTime：2018/9/3
 */
public class CameraPermissionTransformer extends XXFPermissionTransformer {

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
