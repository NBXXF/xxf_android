package com.xxf.arch.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import androidx.core.app.NotificationManagerCompat;

import com.xxf.application.ApplicationProvider;
import com.xxf.arch.R;
import com.xxf.arch.XXF;
import com.xxf.utils.DensityUtil;
import com.xxf.view.snackbar.Snackbar;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

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
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/4/27
 * version 1.0.0
 */
public class ToastUtils {
    @Nullable
    private static ToastFactory toastFactory;

    public static void setToastFactory(@Nullable ToastFactory toastFactory) {
        ToastUtils.toastFactory = toastFactory;
    }

    public interface ToastFactory {
        /**
         *
         * @param msg
         * @param type
         * @param applicationContext
         * @param flag 任意类型的整形 可以标记toast业务类型 比如位置,也可以根据后端返回的状态码进行颜色 着重文字提示
         *            默认Gravity.CENTER
         *            XXF.bindToErrorNotice 是Integer.MIN_VALUE
         *            flag的定值 可以参考【Gravity】类的枚举值
         *
         * flag 等价于ErrorHandler 返回的flag  XXF.init().setErrorHandler(new BiConsumer<Integer, Throwable>() {
         *                     @Override
         *                     public void accept(Integer flag, Throwable throwable) throws Throwable {
         *                         ToastUtils.showToast("error:" + throwable, ToastUtils.ToastType.ERROR,flag);
         *                     }
         *                 }));
         * @return
         */
        LimitToast createToast(CharSequence msg, ToastType type,Context applicationContext,int flag);
    }

    /**
     * 数量限制的toast 避免栈内挤压过多toast  也能批量取消taost
     */
    public static class LimitToast extends Toast {
        private static int MAX_TOAST = 10;
        private static volatile ArrayList<Toast> toastArrayList = new ArrayList<>();

        public LimitToast(Context context) {
            super(context);
        }

        @Override
        public void show() {
            try {
                //队列里面太多, 取消前面部分
                while (toastArrayList.size() > MAX_TOAST) {
                    Toast toast = toastArrayList.get(0);
                    toastArrayList.remove(toast);
                    if (toast != null) {
                        toast.cancel();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!toastArrayList.contains(this)) {
                    toastArrayList.add(this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.show();
        }

        @Override
        public void cancel() {
            super.cancel();
            try {
                toastArrayList.remove(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 取消所有Toast
         */
        static void cancelAll() {
            try {
                //队列里面太多
                while (toastArrayList.size() > 0) {
                    Toast toast = toastArrayList.get(0);
                    toastArrayList.remove(toast);
                    if (toast != null) {
                        toast.cancel();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public enum ToastType {
        NORMAL,
        ERROR,
        SUCCESS;
    }

    private static Field sField_TN;
    private static Field sField_TN_Handler;
    private static Object iNotificationManagerObj;
    private static CharSequence noticeString;


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
        return ApplicationProvider.applicationContext;
    }

    /**
     * toast是否可用
     *
     * @return
     */
    public static boolean isNotificationEnabled() {
        try {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
            boolean areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled();
            return areNotificationsEnabled;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
        return showToast(getContext().getString(notice), type);
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
         return showToast(notice,type,Gravity.CENTER);
    }
    /**
     * 校验线程
     * 否则:Can't create handler inside thread that has not called Looper.prepare()
     *
     * @param notice
     * @param flag  用于标记业务类型
     * @return
     */
    @UiThread
    @Nullable
    public static Toast showToast(@NonNull CharSequence notice, @NonNull ToastType type,int flag) {
        if (!isMainThread() || TextUtils.isEmpty(notice)) {
            return null;
        }
        //app 后台不允许toast
        if (XXF.getActivityStackProvider().isBackground()) {
            return null;
        }

        /**
         * 全局單個toast賦值
         */
        noticeString = notice;

        LimitToast toast =ToastUtils.toastFactory!=null? ToastUtils.toastFactory.createToast(notice, type,ApplicationProvider.applicationContext,flag): createToast(notice, type);
        //fix bug #65709 BadTokenException from BugTags
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            hook(toast);
        }
        if (isNotificationEnabled()) {
            toast.show();
        } else {
            Activity topActivity = XXF.getActivityStackProvider().getTopActivity();
            showSnackBar(topActivity, notice, type);
            return null;
            /* try {
             *//**
             * hook 一下系统notifycation
             *//*
                showSystemToast(toast, notice, type);
            } catch (Throwable e) {
                e.printStackTrace();
                *//**
             * 再不行 用自定义的顶部snackBar
             *//*
                Activity topActivity = XXF.getActivityStackProvider().getTopActivity();
                showSnackBar(topActivity, notice, type);
                return null;
            }*/
        }
        return toast;
    }

    /**
     * 强制显示系统Toast
     */
    private static void showSystemToast(Toast toast, CharSequence notice, @NonNull ToastType type) throws Throwable {
        @SuppressLint("SoonBlockedPrivateApi") Method getServiceMethod = Toast.class.getDeclaredMethod("getService");
        getServiceMethod.setAccessible(true);
        //hook INotificationManager
        if (iNotificationManagerObj == null) {
            iNotificationManagerObj = getServiceMethod.invoke(null);
            Class iNotificationManagerCls = Class.forName("android.app.INotificationManager");
            Object iNotificationManagerProxy = Proxy.newProxyInstance(toast.getClass().getClassLoader(), new Class[]{iNotificationManagerCls}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    try {

                        //强制使用系统Toast
                        if ("enqueueToast".equals(method.getName())
                                || "enqueueToastEx".equals(method.getName())) {  //华为p20 pro上为enqueueToastEx
                            args[0] = "android";
                        }
                        return method.invoke(iNotificationManagerObj, args);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        /**
                         * 再不行 用自定义的顶部snackBar
                         */
                        Activity topActivity = XXF.getActivityStackProvider().getTopActivity();
                        showSnackBar(topActivity, noticeString, type);
                    }
                    return proxy;
                }
            });
            Field sServiceFiled = Toast.class.getDeclaredField("sService");
            sServiceFiled.setAccessible(true);
            sServiceFiled.set(null, iNotificationManagerProxy);
        }
        toast.show();
    }

    public static void showSnackBar(@NonNull Activity topActivity, @NonNull CharSequence notice, @NonNull ToastType type) {
        if (TextUtils.isEmpty(notice)) {
            return;
        }
        if (topActivity != null && !topActivity.isDestroyed() && !topActivity.isFinishing()) {
            try {
                Snackbar snackbar = Snackbar.Companion.make(topActivity.getWindow().getDecorView(), notice, Snackbar.LENGTH_SHORT);
                View snackbarView = snackbar.getView();
                int statusBarHeight = getStatusBarHeight(topActivity);
                snackbarView.setPadding(0, statusBarHeight, 0, 0);
                snackbarView.setBackgroundColor(0xFF333333);
                TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setMaxLines(3);
                textView.setCompoundDrawablePadding(DensityUtil.dip2px(7));
                int dp19 = DensityUtil.dip2px(19);
                switch (type) {
                    case ERROR:
                        Drawable errorDrawable = ApplicationProvider.applicationContext.getDrawable(R.drawable.xxf_ic_toast_error);
                        errorDrawable.setBounds(0, 0, dp19, dp19);
                        textView.setCompoundDrawables(errorDrawable, null, null, null);
                        break;
                    case NORMAL:
                        textView.setCompoundDrawables(null, null, null, null);
                        break;
                    case SUCCESS:
                        Drawable successDrawable = ApplicationProvider.applicationContext.getDrawable(R.drawable.xxf_ic_toast_success);
                        successDrawable.setBounds(0, 0, dp19, dp19);
                        textView.setCompoundDrawables(successDrawable, null, null, null);
                        break;
                }
                snackbar.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private static LimitToast createToast(CharSequence msg, ToastType type) {
        LayoutInflater inflater = LayoutInflater.from(ApplicationProvider.applicationContext);
        View view = inflater.inflate(R.layout.xxf_toast_layout, null);

        TextView text = view.findViewById(android.R.id.message);
        int dp19 = DensityUtil.dip2px(19);
        switch (type) {
            case ERROR:
                Drawable errorDrawable = ApplicationProvider.applicationContext.getDrawable(R.drawable.xxf_ic_toast_error);
                errorDrawable.setBounds(0, 0, dp19, dp19);
                text.setCompoundDrawables(errorDrawable, null, null, null);
                break;
            case NORMAL:
                text.setCompoundDrawables(null, null, null, null);
                break;
            case SUCCESS:
                Drawable successDrawable = ApplicationProvider.applicationContext.getDrawable(R.drawable.xxf_ic_toast_success);
                successDrawable.setBounds(0, 0, dp19, dp19);
                text.setCompoundDrawables(successDrawable, null, null, null);
                break;
        }
        text.setText(msg);
        LimitToast toast = new LimitToast(ApplicationProvider.applicationContext);
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

    /**
     * 取消全部toast
     */
    public static void cancelAll() {
        LimitToast.cancelAll();
    }
}
