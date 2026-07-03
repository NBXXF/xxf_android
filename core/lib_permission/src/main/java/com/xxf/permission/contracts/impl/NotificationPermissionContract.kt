package com.xxf.permission.contracts.impl

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import java.time.LocalDateTime

/**
 * @author XXF
 * @version 1.0
 * @since 2024/8/28 10:36
 *   android >=33
 *   <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
open class NotificationPermissionContract : ActivityResultContract<Unit, Boolean>() {
    private val proxy = ActivityResultContracts.RequestPermission()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(
        allOf = [Manifest.permission.POST_NOTIFICATIONS]
    )
    override fun createIntent(context: Context, input: Unit): Intent {
        return proxy.createIntent(context, Manifest.permission.POST_NOTIFICATIONS)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(
        allOf = [Manifest.permission.POST_NOTIFICATIONS]
    )
    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return proxy.parseResult(resultCode, intent)
    }
}