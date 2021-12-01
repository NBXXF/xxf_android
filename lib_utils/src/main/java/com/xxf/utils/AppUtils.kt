package com.xxf.utils

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description apk 工具类
 * @date createTime：2018/9/7
 */
object AppUtils {
    /**
     * app 是否安装
     *
     * @param pkgName
     * @return
     */
    fun isAppInstalled(context: Context, pkgName: String?): Boolean {
        if (TextUtils.isEmpty(pkgName)) return false
        val pm = context.applicationContext.packageManager
        return try {
            pm.getApplicationInfo(pkgName?:"", 0).enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * 检查apk的签名是否一致
     *
     * @param application
     * @param apkSignatureMd5
     */
    fun appSignatureCheck(application: Application, apkSignatureMd5: String?) {
        if (!TextUtils.equals(getApkSignatureMd5(application), apkSignatureMd5)) {
            throw RuntimeException("signature is not right")
        }
    }

    /**
     * 获取apk的签名
     *
     * @param application
     * @return
     */
    fun getApkSignatureMd5(application: Application): String? {
        try {
            val packageInfo = application.packageManager.getPackageInfo(application.packageName, PackageManager.GET_SIGNATURES)
            val signatures = packageInfo.signatures
            return EncryptUtils.encryptMD5ToString(signatures[0].toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}