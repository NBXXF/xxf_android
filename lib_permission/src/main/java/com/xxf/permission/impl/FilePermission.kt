package com.xxf.permission.impl

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LifecycleOwner
import com.xxf.permission.requestPermissionForResult
import io.reactivex.rxjava3.core.Observable

/**
 * 申请文件读取权限
 */
@RequiresPermission(allOf = [Manifest.permission.MANAGE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE])
inline fun <reified T : LifecycleOwner> T.requestFileReadPermissionForResult(): Observable<Map<String, Boolean>> {
    return if (Build.VERSION.SDK_INT >= 30) {
        requestPermissionForResult(listOf(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE))
    } else {
        requestPermissionForResult(listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))
    }
}

/**
 * 申请文件写入权限
 */
@RequiresPermission(allOf = [Manifest.permission.MANAGE_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE])
inline fun <reified T : LifecycleOwner> T.requestFileWritePermissionForResult(): Observable<Map<String, Boolean>> {
    return if (Build.VERSION.SDK_INT >= 30) {
        requestPermissionForResult(listOf(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE))
    } else {
        requestPermissionForResult(listOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }
}