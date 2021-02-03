package com.xxf.arch.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/3/21 2:36 PM
 * Description: handler utils
 */
public class HandlerUtils {
    private static volatile Handler MainHandler = new Handler(Looper.getMainLooper());

    /**
     * 获取主线程的handler
     * @return
     */
    @NonNull
    public static Handler getMainHandler() {
        return MainHandler;
    }

    /**
     * 检查是否在主线程showToast
     *
     * @return
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
