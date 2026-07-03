package com.xxf.permission.impl

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LifecycleOwner
import com.xxf.activityresult.contracts.EnableFileMangeContract
import com.xxf.activityresult.startActivityForResult
import com.xxf.ktx.findActivity
import com.xxf.permission.requestPermissionForResult
import io.reactivex.rxjava3.core.Observable

/**
 * 申请文件读取权限
 */
@SuppressLint("MissingPermission")
@RequiresPermission(allOf = [Manifest.permission.MANAGE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE])
inline fun <reified T : LifecycleOwner> T.requestFileReadPermissionForResult(): Observable<Map<String, Boolean>> {
    val enableFileMangeContract = EnableFileMangeContract()
    return if (enableFileMangeContract.isSupported(findActivity())) {
        if (enableFileMangeContract.isEnabled(this.findActivity())) {
            Observable.just(mapOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE to true))
        } else {
            startActivityForResult(EnableFileMangeContract())
                .map {
                    mapOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE to it)
                }
        }
    } else {
        requestPermissionForResult(listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))
    }
}

/**
 * 申请文件写入权限
 */
@SuppressLint("MissingPermission")
@RequiresPermission(allOf = [Manifest.permission.MANAGE_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE])
inline fun <reified T : LifecycleOwner> T.requestFileWritePermissionForResult(): Observable<Map<String, Boolean>> {
    val enableFileMangeContract = EnableFileMangeContract()
    return if (enableFileMangeContract.isSupported(findActivity())) {
        if (enableFileMangeContract.isEnabled(this.findActivity())) {
            Observable.just(mapOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE to true))
        } else {
            startActivityForResult(EnableFileMangeContract())
                .map {
                    mapOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE to it)
                }
        }
    } else {
        requestPermissionForResult(listOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }
}