package com.xxf.view.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

/**
 * Description
 * version 2.3.0
 */
public class StatusBarUtils {

    /**
     * 适配状态栏holder view
     * level >= 23
     *
     * @param compatStatusBarHolder holder view
     */
    public static void compatStatusBarHolderViewForM(final View compatStatusBarHolder) {
        try {

            if (compatStatusBarHolder == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                compatStatusBarHolder.setVisibility(View.VISIBLE);
                final ViewGroup.LayoutParams lp = compatStatusBarHolder.getLayoutParams();
                if (lp != null) {
                    lp.height = getSystemStatusBarHeight(compatStatusBarHolder.getContext());
                    if (lp.height != 0) {
                        compatStatusBarHolder.setLayoutParams(lp);
                    } else {
                        // 初始读不到高度的情况下延迟读取
                        compatStatusBarHolder.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                compatStatusBarHolder.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                lp.height = getSystemStatusBarHeight(compatStatusBarHolder.getContext());
                                compatStatusBarHolder.setLayoutParams(lp);
                            }
                        });
                    }
                }
            } else {
                compatStatusBarHolder.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // compat for android P(27)
    private static int getSystemStatusBarHeight(Context context) {
        if (context == null) {
            return 0;
        }
        int statusBarHeight;
        //获取status_bar_height资源的ID
        Resources resources = context.getResources();
        if (resources == null) {
            statusBarHeight = 0;
        } else {
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight = resources.getDimensionPixelSize(resourceId);
            } else {
                statusBarHeight = (int) (resources.getDisplayMetrics().density * 25);
            }
        }
        return statusBarHeight;
    }

    /**
     * 适配状态栏
     * level >= 23
     *
     * @param activity       页面
     * @param fullscreen     是否全屏
     * @param statusBarColor 状态栏颜色
     */
    public static void compatStatusBarForM(Activity activity, boolean fullscreen, int statusBarColor) {
        try {


            if (activity == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    Window window = activity.getWindow();

                    // 在魅族手机上，code1必须在code2之前，否则会出现奇怪问题，其他手机没这个问题
                    // code 1
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                if (fullscreen) {
//                    WindowManager.LayoutParams lp = window.getAttributes();
//                    lp.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//                    window.setAttributes(lp);
//                }
                    window.setStatusBarColor(statusBarColor);

                    // code 2
                    View decorView = window.getDecorView();
                    if (decorView != null) {
                        int ui = decorView.getSystemUiVisibility();
                        ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                        if (fullscreen) {
                            ui |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                        }
                        decorView.setSystemUiVisibility(ui);
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void compatMeizu(Window window) {
        if (window != null && "meizu".equalsIgnoreCase(Build.MANUFACTURER)) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setAttributes(lp);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 改变状态栏亮暗
     * <p>
     * 亮色[背景]的状态栏，那么字将是黑色；
     * <p>
     * 暗色[背景]的状态栏，那么字将是白色
     *
     * @param activity 目标activity
     * @param light    是否是亮色，true代表亮色，false代表暗色
     */
    public static void compatStatusBarLightnessForM(Activity activity, boolean light) {
        try {
            if (activity == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    Window window = activity.getWindow();
                    compatMeizu(window);
                    View decor;
                    if (window != null && (decor = window.getDecorView()) != null) {
                        int ui = decor.getSystemUiVisibility();
                        if (light) {
                            ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                        } else {
                            ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                        }
                        decor.setSystemUiVisibility(ui);
                    }

                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
