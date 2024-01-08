package com.xxf.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.xxf.activityresult.startActivityForResult
import io.reactivex.rxjava3.core.Observable
import java.util.LinkedHashMap

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/2
 * Description ://权限拓展  支持activity fragment LifecycleOwner 挂载对象访问
 */


/**
 * activity 请求权限  可以结合内敛函数topFragmentActivity
 * @param permissions 可以参考 [android.Manifest.permission]  android.Manifest.permission
 */
inline fun <reified T : LifecycleOwner> T.requestPermission(
    permissions: List<String>
): Observable<Boolean> {
    return this.requestPermissionForResult(permissions).map {
        it.values.all { it }
    }
}

inline fun <reified T : LifecycleOwner> T.requestPermissionForResult(
    permissions: List<String>
): Observable<Map<String, Boolean>> {
    return this.startActivityForResult(
        ActivityResultContracts.RequestMultiplePermissions(),
        permissions.toTypedArray()
    )
}


/**
 * activity 请求权限  可以结合内敛函数topFragmentActivity
 * @param permission 可以参考 [android.Manifest.permission]  android.Manifest.permission
 */
inline fun <reified T : LifecycleOwner> T.requestPermission(
    vararg permission: String,
): Observable<Boolean> {
    return this.requestPermission(permission.asList())
}


inline fun <reified T : LifecycleOwner> T.requestPermissionForResult(
    vararg permission: String,
): Observable<Map<String, Boolean>> {
    return this.requestPermissionForResult(permission.asList())
}




/**
 * 批量判断是否授予权限
 */
inline fun <reified T : Context> T.checkSelfPermission(
    permissions: List<String>
): Boolean {
    permissions.forEach {
        if (ContextCompat.checkSelfPermission(this, it) ==
            PackageManager.PERMISSION_DENIED
        ) {
            return false
        }
    }
    return true
}


/**
 * 批量查询权限是否授权
 */
inline fun <reified T : Context> T.checkSelfPermissionForResult(
    vararg permission: String
): Map<String, Boolean> {
    return this.checkSelfPermissionForResult(permission.asList())
}

/**
 * 批量查询权限是否授权
 */
inline fun <reified T : Context> T.checkSelfPermissionForResult(
    permission: List<String>
): Map<String, Boolean> {
    val linkedHashMap = LinkedHashMap<String, Boolean>()
    for (per in permission) {
        linkedHashMap[per] = ContextCompat.checkSelfPermission(
            this,
            per
        ) == PackageManager.PERMISSION_GRANTED
    }
    return linkedHashMap
}