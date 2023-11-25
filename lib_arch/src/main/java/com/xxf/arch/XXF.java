package com.xxf.arch;

import android.Manifest;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;


import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import com.xxf.activity.result.launcher.ActivityResultLauncher;
import com.xxf.application.initializer.ApplicationInitializer;
import com.xxf.application.activity.ActivityStackProvider;
import com.xxf.application.activity.AndroidActivityStackProvider;
import com.xxf.arch.app.AppBackgroundLifecycleCallbacks;
import com.xxf.arch.core.XXFUserInfoProvider;
import com.xxf.arch.lint.ComponentLintPlugin;

import com.xxf.arch.service.XXFFileService;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.arch.widget.progresshud.ProgressHUDFactory;
import com.xxf.log.LogUtils;

import java.util.Arrays;
import java.util.Objects;
import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.functions.Function;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 框架初始化
 */
public class XXF {
    public static class Builder {
        @NonNull
        BiConsumer<Integer, Throwable> errorHandler = new BiConsumer<Integer, Throwable>() {
            @Override
            public void accept(Integer integer, Throwable throwable) {
                ToastUtils.showToast(throwable.getMessage(), ToastUtils.ToastType.ERROR);
            }
        };
        @NonNull
        Function<Throwable, String> errorConvertFunction = new Function<Throwable, String>() {
            @Override
            public String apply(Throwable throwable) throws Exception {
                return throwable.getMessage();
            }
        };
        XXFUserInfoProvider userInfoProvider = new XXFUserInfoProvider() {
            @Override
            public String getUserId() {
                return "";
            }
        };
        Application application;
        @NonNull
        ProgressHUDFactory.ProgressHUDProvider progressHUDProvider;
        boolean isDebug = true;

        public Builder(@NonNull Application application,
                       @NonNull ProgressHUDFactory.ProgressHUDProvider progressHUDProvider) {
            this.application = Objects.requireNonNull(application);
            this.progressHUDProvider = Objects.requireNonNull(progressHUDProvider);
        }


        public Builder setDebug(boolean debug) {
            isDebug = debug;
            return this;
        }

        public Builder setErrorHandler(@NonNull BiConsumer<Integer, Throwable> errorHandler) {
            this.errorHandler = Objects.requireNonNull(errorHandler);
            return this;
        }

        public Builder setErrorConvertFunction(@NonNull Function<Throwable, String> errorConvertFunction) {
            this.errorConvertFunction = Objects.requireNonNull(errorConvertFunction);
            return this;
        }

        public Builder setUserInfoProvider(@NonNull XXFUserInfoProvider userInfoProvider) {
            this.userInfoProvider = Objects.requireNonNull(userInfoProvider);
            return this;
        }
    }

    private XXF() {
    }

    private static Application application;


    private static BiConsumer<Integer, Throwable> errorHandler;
    private static Function<Throwable, String> errorConvertFunction;
    private static XXFUserInfoProvider userInfoProvider;
    private static String sharedPreferencesName="flow_us_sp_release";


    public static void init(Builder builder) {
        if (XXF.application == null) {
            synchronized (XXF.class) {
                if (XXF.application == null) {
                    XXF.application = builder.application;
                    if(builder.isDebug) {
                        ComponentLintPlugin.INSTANCE.initPlugin(builder.application, Arrays.asList("com.xxf.arch.test"));
                    }
                    LogUtils.INSTANCE.getConfig().setDebug(builder.isDebug);
                    AppBackgroundLifecycleCallbacks.INSTANCE.register(builder.application);
                    //初始化新方式 获取activity result和权限
                    ActivityResultLauncher.INSTANCE.init(builder.application);
                    //Initializer 跨进程不会初始化
                    ApplicationInitializer.Companion.init(builder.application);
                    XXF.errorHandler = builder.errorHandler;
                    XXF.errorConvertFunction = builder.errorConvertFunction;
                    XXF.userInfoProvider = builder.userInfoProvider;
                    ProgressHUDFactory.INSTANCE.setProgressHUDProvider(builder.progressHUDProvider);
                }
            }
        }
    }


    /**
     * 获取application
     *
     * @return
     */
    public static Application getApplication() {
        if (application == null) {
            throw new NullPointerException("you need call XXF.init function");
        }
        return application;
    }

    /**
     * activity堆栈
     *
     * @return
     */
    public static ActivityStackProvider getActivityStackProvider() {
        return AndroidActivityStackProvider.INSTANCE;
    }

    public static String getSharedPreferencesName() {
        return sharedPreferencesName;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @Nullable
    @CheckResult
    public static XXFUserInfoProvider getUserInfoProvider() {
        return userInfoProvider;
    }

    /**
     * 获取文件服务
     *
     * @return
     */
    @NonNull
    public static XXFFileService getFileService() {
        return XXFFileService.getDefault();
    }

    /**
     * 获取异常转换器
     *
     * @return
     */
    public static Function<Throwable, String> getErrorConvertFunction() {
        return errorConvertFunction;
    }

    public static BiConsumer<Integer, Throwable> getErrorHandler() {
        return errorHandler;
    }


    /**
     * 注册网络监听
     * you need add
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
     * XXF.registerNetworkCallback(new ConnectivityManager.NetworkCallback() {
     *
     * @param networkCallback
     * @Override public void onAvailable(Network network) {
     * super.onAvailable(network);
     * Log.d("===========>yes:");
     * }
     * @Override public void onLost(Network network) {
     * super.onLost(network);
     * Log.d("===========>no:");
     * }
     * });
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE})
    public static void registerNetworkCallback(ConnectivityManager.NetworkCallback networkCallback) {
        NetworkRequest request = new NetworkRequest.Builder().build();
        ConnectivityManager cmgr = (ConnectivityManager) application
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cmgr != null) {
            cmgr.registerNetworkCallback(request, networkCallback);
        }
    }

    /**
     * 注册网络监听
     * you need addd
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
     * XXF.registerNetworkCallback(new ConnectivityManager.NetworkCallback() {
     *
     * @param networkCallback
     * @Override public void onAvailable(Network network) {
     * super.onAvailable(network);
     * Log.d("===========>yes:");
     * }
     * @Override public void onLost(Network network) {
     * super.onLost(network);
     * Log.d("===========>no:");
     * }
     * });
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE})
    public static void unregisterNetworkCallback(ConnectivityManager.NetworkCallback networkCallback) {
        ConnectivityManager cmgr = (ConnectivityManager) application
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cmgr != null) {
            cmgr.unregisterNetworkCallback(networkCallback);
        }
    }

}
