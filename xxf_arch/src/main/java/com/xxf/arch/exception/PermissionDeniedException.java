package com.xxf.arch.exception;

import com.alibaba.android.arouter.utils.TextUtils;

/**
 * @Description: 权限拒绝异常
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/30 19:05
 */
public class PermissionDeniedException extends RuntimeException {
    final String permission;

    public String getPermission() {
        return permission;
    }

    public PermissionDeniedException(String permission) {
        super(convertDesc(permission));
        this.permission = permission;
    }

    private static final String convertDesc(String permission) {
        String permissionSimpleStr = "";
        if (!TextUtils.isEmpty(permission)) {
            permissionSimpleStr = permission.replace("android.permission.", "");
        }
        return String.format("%s Permission Denied", permissionSimpleStr);
    }
}
