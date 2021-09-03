package com.xxf.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.lang.IllegalArgumentException


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/2
 * Description ://权限拓展  支持activity fragment LifecycleOwner 挂载对象访问
 */

/**
 * activity 请求权限 可以结合内敛函数topFragmentActivity
 * @param permissions 可以参考 {@link #android.Manifest.permission}  android.Manifest.permission
 */
inline fun <reified T : FragmentActivity> T.requestPermissionsObservable(
    permissions: Array<String>,
    requestCode: Int = 100
): Observable<Boolean> {

    return Observable.defer {
        RxPermissions(this).request(*permissions)
    }.subscribeOn(AndroidSchedulers.mainThread())
}

/**
 * activity 请求权限  可以结合内敛函数topFragmentActivity
 * @param permission 可以参考 {@link #android.Manifest.permission}  android.Manifest.permission
 */
inline fun <reified T : FragmentActivity> T.requestPermissionsObservable(
    permission: String,
    requestCode: Int = 100
): Observable<Boolean> {
    return Observable.defer {
        RxPermissions(this).request(permission)
    }.subscribeOn(AndroidSchedulers.mainThread())
}

/**
 * fragment 请求权限  可以结合内敛函数topFragmentActivity
 * @param permissions 可以参考 {@link #android.Manifest.permission}  android.Manifest.permission
 */
inline fun <reified T : Fragment> T.requestPermissionsObservable(
    permissions: Array<String>,
    requestCode: Int = 100
): Observable<Boolean> {
    return Observable.defer {
        RxPermissions(this).request(*permissions)
    }.subscribeOn(AndroidSchedulers.mainThread())
}

/**
 * fragment 请求权限
 * @param permission 可以参考 {@link #android.Manifest.permission}  android.Manifest.permission
 */
inline fun <reified T : Fragment> T.requestPermissionsObservable(
    permission: String,
    requestCode: Int = 100
): Observable<Boolean> {
    return Observable.defer {
        RxPermissions(this).request(permission)
    }.subscribeOn(AndroidSchedulers.mainThread())
}


/**
 * Lifecycle 请求权限   可以结合内敛函数topFragmentActivity
 * @param permissions 可以参考 {@link #android.Manifest.permission}  android.Manifest.permission
 */
inline fun <reified T : LifecycleOwner> T.requestPermissionsObservable(
    permissions: Array<String>,
    requestCode: Int = 100
): Observable<Boolean> {
    return Observable.defer {
        if (this is FragmentActivity) {
            return@defer RxPermissions(this).request(*permissions)
        } else if (this is Fragment) {
            return@defer RxPermissions(this).request(*permissions)
        }
        return@defer Observable.error(IllegalArgumentException("不支持的类型!"))
    }.subscribeOn(AndroidSchedulers.mainThread())
}

/**
 * Lifecycle 请求权限 可以结合内敛函数topFragmentActivity
 * @param permission 可以参考 {@link #android.Manifest.permission}  android.Manifest.permission
 */
inline fun <reified T : LifecycleOwner> T.requestPermissionsObservable(
    permission: String,
    requestCode: Int = 100
): Observable<Boolean> {
    return Observable.defer {
        if (this is FragmentActivity) {
            return@defer RxPermissions(this).request(permission)
        } else if (this is Fragment) {
            return@defer RxPermissions(this).request(permission)
        }
        return@defer Observable.error(IllegalArgumentException("不支持的类型!"))
    }.subscribeOn(AndroidSchedulers.mainThread())
}

/**
 * Lifecycle 判断是否授予权限 可以结合内敛函数topFragmentActivity
 */
inline fun <reified T : LifecycleOwner> T.isGrantedPermission(
    permission: String
): Boolean {
    if (this is FragmentActivity) {
        return ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED
    } else if (this is Fragment) {
        return ContextCompat.checkSelfPermission(this.requireContext(), permission) ==
                PackageManager.PERMISSION_GRANTED
    }
    throw  IllegalArgumentException("不支持的类型!")
}

/**
 * context 判断是否授予权限  可以结合内敛函数topFragmentActivity
 * @param permission 可以参考 {@link #android.Manifest.permission}  android.Manifest.permission
 */
inline fun <reified T : Context> T.isGrantedPermission(
    permission: String
): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

/**
 * activity 判断是否授予权限 可以结合内敛函数topFragmentActivity
 * @param permission 可以参考 {@link #android.Manifest.permission}  android.Manifest.permission
 */
inline fun <reified T : Activity> T.isGrantedPermission(
    permission: String
): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

/**
 * fragment 判断是否授予权限  可以结合内敛函数topFragmentActivity
 * @param permission 可以参考 {@link #android.Manifest.permission}  android.Manifest.permission
 */
inline fun <reified T : Fragment> T.isGrantedPermission(
    permission: String
): Boolean {
    return ContextCompat.checkSelfPermission(this.requireContext(), permission) ==
            PackageManager.PERMISSION_GRANTED
}