package com.xxf.arch;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;
import android.view.View;

import androidx.annotation.CheckResult;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.application.ApplicationInitializer;
import com.xxf.application.activity.ActivityStackProvider;
import com.xxf.application.activity.AndroidActivityStackProvider;
import com.xxf.arch.app.AppBackgroundLifecycleCallbacks;
import com.xxf.arch.core.XXFUserInfoProvider;
import com.xxf.bus.RxBus;
import com.xxf.activityresult.ActivityResult;
import com.xxf.activityresult.RxActivityResultCompact;
import com.xxf.permission.RxPermissions;
import com.xxf.arch.http.XXFHttp;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.rxjava.transformer.UIErrorTransformer;
import com.xxf.arch.service.XXFFileService;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.arch.widget.progresshud.ProgressHUDFactory;
import com.xxf.rxjava.RxLifecycle;

import java.util.Objects;

import autodispose2.AutoDisposeConverter;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Supplier;


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
        String sharedPreferencesName = "xxfSpService";

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

        public Builder setSharedPreferencesName(String sharedPreferencesName) {
            this.sharedPreferencesName = sharedPreferencesName;
            return this;
        }
    }

    private XXF() {
    }

    private static Application application;


    private static BiConsumer<Integer, Throwable> errorHandler;
    private static Function<Throwable, String> errorConvertFunction;
    private static XXFUserInfoProvider userInfoProvider;
    private static String sharedPreferencesName;


    public static void init(Builder builder) {
        if (XXF.application == null) {
            synchronized (XXF.class) {
                if (XXF.application == null) {
                    XXF.application = builder.application;
                    AppBackgroundLifecycleCallbacks.INSTANCE.register(builder.application);
                    //Initializer 跨进程不会初始化
                    ApplicationInitializer.Companion.init(builder.application);
                    XXF.errorHandler = builder.errorHandler;
                    XXF.errorConvertFunction = builder.errorConvertFunction;
                    XXF.userInfoProvider = builder.userInfoProvider;
                    XXF.sharedPreferencesName = builder.sharedPreferencesName;
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
     * get api
     * [过时  请用内敛函数 getApiService 或者 UserApi.class.apiService]
     *
     * @param apiClazz
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> T getApiService(Class<T> apiClazz) {
        return XXFHttp.getApiService(apiClazz);
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
    public static void unregisterNetworkCallback(ConnectivityManager.NetworkCallback networkCallback) {
        ConnectivityManager cmgr = (ConnectivityManager) application
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cmgr != null) {
            cmgr.unregisterNetworkCallback(networkCallback);
        }
    }


    /**
     * 自动取消
     * 不同于截流
     * 用法:observable.as(XXF.bindLifecycle(this))
     * 【过时了, kotlin 对于rxjava 对象 直接bindLifecycle】
     *
     * @param view 和view生命周期关联
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> AutoDisposeConverter<T> bindLifecycle(@NonNull View view) {
        return RxLifecycle.INSTANCE.bindLifecycle(view);
    }

    /**
     * 自动取消
     * 不同于截流
     * 用法:observable.as(XXF.bindLifecycle(this))
     * 【过时了, kotlin 对于rxjava 对象 直接bindLifecycle】
     *
     * @param lifecycleOwner
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> AutoDisposeConverter<T> bindLifecycle(@NonNull LifecycleOwner lifecycleOwner) {
        return RxLifecycle.INSTANCE.bindLifecycle(lifecycleOwner, Lifecycle.Event.ON_DESTROY);
    }

    /**
     * 自动取消
     * 不同于截流
     * 用法:observable.as(XXF.bindLifecycle(this,Lifecycle.Event.OnDestory))
     * 【过时了, kotlin 对于rxjava 对象 直接bindLifecycle】
     *
     * @param lifecycleOwner
     * @param untilEvent
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> AutoDisposeConverter<T> bindLifecycle(@NonNull LifecycleOwner lifecycleOwner, Lifecycle.Event untilEvent) {
        return RxLifecycle.INSTANCE.bindLifecycle(lifecycleOwner, untilEvent);
    }


    /**
     * 绑定loading
     * [请 直接用Observale 对象.bindProgressHud() ]
     *
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> ProgressHUDTransformerImpl<T> bindToProgressHud() {
        if (XXF.getActivityStackProvider().empty()) {
            return ProgressHUDTransformerImpl.EMPTY;
        }
        //避免onActivityResult中执行 造成内存泄露
        Activity topActivity = XXF.getActivityStackProvider().getTopActivity();
        if (topActivity.isFinishing() || topActivity.isDestroyed()) {
            return ProgressHUDTransformerImpl.EMPTY;
        }
        if (topActivity instanceof LifecycleOwner) {
            return bindToProgressHud((LifecycleOwner) topActivity);
        } else {
            return ProgressHUDTransformerImpl.EMPTY;
        }
    }

    /**
     * 绑定loading
     * [请 直接用Observale 对象.bindProgressHud() ]
     *
     * @param lifecycleOwner
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> ProgressHUDTransformerImpl<T> bindToProgressHud(LifecycleOwner lifecycleOwner) {
        return new ProgressHUDTransformerImpl(lifecycleOwner);
    }


    /**
     * 绑定错误提示
     * [请 直接用Observale 对象.bindErrorNotice() ]
     *
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> UIErrorTransformer<T> bindToErrorNotice() {
        return new UIErrorTransformer<T>(XXF.errorHandler);
    }


    /**
     * 替代 startActivityForResult
     * 【过时了 请用 Activity.requestPermissionObservable 或者Fragment.requestPermissionObservable】
     *
     * @param lifecycleOwner
     * @param intent
     * @param requestCode
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @MainThread
    @Deprecated
    public static Observable<ActivityResult> startActivityForResult(
            @NonNull LifecycleOwner lifecycleOwner, @NonNull Intent intent, int requestCode) {
        return Observable
                .defer(new Supplier<ObservableSource<? extends ActivityResult>>() {
                    @Override
                    public ObservableSource<? extends ActivityResult> get() throws Throwable {
                        if (lifecycleOwner instanceof FragmentActivity) {
                            return RxActivityResultCompact.INSTANCE.startActivityForResult((FragmentActivity) lifecycleOwner, intent, requestCode);
                        } else if (lifecycleOwner instanceof Fragment) {
                            return RxActivityResultCompact.INSTANCE.startActivityForResult((Fragment) lifecycleOwner, intent, requestCode);
                        } else {
                            return Observable.error(new IllegalArgumentException("不支持的类型!"));
                        }
                    }
                }).subscribeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 请求权限
     * 不可用zip 等操作符
     * 注意:activity  onRequestPermissionsResult方法 必须调用   super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     * <p>
     * 【过时了 请用 Activity.requestPermissionObservable 或者Fragment.requestPermissionObservable】
     *
     * @param lifecycleOwner
     * @param permissions    {@link android.Manifest}
     * @return
     */
    @Deprecated
    @MainThread
    public static Observable<Boolean> requestPermission(@NonNull final LifecycleOwner lifecycleOwner,
                                                        @NonNull final String... permissions) {
        return Observable
                .defer(new Supplier<ObservableSource<? extends Boolean>>() {
                    @Override
                    public ObservableSource<? extends Boolean> get() throws Throwable {
                        if (lifecycleOwner instanceof FragmentActivity) {
                            return new RxPermissions((FragmentActivity) lifecycleOwner).request(permissions);
                        } else if (lifecycleOwner instanceof Fragment) {
                            return new RxPermissions((Fragment) lifecycleOwner).request(permissions);
                        } else {
                            return Observable.error(new IllegalArgumentException("不支持的类型!"));
                        }
                    }
                }).subscribeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 是否开启该权限
     * 【过时了 请用 Activity.isGrantedPermission 或者Fragment.isGrantedPermission】
     *
     * @param permission
     * @return
     */
    @Deprecated
    public static boolean isGrantedPermission(@NonNull String permission) {
        return ContextCompat.checkSelfPermission(ApplicationInitializer.applicationContext, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }


    /**
     * 发送事件
     * 请参考新用法
     * String.javaClass.subscribeEvent()
     * .subscribe {
     * <p>
     * }
     * "测试".postEvent();
     *
     * @param event
     * @return
     */
    @Deprecated
    public static boolean postEvent(@NonNull Object event) {
        return RxBus.INSTANCE.postEvent(event);
    }


    /**
     * 订阅事件,注意线程问题
     * <p>
     * 例子:
     * XXF.subscribeEvent(String.class)
     * .observeOn(AndroidSchedulers.mainThread())
     * .as(XXF.bindLifecycle(this, Lifecycle.Event.ON_PAUSE))
     * .subscribe(new Consumer<String>() {
     * <p>
     * <p>
     * 请参考新用法
     * String.javaClass.subscribeEvent()
     * .subscribe {
     * <p>
     * }
     * "测试".postEvent();
     *
     * @param eventType
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> Observable<T> subscribeEvent(Class<T> eventType) {
        return RxBus.INSTANCE.subscribeEvent(eventType, false);
    }

    /**
     * 订阅粘性事件
     * 请参考新用法
     * String.javaClass.subscribeEvent()
     * .subscribe {
     * <p>
     * }
     * "测试".postEvent();
     *
     * @param eventType
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> Observable<T> subscribeStickyEvent(Class<T> eventType) {
        return RxBus.INSTANCE.subscribeEvent(eventType, true);
    }

}
