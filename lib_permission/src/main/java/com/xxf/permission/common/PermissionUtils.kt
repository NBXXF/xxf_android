package com.xxf.permission.common

import android.app.Activity

object PermissionUtils {

    /**
     * 跳转到权限设置页面
     */
    fun jumpPermissionSettingPage(activity:Activity){
        JumpPermissionManagement.goToSetting(activity)
    }
}