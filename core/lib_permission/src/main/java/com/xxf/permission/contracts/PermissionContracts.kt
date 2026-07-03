package com.xxf.permission.contracts

import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.permission.contracts.impl.NotificationPermissionContract
import com.xxf.permission.contracts.impl.NotificationPermissionContractCompat

/**
 * @author XXF
 * @version 1.0
 * @since 2024/8/28 10:34
 * 索引清单
 */
class PermissionContracts {
    /**
     * 通知权限
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    class NotificationPermission : NotificationPermissionContract()

    /**
     * 通知权限
     */
    class NotificationPermissionCompat : NotificationPermissionContractCompat()

}