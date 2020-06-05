package com.xxf.arch;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.core.ArouterTab;
import com.alibaba.android.arouter.facade.model.RouteMeta;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xxf.arch.arouter.ArouterAppInject;
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
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.arch.widget.progresshud.ProgressHUDFactory;

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
    private XXF() {

    }

    private static Application application;


    private static Logger logger;
    private static AndroidActivityStackProvider activityStackProvider;
    private static AndroidLifecycleProvider LifecycleProvider;
    private static Consumer<Throwable> errorHandler;
    private static Function<Throwable, String> errorConvertFunction;


    public static void init(Application application,
                            Logger logger,
                            Consumer<Throwable> errorHandler,
                            Function<Throwable, String> errorConvertFunction) {
        if (XXF.application == null) {
            synchronized (XXF.class) {
                if (XXF.application == null) {
                    XXF.application = application;
                    XXF.logger = logger;
                    XXF.errorHandler = errorHandler;
                    XXF.errorConvertFunction = errorConvertFunction;
                    activityStackProvider = new AndroidActivityStackProvider(application);
                    LifecycleProvider = new AndroidLifecycleProvider(application);
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
        //router注册
        new ArouterAppInject().register(application);
    }

    /**
     * 设置默认的progress loading
     *
     * @param progressHUDProvider
     */
    public static void setProgressHUDProvider(ProgressHUDFactory.ProgressHUDProvider progressHUDProvider) {
        ProgressHUDFactory.setProgressHUDProvider(progressHUDProvider);
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
     *   替代 startActivityForResult
     *   activity栈中必须包含一个FragmentActivity,如果栈中没有,否则会报错
     *    注意:activity  onActivityResult方法 必须调用 super.onActivityResult(requestCode, resultCode, data);
     * @param targetActivityRouterPath 路由
     * @param params
     * @param requestCode
     * @return
     */
    public static Observable<ActivityResult> startActivityForResult(@NonNull final String targetActivityRouterPath,
                                                                    @NonNull final Bundle params,
                                                                    final int requestCode) {
       return  ___getOneFragmentActivityFormStack()
                .flatMap(new Function<FragmentActivity, ObservableSource<ActivityResult>>() {
                    @Override
                    public ObservableSource<ActivityResult> apply(FragmentActivity fragmentActivity) throws Exception {
                        return startActivityForResult(fragmentActivity,targetActivityRouterPath,params,requestCode);
                    }
                });

    }

    public static Observable<ActivityResult> startActivityForResult(@NonNull final FragmentActivity fragmentActivity,
                                                                    @NonNull final String targetActivityRouterPath,
                                                                    @NonNull final Bundle params,
                                                                    final int requestCode) {
        return getActivity(targetActivityRouterPath)
                .flatMap(new Function<Class<Activity>, ObservableSource<ActivityResult>>() {
                    @Override
                    public ObservableSource<ActivityResult> apply(Class<Activity> activityClass) throws Exception {
                        return startActivityForResult(fragmentActivity, new Intent(fragmentActivity, activityClass)
                                .putExtras(params), requestCode);
                    }
                });

    }

    /**
     * 替代 startActivityForResult
     * activity栈中必须包含一个FragmentActivity,如果栈中没有,否则会报错
     * 注意:activity  onActivityResult方法 必须调用 super.onActivityResult(requestCode, resultCode, data);
     *
     * @param intent
     * @param requestCode
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Observable<ActivityResult> startActivityForResult(@NonNull final Intent intent, final int requestCode) {
        return ___getOneFragmentActivityFormStack()
                .flatMap(new Function<FragmentActivity, ObservableSource<ActivityResult>>() {
                    @Override
                    public ObservableSource<ActivityResult> apply(FragmentActivity fragmentActivity) throws Exception {
                        return startActivityForResult(fragmentActivity, intent, requestCode);
                    }
                });
    }


    /**
     * 通过路由获取Activity
     *
     * @param activityRouterPath
     * @return
     */
    public static Observable<Class<Activity>> getActivity(final String activityRouterPath) {
        return Observable.fromCallable(new Callable<Class<Activity>>() {
            @Override
            public Class<Activity> call() throws Exception {
                RouteMeta routes = ArouterTab.getRoutes(activityRouterPath);
                Class<?> destination = routes.getDestination();
                if (!Activity.class.isAssignableFrom(destination)) {
                    throw new RuntimeException(String.format("% is not activity", activityRouterPath));
                }
                return (Class<Activity>) destination;
            }
        });
    }

    /**
     * 从栈中获取一个FragmentActivity
     *
     * @return
     */
    private static Observable<FragmentActivity> ___getOneFragmentActivityFormStack() {
        return Observable.fromCallable(new Callable<FragmentActivity>() {
            @Override
            public FragmentActivity call() throws Exception {
                Activity[] allActivity = getActivityStackProvider().getAllActivity();
                for (Activity activity : allActivity) {
                    if (activity instanceof FragmentActivity
                            && !activity.isDestroyed()
                            && !activity.isFinishing()) {
                        return (FragmentActivity) activity;
                    }
                }
                throw new RuntimeException("stack is not contain one FragmentActivity");
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
    public static Observable<ActivityResult> startActivityForResult(
            @NonNull Fragment fragment, @NonNull Intent intent, int requestCode) {
        return RxActivityResultCompact.startActivityForResult(fragment, intent, requestCode);
    }

    /**
     * 请求权限
     * activity栈中必须包含一个FragmentActivity,如果栈中没有,否则会报错
     * 注意:activity  onRequestPermissionsResult方法 必须调用   super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     *
     * @param permissions
     * @return
     */
    public static Observable<Boolean> requestPermission(@NonNull final String... permissions) {
        return ___getOneFragmentActivityFormStack()
                .flatMap(new Function<FragmentActivity, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(FragmentActivity fragmentActivity) throws Exception {
                        return requestPermission(fragmentActivity, permissions);
                    }
                });
    }

    /**
     * 请求权限
     *
     * @param activity
     * @param permissions
     * @return
     */
    public static Observable<Boolean> requestPermission(@NonNull final FragmentActivity activity, @NonNull final String... permissions) {
        return new RxPermissions(activity).request(permissions);
    }

    /**
     * 请求权限
     *
     * @param fragment
     * @param permissions
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
