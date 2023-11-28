package com.xxf.toast;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import androidx.core.app.NotificationManagerCompat;
import com.xxf.application.ApplicationProviderKtKt;
import com.xxf.toast.impl.DefaultToastFactory;


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
@SuppressLint("SoonBlockedPrivateApi")
public class ToastUtils {
    /**
     * 私有 仅限内部链接application
     * @return
     */
    private static Application getLinkedApplication(){
        return ApplicationProviderKtKt.getApplication();
    }

    public static ToastFactory toastFactory=new DefaultToastFactory();


    private ToastUtils() {
    }


    /**
     * toast是否可用
     *
     * @return
     */
    public static boolean isNotificationEnabled() {
        try {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getLinkedApplication());
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
        return showToast(getLinkedApplication().getString(notice), type);
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
    public static Toast showToast(@NonNull CharSequence notice) {
        return showToast(notice, ToastType.NORMAL, Gravity.CENTER);
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
        return showToast(notice, type, Gravity.CENTER);
    }

    /**
     * 校验线程
     * 否则:Can't create handler inside thread that has not called Looper.prepare()
     *
     * @param notice
     * @param flag   用于标记业务类型
     * @return
     */
    @UiThread
    @Nullable
    public static Toast showToast(@NonNull CharSequence notice, @NonNull ToastType type, int flag) {
        try{
            if (!isMainThread() || TextUtils.isEmpty(notice)) {
                return null;
            }

            LimitToast toast =toastFactory.createToast(notice, type, getLinkedApplication(), flag);


            if (isNotificationEnabled()) {
                toast.show();
                return toast;
            } else {
                SnackbarUtils.showSnackBar(notice, type);
                return null;
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        return null;
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
