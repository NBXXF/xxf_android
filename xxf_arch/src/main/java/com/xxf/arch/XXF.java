package com.xxf.arch;

import android.app.Activity;
import android.app.Application;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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

import io.reactivex.Observable;
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
                }
            }
        }
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
     * @param fragment
     * @param permission
     * @return
     */
    public static boolean isGrantedPermission(@NonNull final Fragment fragment, @NonNull String permission) {
        return new RxPermissions(fragment).isGranted(permission);
    }

    /**
     * 是否开启该权限
     *
     * @param activity
     * @param permission
     * @return
     */
    public static boolean isGrantedPermission(@NonNull final FragmentActivity activity, @NonNull String permission) {
        return new RxPermissions(activity).isGranted(permission);
    }
}
