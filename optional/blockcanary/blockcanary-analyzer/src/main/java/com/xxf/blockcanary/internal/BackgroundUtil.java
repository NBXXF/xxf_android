package com.xxf.blockcanary.internal;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 判断app是否在前台
 * @author : BaoZhou
 * @date : 2020/4/22 11:53 AM
 */
public class BackgroundUtil {
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses != null && appProcesses.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(context.getPackageName())) {
                    if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }
}
