package com.xxf.view.utils;

import android.view.MotionEvent;
import android.view.View;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-11-10 17:50
 * 防爆点击
 */

public class RAUtils {

    private RAUtils() {
    }

    public static final long DURATION_DEFAULT = 350;
    private static long lastClickTime;

    public static boolean isLegalDefault() {
        return RAUtils.isLegal(RAUtils.DURATION_DEFAULT);
    }

    /**
     * 防爆 阻力 false 表示暴力点击
     *
     * @param duration 点击间隔
     * @return
     */
    public static boolean isLegal(long duration) {
        long current;
        current = System.currentTimeMillis();
        if (0 == lastClickTime) {
            lastClickTime = current;
            return true;
        } else {
            long distance = current - lastClickTime;
            lastClickTime = current;
            return duration < distance;
        }
    }

    public static boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }
}
