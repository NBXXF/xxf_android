package com.xxf.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/2
 * Description ://权限拓展
 */
inline fun <reified T : FragmentActivity> T.requestPermissionsObservable(
    permissions: Array<String>,
    requestCode: Int = 100
): Observable<Boolean> {
    return Observable.defer {
        RxPermissions(this).request(*permissions)
    }.subscribeOn(AndroidSchedulers.mainThread())
}

inline fun <reified T : FragmentActivity> T.requestPermissionsObservable(
    permission: String,
    requestCode: Int = 100
): Observable<Boolean> {
    return Observable.defer {
        RxPermissions(this).request(permission)
    }.subscribeOn(AndroidSchedulers.mainThread())
}

inline fun <reified T : Fragment> T.requestPermissionsObservable(
    permissions: Array<String>,
    requestCode: Int = 100
): Observable<Boolean> {
    return Observable.defer {
        RxPermissions(this).request(*permissions)
    }.subscribeOn(AndroidSchedulers.mainThread())
}

inline fun <reified T : Fragment> T.requestPermissionsObservable(
    permission: String,
    requestCode: Int = 100
): Observable<Boolean> {
    return Observable.defer {
        RxPermissions(this).request(permission)
    }.subscribeOn(AndroidSchedulers.mainThread())
}

/**
 * 是否授予权限
 */
inline fun <reified T : Activity> T.isGrantedPermission(
    permission: String
): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

/**
 * 是否授予权限
 */
inline fun <reified T : Fragment> T.isGrantedPermission(
    permission: String
): Boolean {
    return ContextCompat.checkSelfPermission(this.requireContext(), permission) ==
            PackageManager.PERMISSION_GRANTED
}