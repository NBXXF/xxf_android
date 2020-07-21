package com.xxf.arch.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;

import com.xxf.arch.R;
import com.xxf.arch.XXF;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Description
 * * <p>
 * 【不建议使用 不建议使用】
 * <p>
 * 某些系统可能屏蔽通知
 * 1:检查 SystemUtils.isEnableNotification(BaseApplication.getApplication());
 * 2:替代方案 SnackbarUtils.showSnack(topActivity, noticeStr);
 * <p>
 * <p>
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/4/27
 * version 1.0.0
 */
public class ToastUtils {
    public enum ToastType {
        NORMAL,
        ERROR,
        SUCCESS;
    }

    private static Field sField_TN;
    private static Field sField_TN_Handler;

    private ToastUtils() {
    }

    /**
     * 通过反射封装 Toast 类中TN Binder内部类中的handler,
     * 捕获BadTokenException, 解决Android API 25 引入的
     * Bug
     */
    static {
        try {
            sField_TN = Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
        } catch (Exception e) {
        }
    }

    public static void hook(Toast toast) {
        try {
            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandlerWrapper(preHandler));
        } catch (Exception e) {
        }
    }


    private static class SafelyHandlerWrapper extends Handler {

        private Handler impl;

        public SafelyHandlerWrapper(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
            }
        }

        @Override
        public void handleMessage(Message msg) {
            impl.handleMessage(msg);//需要委托给原Handler执行
        }
    }


    private static Context getContext() {
        return XXF.getApplication();
    }

    /**
     * toast是否可用
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isToastAvailable() {
        try {
            final String CHECK_OP_NO_THROW = "checkOpNoThrow";
            final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
            AppOpsManager mAppOps = (AppOpsManager) getContext().getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = getContext().getApplicationInfo();
            String pkg = getContext().getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            Class appOpsClass = null;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    appOpsClass = Class.forName(AppOpsManager.class.getName());
                    Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
                    Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                    int value = (int) opPostNotificationValue.get(Integer.class);
                    return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {
        }
        return true;
    }

    /**
     * 校验线程
     * 否则:Can't create handler inside thread that has not called Looper.prepare()
     *
     * @param notice
     * @return
     */
    @UiThread
    @Nullable
    public static Toast showToast(@StringRes int notice, @NonNull ToastType type) {
        return showToast(XXF.getApplication().getString(notice), type);
    }

    /**
     * 校验线程
     * 否则:Can't create handler inside thread that has not called Looper.prepare()
     *
     * @param notice
     * @return
     */
    @UiThread
    @Nullable
    public static Toast showToast(@NonNull CharSequence notice, @NonNull ToastType type) {
        if (!isMainThread() || TextUtils.isEmpty(notice)) {
            return null;
        }
        //app 后台不允许toast
        if (XXF.getActivityStackProvider().isBackground()) {
            return null;
        }
        Toast toast = createToast(notice, type);
        //fix bug #65709 BadTokenException from BugTags
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            hook(toast);
        }
        toast.show();
        return toast;
    }

    public static Toast createToast(CharSequence msg, ToastType type) {
        LayoutInflater inflater = LayoutInflater.from(XXF.getApplication());
        View view = inflater.inflate(R.layout.xxf_toast_layout, null);

        TextView text = view.findViewById(android.R.id.message);
        int dp19 = DensityUtil.dip2px(19);
        switch (type) {
            case ERROR:
                Drawable errorDrawable = XXF.getApplication().getDrawable(R.drawable.xxf_ic_toast_error);
                errorDrawable.setBounds(0, 0, dp19, dp19);
                text.setCompoundDrawables(errorDrawable, null, null, null);
                break;
            case NORMAL:
                text.setCompoundDrawables(null, null, null, null);
                break;
            case SUCCESS:
                Drawable successDrawable = XXF.getApplication().getDrawable(R.drawable.xxf_ic_toast_success);
                successDrawable.setBounds(0, 0, dp19, dp19);
                text.setCompoundDrawables(successDrawable, null, null, null);
                break;
        }
        text.setText(msg);
        Toast toast = new Toast(XXF.getApplication());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        return toast;
    }


    /**
     * 检查是否在主线程showToast
     *
     * @return
     */
    private static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
