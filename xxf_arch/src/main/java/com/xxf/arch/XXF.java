package com.xxf.arch;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.core.ARouterTab;
import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.enums.RouteType;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xxf.arch.arouter.ARouterParamsInject;
import com.xxf.arch.arouter.XXFUserInfoProvider;
import com.xxf.arch.core.AndroidActivityStackProvider;
import com.xxf.arch.core.AndroidLifecycleProvider;
import com.xxf.arch.core.Logger;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.core.activityresult.RxActivityResultCompact;
import com.xxf.arch.core.permission.RxPermissions;
import com.xxf.arch.http.XXFHttp;
import com.xxf.arch.rxjava.lifecycle.internal.LifecycleTransformer;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.rxjava.transformer.UIErrorTransformer;
import com.xxf.arch.service.XXFFileService;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.arch.widget.progresshud.ProgressHUDFactory;

import java.util.Objects;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
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
    private static AndroidLifecycleProvider LifecycleProvider;
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
                    LifecycleProvider = new AndroidLifecycleProvider(application);
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
     * 绑定生命周期
     *
     * @param lifecycleOwner
     * @param event
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull LifecycleOwner lifecycleOwner, @NonNull Lifecycle.Event event) {
        return LifecycleProvider.getLifecycleProvider(lifecycleOwner).bindUntilEvent(event);
    }

    /**
     * 绑定生命周期
     *
     * @param lifecycleOwner
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull LifecycleOwner lifecycleOwner) {
        return LifecycleProvider.getLifecycleProvider(lifecycleOwner).bindToLifecycle();
    }

    /**
     * 绑定loading
     *
     * @param builder
     * @param <T>
     * @return
     */
    public static <T> ProgressHUDTransformerImpl<T> bindToProgressHud(ProgressHUDTransformerImpl.Builder builder) {
        return builder.build();
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
            throw new RuntimeException("topActivity is not FragmentActivity or LifecycleOwner");
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
        ProgressHUD progressHUD = ProgressHUDFactory.getInstance().getProgressHUD(lifecycleOwner);
        return new ProgressHUDTransformerImpl.Builder(progressHUD).build();
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
        return Observable.defer(new Callable<ObservableSource<? extends Class<Activity>>>() {
            @Override
            public ObservableSource<? extends Class<Activity>> call() throws Exception {
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
                }).flatMap(new Function<FragmentActivity, ObservableSource<ActivityResult>>() {
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
     * @param activity
     * @param intent
     * @param requestCode
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @MainThread
    public static Observable<ActivityResult> startActivityForResult(
            @NonNull FragmentActivity activity, @NonNull Intent intent, int requestCode) {
        return RxActivityResultCompact.startActivityForResult(activity, intent, requestCode);
    }


    /**
     * 替代 startActivityForResult
     *
     * @param fragment
     * @param intent
     * @param requestCode
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @MainThread
    public static Observable<ActivityResult> startActivityForResult(
            @NonNull Fragment fragment, @NonNull Intent intent, int requestCode) {
        return RxActivityResultCompact.startActivityForResult(fragment, intent, requestCode);
    }

    /**
     * 请求权限
     * activity栈顶Activity必须是 FragmentActivity,否则会报错
     * 注意:activity  onRequestPermissionsResult方法 必须调用   super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     * 不可并行
     *
     * @param permissions {@link android.Manifest}
     * @return
     */
    @MainThread
    public static Observable<Boolean> requestPermission(@NonNull final String... permissions) {
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
                .flatMap(new Function<FragmentActivity, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(FragmentActivity fragmentActivity) throws Exception {
                        return requestPermission(fragmentActivity, permissions);
                    }
                });
    }

    /**
     * 请求权限
     * 不可并行
     *
     * @param activity
     * @param permissions {@link android.Manifest}
     * @return
     */
    @MainThread
    public static Observable<Boolean> requestPermission(@NonNull final FragmentActivity activity, @NonNull final String... permissions) {
        return new RxPermissions(activity).request(permissions);
    }

    /**
     * 请求权限
     * 不可并行
     *
     * @param fragment
     * @param permissions {@link android.Manifest}
     * @return
     */
    public static Observable<Boolean> requestPermission(@NonNull final Fragment fragment, @NonNull final String... permissions) {
        return new RxPermissions(fragment).request(permissions);
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
     * @param fragment
     * @param modelClass
     * @param <T>
     * @return
     */
    public static <T extends ViewModel> T getViewModel(@NonNull Fragment fragment, Class<T> modelClass) {
        return ViewModelProviders.of(fragment).get(modelClass);
    }

    /**
     * 获取ViewModel
     *
     * @param activity
     * @param modelClass
     * @param <T>
     * @return
     */
    public static <T extends ViewModel> T getViewModel(@NonNull FragmentActivity activity, Class<T> modelClass) {
        return ViewModelProviders.of(activity).get(modelClass);
    }

}
