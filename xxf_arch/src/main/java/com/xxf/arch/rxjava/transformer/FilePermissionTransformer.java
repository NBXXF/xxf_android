package com.xxf.arch.rxjava.transformer;

import android.content.Context;
import androidx.annotation.NonNull;

import com.xxf.arch.core.permission.RxPermissionTransformer;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description 文件读写权限
 * @date createTime：2018/9/3
 */
public class FilePermissionTransformer extends RxPermissionTransformer {

    private FilePermissionTransformer(@NonNull Context context, @NonNull String rejectNotice) {
        super(context, rejectNotice);
    }

    public FilePermissionTransformer(@NonNull Context context, boolean rejecctJumpPermissionSetting) {
        super(context, "请开启文件权限", rejecctJumpPermissionSetting);
    }

    public FilePermissionTransformer(@NonNull Context context) {
        super(context,"请开启文件权限");
    }
}
