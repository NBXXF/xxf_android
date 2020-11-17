package com.xxf.arch.utils;

import android.content.Context;

import com.xxf.arch.XXF;

/**
 * Description 单位换算工具
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/6/23
 * version
 */
public class DensityUtil {


    private static Context getContext() {
        return XXF.getApplication();
    }

    public static int getScreenHeightPx() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidthPx() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }


    public static int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(float spValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }
}
