package com.xxf.permission

import android.content.Context
import java.util.*

/**
 * @Description: 权限拒绝异常
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/30 19:05
 */
class PermissionDeniedException(val context: Context, vararg val permission: String) :
    RuntimeException(convertDesc(context, *permission)) {

    /**
     * 获取权限结果
     *
     * @return
     */
    fun getPermissionResult(context: Context): Map<String, Boolean> {
        return getPermissionResult(context, *permission)
    }

    companion object {
        fun getPermissionResult(
            context: Context,
            vararg permissionStr: String
        ): Map<String, Boolean> {
            return context.checkSelfPermissionForResult(*permissionStr)
        }

        /**
         * 将权限被拒绝的组装成描述
         *
         * @param permissionArr
         * @return
         */
        fun convertDesc(context: Context, vararg permissionArr: String): String {
            val permissionResult = getPermissionResult(context, *permissionArr)
            val joinToString = permissionResult.filter {
                !it.value
            }.keys.joinToString(",") {
                it.replace("android.permission.", "")
            }
            return String.format("%s Permission Denied", joinToString)
        }
    }
}