package com.xxf.permission.common;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

/**
 * 权限跳转到app 详情设置页面
 */
public class JumpPermissionManagement {
    /**
     * Build.MANUFACTURER
     */
    private static final String MANUFACTURER_HUAWEI = "HUAWEI";//华为
    private static final String MANUFACTURER_MEIZU = "MEIZU";//魅族
    private static final String MANUFACTURER_XIAOMI = "XIAOMI";//小米
    private static final String MANUFACTURER_SONY = "SONY";//索尼
    private static final String MANUFACTURER_OPPO = "OPPO";
    private static final String MANUFACTURER_LG = "LG";
    private static final String MANUFACTURER_VIVO = "VIVO";
    private static final String MANUFACTURER_SAMSUNG = "SAMSUNG";//三星
    private static final String MANUFACTURER_LETV = "LETV";//乐视
    private static final String MANUFACTURER_ZTE = "ZTE";//中兴
    private static final String MANUFACTURER_YULONG = "YULONG";//酷派
    private static final String MANUFACTURER_LENOVO = "LENOVO";//联想
    private static final String MANUFACTURER_SMARTISAN = "SMARTISAN";//锤子

    /**
     * 此函数可以自己定义
     * @param activity
     */
    public static void goToSetting(Activity activity){
        try{
            switch (Build.MANUFACTURER.toUpperCase()){
                case MANUFACTURER_HUAWEI:
                    Huawei(activity);
                    break;
                case MANUFACTURER_MEIZU:
                    Meizu(activity);
                    break;
                case MANUFACTURER_XIAOMI:
                    Xiaomi(activity);
                    break;
                case MANUFACTURER_SONY:
                    Sony(activity);
                    break;
                case MANUFACTURER_OPPO:
                    OPPO(activity);
                    break;
                case MANUFACTURER_LG:
                    LG(activity);
                    break;
                case MANUFACTURER_LETV:
                    Letv(activity);
                    break;
                case MANUFACTURER_SMARTISAN:
                    Smartisan(activity);
                    break;
                default:
                    ApplicationInfo(activity);
                    Log.e("goToSetting", "目前暂不支持此系统");
                    break;
            }
        }catch (Throwable e){
            try{
                ApplicationInfo(activity);
            }catch (Throwable ex){
                ex.printStackTrace();
                SystemConfig(activity);
            }
        }
    }

    private static void Smartisan(Context activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", activity.getPackageName());
        ComponentName comp = new ComponentName("com.smartisanos.security", "com.smartisanos.security.MainActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    private static void Huawei(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", activity.getPackageName());
        ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    private static void Meizu(Activity activity) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", activity.getPackageName());
        activity.startActivity(intent);
    }

    // public static void Xiaomi(Context activity) {
    //     Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
    //     ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
    //     intent.setComponent(componentName);
    //     intent.putExtra("extra_pkgname", activity.getPackageName());
    //     activity.startActivity(intent);
    // }

    private static void Xiaomi(Activity activity) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.setComponent(componentName);
        intent.putExtra("extra_pkgname", activity.getPackageName());
        activity.startActivity(intent);
    }

    private static void Sony(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", activity.getPackageName());
        ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    private static void OPPO(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", activity.getPackageName());
        ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    private static void LG(Activity activity) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", activity.getPackageName());
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    private static void Letv(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", activity.getPackageName());
        ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    /**
     * 只能打开到自带安全软件
     * @param activity
     */
    private static void _360(Activity activity) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", activity.getPackageName());
        ComponentName comp = new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    /**
     * 应用信息界面
     * @param activity
     */
    private static void ApplicationInfo(Activity activity){
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        activity.startActivity(localIntent);
    }

    /**
     * 系统设置界面
     * @param activity
     */
    private static void SystemConfig(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivity(intent);
    }
}