package com.xxf.utils


import android.Manifest
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Process
import android.text.TextUtils
import androidx.annotation.RequiresPermission
import com.xxf.application.allActivity
import java.io.File
import kotlin.system.exitProcess

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  获取全局的context
 * @date createTime：2018/9/7
 */

/**
 * 安装
 */
@RequiresPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
fun Application.installApp(file: File){
    this.startActivity(IntentUtils.getInstallAppIntent(this,file))
}

/**
 * 卸载
 */
@RequiresPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
fun Application.installApp(uri: Uri){
    this.startActivity(IntentUtils.getInstallAppIntent(uri))
}

/**
 * 卸载
 */
@RequiresPermission(Manifest.permission.REQUEST_DELETE_PACKAGES)
fun Application.uninstallApp(packageName:String=this.packageName){
    this.startActivity(IntentUtils.getUninstallAppIntent(packageName))
}

/**
 * 是否安装
 */
fun Application.isAppInstalled(packageName:String=this.packageName): Boolean {
    return try {
        this.packageManager.getApplicationInfo(packageName, 0).enabled;
    } catch (e:PackageManager.NameNotFoundException) {
        false;
    }
}

/**
 * 判断 App 是否是 Debug 版本
 */
fun Application.isAppDebug(packageName: String=this.packageName): Boolean {
    return try {
        val ai = this.packageManager.getApplicationInfo(packageName, 0)
        ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        false
    }
}

/**
 * 判断 App 是否是系统应用
 */
fun Application.isAppSystem(packageName: String=this.packageName): Boolean {
    return try {
        val ai = this.packageManager.getApplicationInfo(packageName, 0)
        ai.flags and ApplicationInfo.FLAG_SYSTEM != 0
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        false
    }
}


/**
 * 启动app
 */
fun Application.launchApp(packageName: String=this.packageName):Boolean {
    val launchAppIntent =IntentUtils.getLaunchAppIntent(this,packageName)
    if(launchAppIntent!=null) {
        this.startActivity(launchAppIntent)
    }
    return launchAppIntent!=null
}

/**
 * 重启app
 */
fun Application.relaunchApp(){
    val intent =
        this.packageManager.getLaunchIntentForPackage(this.packageName)
    intent!!.addFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_CLEAR_TASK
    )
    applicationContext.startActivity(intent)
    Process.killProcess(Process.myPid())
    System.exit(0)
}

/***
 * 重置应用
 */
fun Application.resetApp(){
    val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
    am?.clearApplicationUserData();
}


/**
 * 退出应用
 */
fun Application.exitApp(){
   allActivity.forEach {
       it.finish()
   }
    exitProcess(0)
}


/**
 * 启动app 设置页面
 */
fun Application.launchAppDetailsSettings(packageName: String=this.packageName) {
    val intent: Intent = IntentUtils.getLaunchAppDetailsSettingsIntent(packageName, true)
    if (!IntentUtils.isIntentAvailable(this,intent)) return
    this.startActivity(intent)
}

/**
 * 获取 App 图标
 */
fun Application.getAppIcon(packageName: String=this.packageName): Drawable? {
    return try {
        val pm: PackageManager = this.packageManager
        val pi = pm.getPackageInfo(packageName, 0)
        pi?.applicationInfo?.loadIcon(pm)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        null
    }
}

/**
 * 获取 App 名称
 */
fun Application.getAppName(packageName: String=this.packageName): String {
    return  try {
        val pm: PackageManager = this.packageManager
        val pi = pm.getPackageInfo(packageName, 0)
        pi?.applicationInfo?.loadLabel(pm)?.toString() ?: ""
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ""
    }
}


/**
 * 获取 App 版本名
 */
fun Application.getAppVersionName(packageName: String=this.packageName): String {
    return try {
        val pm: PackageManager = this.packageManager
        val pi = pm.getPackageInfo(packageName, 0)
        if (pi == null) "" else pi.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ""
    }
}


/**
 * 获取 App 版本号
 */
fun Application.getAppVersionCode(packageName: String=this.packageName): Int {
    return try {
        val pm: PackageManager = this.getPackageManager()
        val pi = pm.getPackageInfo(packageName!!, 0)
        pi?.versionCode ?: -1
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        -1
    }
}

/**
 * 获取 App 签名
 */
fun Application.getAppSignatures(packageName: String=this.packageName): Array<Signature>? {
    return try {
        val pm: PackageManager = this.getPackageManager()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val pi = pm.getPackageInfo(packageName!!, PackageManager.GET_SIGNING_CERTIFICATES)
                ?: return null
            val signingInfo = pi.signingInfo
            if (signingInfo.hasMultipleSigners()) {
                signingInfo.apkContentsSigners
            } else {
                signingInfo.signingCertificateHistory
            }
        } else {
            val pi = pm.getPackageInfo(packageName!!, PackageManager.GET_SIGNATURES)
                ?: return null
            pi.signatures
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        null
    }
}

/**
 * 获取应用签名的的 SHA1 值
 */
fun Application.getAppSignaturesSHA1(packageName: String=this.packageName): List<String>? {
    return this.getAppSignaturesHash(packageName, "SHA1")
}

/**
 * 获取应用签名的的 SHA256 值
 */
fun Application.getAppSignaturesSHA256(packageName: String=this.packageName): List<String>? {
    return this.getAppSignaturesHash(packageName, "SHA256")
}

/**
 * 获取应用签名的的 MD5 值
 */
fun Application.getAppSignaturesMD5(packageName: String=this.packageName): List<String>? {
    return this.getAppSignaturesHash(packageName, "MD5")
}


private fun Application.getAppSignaturesHash(
    packageName: String=this.packageName,
    algorithm: String
): MutableList<String>? {
    val result = ArrayList<String>()
    if (TextUtils.isEmpty(packageName)) return result
    val signatures: Array<Signature>? = this.getAppSignatures(packageName)
    if (signatures.isNullOrEmpty()) return result
    for (signature in signatures) {
        val hash: String = EncryptUtils.bytes2HexString(
            EncryptUtils.hashTemplate(
                signature.toByteArray(),
                algorithm
            )
        )
            .replace("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0")
        result.add(hash)
    }
    return result
}

/**
 * 判断应用是否首次安装
 */
fun Application.isFirstTimeInstalled(packageName: String=this.packageName): Boolean {
    return try {
        val pi: PackageInfo =
            this.packageManager.getPackageInfo(packageName, 0)
        pi.firstInstallTime == pi.lastUpdateTime
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        true
    }
}