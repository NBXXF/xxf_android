package com.xxf.arch.core.permission;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;

import com.xxf.arch.utils.ToastUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description 权限转换 加提示
 * @Company Beijing icourt
 * @date createTime：2018/9/3
 */
public class RxPermissionTransformer implements ObservableTransformer<Boolean, Boolean> {
    String rejectNotice;
    Context context;
    boolean rejecctJumpPermissionSetting = true;//默认跳转到设置页面

    /**
     * @param context                      上下文
     * @param rejectNotice                 拒绝后的提示文案
     * @param rejecctJumpPermissionSetting 权限拒绝是否跳转到系统权限设置页面
     */
    public RxPermissionTransformer(@NonNull Context context, @NonNull String rejectNotice, boolean rejecctJumpPermissionSetting) {
        this.context = context;
        this.rejectNotice = rejectNotice;
        this.rejecctJumpPermissionSetting = rejecctJumpPermissionSetting;
    }

    public RxPermissionTransformer(@NonNull Context context, @NonNull String rejectNotice) {
        this(context, rejectNotice, true);
    }

    @Override
    public ObservableSource<Boolean> apply(Observable<Boolean> upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            ToastUtils.showToast(rejectNotice, ToastUtils.ToastType.ERROR);
                        }
                        if (!aBoolean && rejecctJumpPermissionSetting) {
                            try {
                                context.startActivity(getAppDetailSettingIntent());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 获取app应用详情
     *
     * @return
     */
    private Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }
}
