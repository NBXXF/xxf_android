package com.xxf.view.utils;

import android.view.MotionEvent;
import android.view.View;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-11-10 17:50
 * 防爆点击
 */

public class RAUtils {
    /**
     * 记录一个路由上次跳转的时间
     */
    private static final Map<String, Long> routerJumpRecordLastTimes = new ConcurrentHashMap<>();

    private RAUtils() {
    }

    public static final long DURATION_DEFAULT = 500;

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
        return isLegal(RAUtils.class.getName(), duration);
    }

    public static boolean isLegal(String path, long duration) {
        Long lastRecord = routerJumpRecordLastTimes.get(path);
        long current = System.currentTimeMillis();
        routerJumpRecordLastTimes.put(path, current);
        return lastRecord == null || System.currentTimeMillis() - lastRecord >= duration;
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
