package com.xxf.arch;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

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
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.alibaba.android.arouter.core.ARouterTab;
import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.enums.RouteType;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xxf.arch.arouter.ARouterParamsInject;
import com.xxf.arch.arouter.XXFUserInfoProvider;
import com.xxf.arch.core.AndroidActivityStackProvider;
import com.xxf.arch.core.Logger;
import com.xxf.arch.core.RxBus;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.core.activityresult.RxActivityResultCompact;
import com.xxf.arch.core.permission.RxPermissions;
import com.xxf.arch.http.XXFHttp;
import com.xxf.arch.lifecycle.LifecycleOwnerProvider;
import com.xxf.arch.rxjava.lifecycle.LifecycleProviderFactory;
import com.xxf.arch.rxjava.lifecycle.internal.LifecycleProvider;
import com.xxf.arch.rxjava.lifecycle.internal.LifecycleTransformer;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.rxjava.transformer.UIErrorTransformer;
import com.xxf.arch.service.SharedPreferencesService;
import com.xxf.arch.service.SharedPreferencesServiceImpl;
import com.xxf.arch.service.XXFFileService;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.arch.widget.progresshud.ProgressHUDFactory;
import com.xxf.arch.widget.progresshud.ProgressHUDProvider;

import java.util.Objects;
import java.util.concurrent.Callable;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Supplier;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 框架初始化
 */
public class XXF {
    public static class Builder {
        @NonNull
        Logger logger = new Logger() {
            @Override
            public boolean isLoggable() {
                return true;
            }

            @Override
            public void d(String msg) {
                Log.d("=============>", msg);
            }

            @Override
            public void d(String msg, Throwable tr) {
                Log.d("=============>", msg, tr);
            }

            @Override
            public void e(String msg) {
                Log.e("=============>", msg);
            }

            @Override
            public void e(String msg, Throwable tr) {
                Log.e("=============>", msg, tr);
            }
        };
        @NonNull
        Consumer<Throwable> errorHandler = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
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

            @Override
            public void init(Context context) {

            }
        };
        Application application;
        @NonNull
        ProgressHUDFactory.ProgressHUDProvider progressHUDProvider;

        public Builder(@NonNull Application application,
                       @NonNull ProgressHUDFactory.ProgressHUDProvider progressHUDProvider) {
            this.application = Objects.requireNonNull(application);
            this.progressHUDProvider = Objects.requireNonNull(progressHUDProvider);
        }

        public Builder setLogger(@NonNull Logger logger) {
            this.logger = logger;
            return this;
        }

        public Builder setErrorHandler(@NonNull Consumer<Throwable> errorHandler) {
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


    private static Logger logger;
    private static AndroidActivityStackProvider activityStackProvider;
    private static Consumer<Throwable> errorHandler;
    private static Function<Throwable, String> errorConvertFunction;
    private static XXFUserInfoProvider userInfoProvider;


    public static void init(Builder builder) {
        if (XXF.application == null) {
            synchronized (XXF.class) {
                if (XXF.application == null) {
                    XXF.application = builder.application;
                    XXF.logger = builder.logger;
                    XXF.errorHandler = builder.errorHandler;
                    XXF.errorConvertFunction = builder.errorConvertFunction;
                    XXF.userInfoProvider = builder.userInfoProvider;
                    activityStackProvider = new AndroidActivityStackProvider(application);
                    ProgressHUDFactory.setProgressHUDProvider(builder.progressHUDProvider);
                    initRouter();
                }
            }
        }
    }

    /**
     * arouter
     */
    private static void initRouter() {
        if (logger.isLoggable()) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(application);
        //一次性加载到表中
        ARouterTab.____initLoad();
        //router 解析参数注册
        new ARouterParamsInject().register(application);
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
    public static AndroidActivityStackProvider getActivityStackProvider() {
        return activityStackProvider;
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
     * 获取sp文件服务
     *
     * @return
     */
    @NonNull
    public static SharedPreferencesService getSharedPreferencesService() {
        return SharedPreferencesServiceImpl.getInstance();
    }


    /**
     * 日志打印器
     *
     * @return
     */
    public static Logger getLogger() {
        return logger;
    }


    /**
     * 获取异常转换器
     *
     * @return
     */
    public static Function<Throwable, String> getErrorConvertFunction() {
        return errorConvertFunction;
    }


    /**
     * get api
     *
     * @param apiClazz
     * @param <T>
     * @return
     */
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
     * XXF.getLogger().d("===========>yes:");
     * }
     * @Override public void onLost(Network network) {
     * super.onLost(network);
     * XXF.getLogger().d("===========>no:");
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
     * XXF.getLogger().d("===========>yes:");
     * }
     * @Override public void onLost(Network network) {
     * super.onLost(network);
     * XXF.getLogger().d("===========>no:");
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
     * 绑定生命周期
     *
     * @param lifecycleOwner
     * @param event
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull LifecycleOwner lifecycleOwner, @NonNull Lifecycle.Event event) {
        return LifecycleProviderFactory.getLifecycleProvider(lifecycleOwner).bindUntilEvent(event);
    }

    /**
     * 绑定生命周期
     *
     * @param lifecycleOwnerProvider
     * @param event
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull LifecycleOwnerProvider lifecycleOwnerProvider, @NonNull Lifecycle.Event event) {
        return LifecycleProviderFactory.getLifecycleProvider(lifecycleOwnerProvider == null ? null : lifecycleOwnerProvider.getLifecycleOwner()).bindUntilEvent(event);
    }

    /**
     * 绑定生命周期
     *
     * @param lifecycleOwner
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull LifecycleOwner lifecycleOwner) {
        return LifecycleProviderFactory.getLifecycleProvider(lifecycleOwner).bindToLifecycle();
    }

    /**
     * 自动取消
     * 不同于截流
     * 用法:observable.as(XXF.bindLifecycle(this))
     *
     * @param lifecycleOwner
     * @param <T>
     * @return
     */
    public static <T> AutoDisposeConverter<T> bindLifecycle(@NonNull LifecycleOwner lifecycleOwner) {
        return AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner));
    }

    /**
     * 自动取消
     * 不同于截流
     * 用法:observable.as(XXF.bindLifecycle(this,Lifecycle.Event.OnDestory))
     *
     * @param lifecycleOwner
     * @param untilEvent
     * @param <T>
     * @return
     */
    public static <T> AutoDisposeConverter<T> bindLifecycle(@NonNull LifecycleOwner lifecycleOwner, Lifecycle.Event untilEvent) {
        return AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent));
    }

    /**
     * 绑定生命周期
     *
     * @param lifecycleOwnerProvider
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull LifecycleOwnerProvider lifecycleOwnerProvider) {
        return LifecycleProviderFactory.getLifecycleProvider(lifecycleOwnerProvider == null ? null : lifecycleOwnerProvider.getLifecycleOwner()).bindToLifecycle();
    }

    /**
     * 绑定生命周期
     *
     * @param lifecycleProvider
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull LifecycleProvider lifecycleProvider) {
        return lifecycleProvider.bindToLifecycle();
    }


    /**
     * 绑定loading
     *
     * @param <T>
     * @return
     */
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
     *
     * @param lifecycleOwner
     * @param <T>
     * @return
     */
    public static <T> ProgressHUDTransformerImpl<T> bindToProgressHud(LifecycleOwner lifecycleOwner) {
        return new ProgressHUDTransformerImpl(lifecycleOwner);
    }

    /**
     * 绑定loading
     *
     * @param progressHUDProvider
     * @param <T>
     * @return
     */
    public static <T> ProgressHUDTransformerImpl<T> bindToProgressHud(ProgressHUDProvider progressHUDProvider) {
        return new ProgressHUDTransformerImpl(progressHUDProvider);
    }


    /**
     * 绑定错误提示
     *
     * @param <T>
     * @return
     */
    public static <T> UIErrorTransformer<T> bindToErrorNotice() {
        return new UIErrorTransformer<T>(XXF.errorHandler);
    }

    /**
     * 通过路由获取Activity class对象
     *
     * @param activityRouterPath
     * @return
     */
    public static Observable<Class<Activity>> getActivityClassByRouter(@NonNull final String activityRouterPath) {
        return Observable.defer(new Supplier<ObservableSource<? extends Class<Activity>>>() {
            @Override
            public ObservableSource<? extends Class<Activity>> get() throws Throwable {
                Objects.requireNonNull(activityRouterPath);
                return getPostcardByRouter(activityRouterPath)
                        .map(new Function<Postcard, Class<Activity>>() {
                            @Override
                            public Class<Activity> apply(Postcard build) throws Exception {
                                if (build.getType() != RouteType.ACTIVITY) {
                                    throw new IllegalArgumentException(String.format("% is not activity", activityRouterPath));
                                }
                                return (Class<Activity>) build.getDestination();
                            }
                        });
            }
        });
    }

    /**
     * 通过路由获取导航 对象
     *
     * @param routerPath
     * @return
     */
    public static Observable<Postcard> getPostcardByRouter(@NonNull final String routerPath) {
        return Observable.fromCallable(new Callable<Postcard>() {
            @Override
            public Postcard call() throws Exception {
                Objects.requireNonNull(routerPath);
                Postcard build = ARouter.getInstance().build(routerPath);
                LogisticsCenter.completion(build);
                if (build == null) {
                    throw new NullPointerException(String.format("% not found", routerPath));
                }
                return build;
            }
        });
    }

    /**
     * 通过路由获取CLASS 对象
     *
     * @param routerPath
     * @return
     */
    public static Observable<Class<?>> getClassByRouter(@NonNull final String routerPath) {
        return getPostcardByRouter(routerPath)
                .map(new Function<Postcard, Class<?>>() {
                    @Override
                    public Class<?> apply(Postcard postcard) throws Exception {
                        return postcard.getDestination();
                    }
                });
    }

    /**
     * 替代 startActivityForResult
     * activity栈顶Activity必须是 FragmentActivity,否则会报错
     * 注意:activity  onActivityResult方法 必须调用 super.onActivityResult(requestCode, resultCode, data);
     *
     * @param targetActivityRouterPath 目标Activity路由
     * @param params
     * @param requestCode
     * @return
     */
    @MainThread
    public static Observable<ActivityResult> startActivityForResult(@NonNull final String targetActivityRouterPath,
                                                                    @NonNull final Bundle params,
                                                                    final int requestCode) {
        return getActivityClassByRouter(targetActivityRouterPath)
                .flatMap(new Function<Class<Activity>, ObservableSource<ActivityResult>>() {
                    @Override
                    public ObservableSource<ActivityResult> apply(Class<Activity> activityClass) throws Exception {
                        return startActivityForResult(activityClass, params, requestCode);
                    }
                });
    }

    /**
     * 替代 startActivityForResult
     * activity栈顶Activity必须是 FragmentActivity,否则会报错
     * 注意:activity  onActivityResult方法 必须调用 super.onActivityResult(requestCode, resultCode, data);
     *
     * @param targetActivityClazz 目标Activity
     * @param params
     * @param requestCode
     * @return
     */
    @MainThread
    public static Observable<ActivityResult> startActivityForResult(
            @NonNull final Class<? extends Activity> targetActivityClazz,
            @NonNull final Bundle params,
            final int requestCode) {
        return Observable
                .fromCallable(new Callable<FragmentActivity>() {
                    @Override
                    public FragmentActivity call() throws Exception {
                        Activity topActivity = getActivityStackProvider().getTopActivity();
                        if (topActivity instanceof FragmentActivity) {
                            return (FragmentActivity) topActivity;
                        }
                        throw new RuntimeException("stack top activity must FragmentActivity!");
                    }
                })
                .flatMap(new Function<FragmentActivity, ObservableSource<ActivityResult>>() {
                    @Override
                    public ObservableSource<ActivityResult> apply(FragmentActivity fragmentActivity) throws Exception {
                        return startActivityForResult(fragmentActivity,
                                new Intent(fragmentActivity, targetActivityClazz)
                                        .putExtras(params),
                                requestCode);
                    }
                });
    }


    /**
     * 替代 startActivityForResult
     *
     * @param lifecycleOwner
     * @param intent
     * @param requestCode
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @MainThread
    public static Observable<ActivityResult> startActivityForResult(
            @NonNull LifecycleOwner lifecycleOwner, @NonNull Intent intent, int requestCode) {
        return Observable
                .defer(new Supplier<ObservableSource<? extends ActivityResult>>() {
                    @Override
                    public ObservableSource<? extends ActivityResult> get() throws Throwable {
                        if (lifecycleOwner instanceof FragmentActivity) {
                            return RxActivityResultCompact.startActivityForResult((FragmentActivity) lifecycleOwner, intent, requestCode);
                        } else if (lifecycleOwner instanceof Fragment) {
                            return RxActivityResultCompact.startActivityForResult((Fragment) lifecycleOwner, intent, requestCode);
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
     *
     * @param lifecycleOwner
     * @param permissions    {@link android.Manifest}
     * @return
     */
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
     *
     * @param permission
     * @return
     */
    public static boolean isGrantedPermission(@NonNull String permission) {
        return ContextCompat.checkSelfPermission(getApplication(), permission) ==
                PackageManager.PERMISSION_GRANTED;
    }


    /**
     * 获取ViewModel
     *
     * @param viewModelStoreOwner
     * @param modelClass
     * @param <T>
     * @return
     */
    public static <T extends ViewModel> T getViewModel(@NonNull ViewModelStoreOwner viewModelStoreOwner, @NonNull Class<T> modelClass) {
        return new ViewModelProvider(viewModelStoreOwner).get(modelClass);
    }

    /**
     * 获取ViewModel
     *
     * @param viewModelStoreOwner
     * @param key
     * @param modelClass
     * @param <T>
     * @return
     */
    public static <T extends ViewModel> T getViewModel(@NonNull ViewModelStoreOwner viewModelStoreOwner, @NonNull String key, @NonNull Class<T> modelClass) {
        return new ViewModelProvider(viewModelStoreOwner).get(key, modelClass);
    }


    /**
     * 发送事件
     *
     * @param event
     * @return
     */
    public static boolean postEvent(@NonNull Object event) {
        return RxBus.getInstance().postEvent(event);
    }


    /**
     * 订阅事件,注意线程问题
     * <p>
     * 例子:
     * XXF.subscribeEvent(String.class)
     * .observeOn(AndroidSchedulers.mainThread())
     * .as(XXF.bindLifecycle(this, Lifecycle.Event.ON_PAUSE))
     * .subscribe(new Consumer<String>() {
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public static <T> Observable<T> subscribeEvent(Class<T> eventType) {
        return RxBus.getInstance().subscribeEvent(eventType);
    }

    /**
     * 订阅粘性事件
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public static <T> Observable<T> subscribeStickyEvent(Class<T> eventType) {
        return RxBus.getInstance().subscribeStickyEvent(eventType);
    }

}
