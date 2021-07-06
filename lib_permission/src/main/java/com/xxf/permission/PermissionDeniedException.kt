package com.xxf.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import java.util.*

/**
 * @Description: 权限拒绝异常
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/30 19:05
 */
class PermissionDeniedException(val context: Context, vararg val permission: String) : RuntimeException(convertDesc(context,*permission)) {

    /**
     * 获取权限结果
     *
     * @return
     */
    fun getPermissionResult(context: Context): LinkedHashMap<String, Boolean> {
        return getPermissionResult(context, *permission)
    }

    companion object {
        fun getPermissionResult( context: Context, vararg permissionStr: String): LinkedHashMap<String, Boolean> {
            val linkedHashMap = LinkedHashMap<String, Boolean>()
            if (permissionStr != null) {
                for (per in permissionStr) {
                    linkedHashMap[per] =  ContextCompat.checkSelfPermission(context,per) == PackageManager.PERMISSION_GRANTED
                }
            }
            return linkedHashMap
        }

        /**
         * 将权限被拒绝的组装成描述
         *
         * @param permissions
         * @return
         */
        fun convertDesc(context: Context, vararg permissionArr: String): String {
            val permissionResult = getPermissionResult(context, *permissionArr)
            val permissionSb = StringBuilder("")
            var i = 0
            for ((key, value) in permissionResult) {
                if (!value) {
                    if (i != 0) {
                        permissionSb.append(",")
                    }
                    i++
                    permissionSb.append(key.replace("android.permission.", ""))
                }
            }
            return String.format("%s Permission Denied", permissionSb.toString())
        }
    }
}