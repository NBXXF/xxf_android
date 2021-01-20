package com.xxf.arch.exception;

/**
 * @Description: 权限拒绝异常
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/30 19:05
 */
public class PermissionDeniedException extends RuntimeException {
    final String[] permission;

    public String[] getPermission() {
        return permission;
    }

    public PermissionDeniedException(String... permission) {
        super(convertDesc(permission));
        this.permission = permission;
    }

    public static final String convertDesc(String... permissions) {
        StringBuilder permissionSb = new StringBuilder("");
        if (permissions != null && permissions.length > 0) {
            for (int i = 0; i < permissions.length; i++) {
                /**
                 * android.permission.RECORD_AUDIO
                 */
                String per = permissions[i];
                if (i != 0) {
                    permissionSb.append(",");
                }
                permissionSb.append(per.replaceAll("android.permission.", ""));
            }
        }
        return String.format("%s Permission Denied", permissionSb.toString());
    }
}
