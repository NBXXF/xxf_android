package com.xxf.permission.contracts.impl

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import com.xxf.activityresult.contracts.EnableNotificationContract

/**
 * @author XXF
 * @version 1.0
 * @since 2024/8/28 10:36
 *  兼容各个版本 包括33动态请求权限
 *    android >=33
 *    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
 */
@SuppressLint("NewApi")
open class NotificationPermissionContractCompat : NotificationPermissionContract() {

    private val enableNotificationContract = EnableNotificationContract()


    @RequiresPermission(
        allOf = [Manifest.permission.POST_NOTIFICATIONS]
    )
    override fun createIntent(context: Context, input: Unit): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            super.createIntent(context, input)
        } else {
            enableNotificationContract.createIntent(context, Unit)
        }
    }

    @RequiresPermission(
        allOf = [Manifest.permission.POST_NOTIFICATIONS]
    )
    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            super.parseResult(resultCode, intent)
        } else {
            enableNotificationContract.parseResult(resultCode, intent)
        }
    }
}