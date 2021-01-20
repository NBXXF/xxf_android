package com.xxf.arch.exception;

import com.xxf.arch.XXF;

import java.util.LinkedHashMap;
import java.util.Map;

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


    /**
     * 获取权限结果
     *
     * @return
     */
    public LinkedHashMap<String, Boolean> getPermissionResult() {
        LinkedHashMap<String, Boolean> linkedHashMap = new LinkedHashMap<>();
        if (permission != null) {
            for (String per : permission) {
                linkedHashMap.put(per, XXF.isGrantedPermission(per));
            }
        }
        return linkedHashMap;
    }

    public PermissionDeniedException(String... permission) {
        super(convertDesc(permission));
        this.permission = permission;
    }

    /**
     * 将权限被拒绝的组装成描述
     *
     * @param permissions
     * @return
     */
    public static final String convertDesc(String... permissions) {
        LinkedHashMap<String, Boolean> permissionResult = new PermissionDeniedException(permissions).getPermissionResult();
        StringBuilder permissionSb = new StringBuilder("");
        int i = 0;
        for (Map.Entry<String, Boolean> entry : permissionResult.entrySet()) {
            if (!entry.getValue()) {
                if (i != 0) {
                    permissionSb.append(",");
                }
                i++;
                permissionSb.append(entry.getKey().replace("android.permission.", ""));
            }
        }
        return String.format("%s Permission Denied", permissionSb.toString());
    }
}
