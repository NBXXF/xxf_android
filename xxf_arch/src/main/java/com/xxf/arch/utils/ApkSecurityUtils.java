package com.xxf.arch.utils;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xuanyouwu@163.com
 * @Description apk安全校验
 * @date createTime：2018/9/7
 */
public class ApkSecurityUtils {

    /**
     * 检查apk的签名是否一致
     *
     * @param application
     * @param apkSignatureMd5
     */
    public static void ApkSignatureCheck(Application application, String apkSignatureMd5) {
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
            return encryptionMD5(signatures[0].toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * MD5加密
     *
     * @param byteStr 需要加密的内容
     * @return 返回 byteStr的md5值
     */
    private static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }
}
