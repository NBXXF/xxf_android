package com.xxf.arch.utils;

import android.util.Log;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-05-05 10:38
 */
public class XXFLogUtils {
    public static boolean isLoggable = true;
    private static final String TAG = "XXFLogUtils";

    public static void d(String msg) {
        if (isLoggable) {
            d(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isLoggable) {
            Log.d(tag, msg);
        }
    }

    public static void e(String msg) {
        if (isLoggable) {
            e(TAG, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isLoggable) {
            Log.e(tag, msg);
        }
    }

    public static void w(String msg) {
        if (isLoggable) {
            w(TAG, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isLoggable) {
            Log.w(tag, msg);
        }
    }

    public static void i(String msg) {
        if (isLoggable) {
            i(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isLoggable) {
            Log.i(tag, msg);
        }
    }
}
