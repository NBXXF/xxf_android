package com.xxf.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.xxf.activityresult.startActivityForResult
import io.reactivex.rxjava3.core.Observable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/2
 * Description ://权限拓展  支持activity fragment LifecycleOwner 挂载对象访问
 */

/**
 * activity 请求权限 可以结合内敛函数topFragmentActivity
 * @param permissions 可以参考 [android.Manifest.permission]  android.Manifest.permission
 */
inline fun <reified T : LifecycleOwner> T.requestPermission(
    permissions: Array<String>
): Observable<Boolean> {
    return this.startActivityForResult(ActivityResultContracts.RequestMultiplePermissions(),permissions)
        .map {
            it.values.all { it }
        }
}


/**
 * fragment 请求权限  可以结合内敛函数topFragmentActivity
 * @param permission 可以参考 [android.Manifest.permission]  android.Manifest.permission
 */
inline fun <reified T : LifecycleOwner> T.requestPermission(
    permission: String,
): Observable<Boolean> {
    return this.startActivityForResult(ActivityResultContracts.RequestPermission(),permission)
}



/**
 * context 判断是否授予权限  可以结合内敛函数topFragmentActivity
 * @param permission 可以参考 [android.Manifest.permission]  android.Manifest.permission
 */
inline fun <reified T : Context> T.isGrantedPermission(
    permission: String
): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

/**
 * fragment 判断是否授予权限  可以结合内敛函数topFragmentActivity
 * @param permission 可以参考 [android.Manifest.permission]  android.Manifest.permission
 */
inline fun <reified T : Fragment> T.isGrantedPermission(
    permission: String
): Boolean {
    return ContextCompat.checkSelfPermission(this.requireContext(), permission) ==
            PackageManager.PERMISSION_GRANTED
}