package com.xxf.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static android.Manifest.permission.WRITE_SETTINGS;

import com.xxf.application.initializer.ApplicationInitializer;


/**
 * 屏幕的工具类
 * getScreenWidth     : 获取屏幕的宽度（单位：px）
 * getScreenHeight    : 获取屏幕的高度（单位：px）
 * getAppScreenWidth  : 获取应用屏幕的宽度（单位：px）
 * getAppScreenHeight : 获取应用屏幕的高度（单位：px）
 * getScreenDensity   : 获取屏幕密度
 * getScreenDensityDpi: 获取屏幕密度 DPI
 * setFullScreen      : 设置屏幕为全屏
 * setNonFullScreen   : 设置屏幕为非全屏
 * toggleFullScreen   : 切换屏幕为全屏与否状态
 * isFullScreen       : 判断屏幕是否为全屏
 * setLandscape       : 设置屏幕为横屏
 * setPortrait        : 设置屏幕为竖屏
 * isLandscape        : 判断是否横屏
 * isPortrait         : 判断是否竖屏
 * getScreenRotation  : 获取屏幕旋转角度
 * screenShot         : 截屏
 * isScreenLock       : 判断是否锁屏
 * setSleepDuration   : 设置进入休眠时长
 * getSleepDuration   : 获取进入休眠时长
 */
public final class ScreenUtils {

    private ScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 格子布局
     *
     * @param parentWidth      父容器宽度
     * @param gridExpectedSize 最小格子宽度 px
     * @return
     */
    public static int spanCount(int parentWidth, int gridExpectedSize) {
        int screenWidth = parentWidth;
        int spanCount = screenWidth / gridExpectedSize;
        if (spanCount <= 0) {
            spanCount = 1;
        }
        return spanCount;
    }

    public static Point getScreenSize() {
        WindowManager wm = (WindowManager) ApplicationInitializer.applicationContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return new Point(-1,-1);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point;
    }

    /**
     * Return the width of screen, in pixel.
     *
     * @return the width of screen, in pixel
     */
    public static int getScreenWidth() {
        return getScreenSize().x;
    }

    /**
     * Return the height of screen, in pixel.
     *
     * @return the height of screen, in pixel
     */
    public static int getScreenHeight() {
        return getScreenSize().y;
    }

    /**
     * Return the application's width of screen, in pixel.
     *
     * @return the application's width of screen, in pixel
     */
    public static int getAppScreenWidth() {
        WindowManager wm = (WindowManager) ApplicationInitializer.applicationContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    /**
     * Return the application's height of screen, in pixel.
     *
     * @return the application's height of screen, in pixel
     */
    public static int getAppScreenHeight() {
        WindowManager wm = (WindowManager) ApplicationInitializer.applicationContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

    /**
     * Return the density of screen.
     *
     * @return the density of screen
     */
    public static float getScreenDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * Return the screen density expressed as dots-per-inch.
     *
     * @return the screen density expressed as dots-per-inch
     */
    public static int getScreenDensityDpi() {
        return Resources.getSystem().getDisplayMetrics().densityDpi;
    }

    /**
     * Return the exact physical pixels per inch of the screen in the Y dimension.
     *
     * @return the exact physical pixels per inch of the screen in the Y dimension
     */
    public static float getScreenXDpi() {
        return Resources.getSystem().getDisplayMetrics().xdpi;
    }

    /**
     * Return the exact physical pixels per inch of the screen in the Y dimension.
     *
     * @return the exact physical pixels per inch of the screen in the Y dimension
     */
    public static float getScreenYDpi() {
        return Resources.getSystem().getDisplayMetrics().ydpi;
    }

    /**
     * Return the distance between the given View's X (start point of View's width) and the screen width.
     *
     * @return the distance between the given View's X (start point of View's width) and the screen width.
     */
    public int calculateDistanceByX(View view) {
        int[] point = new int[2];
        view.getLocationOnScreen(point);
        return getScreenWidth() - point[0];
    }

    /**
     * Return the distance between the given View's Y (start point of View's height) and the screen height.
     *
     * @return the distance between the given View's Y (start point of View's height) and the screen height.
     */
    public int calculateDistanceByY(View view) {
        int[] point = new int[2];
        view.getLocationOnScreen(point);
        return getScreenHeight() - point[1];
    }

    /**
     * Return the X coordinate of the given View on the screen.
     *
     * @return X coordinate of the given View on the screen.
     */
    public int getViewX(View view) {
        int[] point = new int[2];
        view.getLocationOnScreen(point);
        return point[0];
    }

    /**
     * Return the Y coordinate of the given View on the screen.
     *
     * @return Y coordinate of the given View on the screen.
     */
    public int getViewY(View view) {
        int[] point = new int[2];
        view.getLocationOnScreen(point);
        return point[1];
    }

    /**
     * Set full screen.
     *
     * @param activity The activity.
     */
    public static void setFullScreen(@NonNull final Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Set non full screen.
     *
     * @param activity The activity.
     */
    public static void setNonFullScreen(@NonNull final Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Toggle full screen.
     *
     * @param activity The activity.
     */
    public static void toggleFullScreen(@NonNull final Activity activity) {
        boolean isFullScreen = isFullScreen(activity);
        Window window = activity.getWindow();
        if (isFullScreen) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * Return whether screen is full.
     *
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isFullScreen(@NonNull final Activity activity) {
        int fullScreenFlag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        return (activity.getWindow().getAttributes().flags & fullScreenFlag) == fullScreenFlag;
    }

    /**
     * Set the screen to landscape.
     *
     * @param activity The activity.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    public static void setLandscape(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * Set the screen to portrait.
     *
     * @param activity The activity.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    public static void setPortrait(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Return whether screen is landscape.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isLandscape() {
        return ApplicationInitializer.applicationContext.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Return whether screen is portrait.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isPortrait() {
        return ApplicationInitializer.applicationContext.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * Return the rotation of screen.
     *
     * @param activity The activity.
     * @return the rotation of screen
     */
    public static int getScreenRotation(@NonNull final Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }

    /**
     * Return whether screen is locked.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isScreenLock() {
        KeyguardManager km =
                (KeyguardManager) ApplicationInitializer.applicationContext.getSystemService(Context.KEYGUARD_SERVICE);
        if (km == null) return false;
        return km.inKeyguardRestrictedInputMode();
    }

    /**
     * Set the duration of sleep.
     * <p>Must hold {@code <uses-permission android:name="android.permission.WRITE_SETTINGS" />}</p>
     *
     * @param duration The duration.
     */
    @RequiresPermission(WRITE_SETTINGS)
    public static void setSleepDuration(final int duration) {
        Settings.System.putInt(
                ApplicationInitializer.applicationContext.getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT,
                duration
        );
    }

    /**
     * Return the duration of sleep.
     *
     * @return the duration of sleep.
     */
    public static int getSleepDuration() {
        try {
            return Settings.System.getInt(
                    ApplicationInitializer.applicationContext.getContentResolver(),
                    Settings.System.SCREEN_OFF_TIMEOUT
            );
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return -123;
        }
    }
}