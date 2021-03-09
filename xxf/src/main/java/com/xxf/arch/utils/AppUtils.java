package com.xxf.arch.utils;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import com.xxf.arch.XXF;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description apk 工具类
 * @date createTime：2018/9/7
 */
public class AppUtils {
    /**
     * app 是否安装
     *
     * @param pkgName
     * @return
     */
    public static boolean isAppInstalled(final String pkgName) {
        if (TextUtils.isEmpty(pkgName)) return false;
        PackageManager pm = XXF.getApplication().getPackageManager();
        try {
            return pm.getApplicationInfo(pkgName, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 检查apk的签名是否一致
     *
     * @param application
     * @param apkSignatureMd5
     */
    public static void appSignatureCheck(Application application, String apkSignatureMd5) {
        if (!TextUtils.equals(getApkSignatureMd5(application), apkSignatureMd5)) {
            throw new RuntimeException("signature is not right");
        }
    }

    /**
     * 获取apk的签名
     *
     * @param application
     * @return
     */
    public static String getApkSignatureMd5(Application application) {
        try {
            PackageInfo packageInfo = application.getPackageManager().getPackageInfo(application.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            return EncryptUtils.encryptMD5ToString(signatures[0].toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
