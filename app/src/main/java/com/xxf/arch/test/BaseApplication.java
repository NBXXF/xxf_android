package com.xxf.arch.test;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.lifecycle.LifecycleOwner;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.xxf.application.ApplicationInitializer;
import com.xxf.arch.XXF;
import com.xxf.arch.core.XXFUserInfoProvider;
import com.xxf.arch.lint.ComponentLintPlugin;
import com.xxf.arch.model.AppBackgroundEvent;
import com.xxf.arch.service.SpService;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.arch.widget.progresshud.ProgressHUDFactory;
import com.xxf.view.loading.XXFLoadingDialog;
import com.xxf.view.refresh.XXFJumpRefreshFooter;
import com.xxf.view.refresh.XXFJumpRefreshHeader;
import com.xxf.utils.ResourcesUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;


/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2018/9/7
 */
public class BaseApplication extends Application {
    public static BaseApplication INSTANCE;

    public static BaseApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("====>app", "app:" + ApplicationInitializer.applicationContext);
        Toast.makeText(this, "yes:" + ApplicationInitializer.applicationContext, Toast.LENGTH_LONG).show();
        INSTANCE = this;
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("=============>", "t:" + Thread.currentThread().getName(), throwable);
            }
        });
        XXF.init(new XXF.Builder(this, new ProgressHUDFactory.ProgressHUDProvider() {
            @Override
            public ProgressHUD onCreateProgressHUD(LifecycleOwner lifecycleOwner) {
             /*   if (lifecycleOwner instanceof FragmentActivity) {
                    return new DefaultProgressHUDImpl((FragmentActivity) lifecycleOwner);
                } else if (lifecycleOwner instanceof Fragment) {
                    return new DefaultProgressHUDImpl(((Fragment) lifecycleOwner).getContext());
                }*/

                if (lifecycleOwner instanceof FragmentActivity) {
                    return new XXFLoadingDialog((FragmentActivity) lifecycleOwner);
                } else if (lifecycleOwner instanceof Fragment) {
                    return new XXFLoadingDialog(((Fragment) lifecycleOwner).getContext());
                }
                return null;
            }
        }).setUserInfoProvider(new XXFUserInfoProvider() {
            @Override
            public String getUserId() {
                return "xxx_7764";
            }
        })
                .setErrorHandler(new BiConsumer<Integer, Throwable>() {
                    @Override
                    public void accept(Integer flag, Throwable throwable) throws Throwable {
                        ToastUtils.showToast("error:" + throwable, ToastUtils.ToastType.ERROR, flag);
                    }
                }));
        ComponentLintPlugin.INSTANCE.setLintConsumer(new BiConsumer<Object, Class<?>>() {
            @Override
            public void accept(Object o, Class<?> aClass) throws Throwable {
                ToastUtils.showToast(""+o.getClass().getSimpleName()+" must extends "+aClass);
            }
        });
        //可做本module 相关sdk初始化
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new XXFJumpRefreshHeader(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new XXFJumpRefreshFooter(context);
            }
        });
        // XXFStateLayout.setDefaultEmptyText(R.string.app_name);
        List<Integer> ignores = new ArrayList<>();
        ignores.addAll(ResourcesUtil.getStringResources(androidx.appcompat.R.class));
        ignores.addAll(ResourcesUtil.getDrawableResources(androidx.appcompat.R.class));

        //  ResourcesUtil.checkResources(ignores);

        setVmPolicy();

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                FrameLayout frameLayout = new FrameLayout(activity);
                frameLayout.setBackgroundColor(Color.TRANSPARENT);

                ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Toast.makeText(BaseApplication.this, "点击了", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                viewGroup.addView(frameLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }

            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                if (activity instanceof FragmentActivity) {
                    ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {

                        @Override
                        public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment fragment, @NonNull View v, @Nullable Bundle savedInstanceState) {
                            super.onFragmentViewCreated(fm, fragment, v, savedInstanceState);
                            if (fragment instanceof DialogFragment) {
                                Toast.makeText(activity, "fragment dialog", Toast.LENGTH_SHORT).show();

                                DialogFragment dialogFragment = (DialogFragment) fragment;
                                ViewGroup viewGroup = (ViewGroup) dialogFragment.getDialog().getWindow().getDecorView();
                                FrameLayout frameLayout = new FrameLayout(activity);
                                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Toast.makeText(BaseApplication.this, "点击了", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });
                                viewGroup.addView(frameLayout,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            }
                        }
                    }, true);
                }
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                activity.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                       // Toast.makeText(v.getContext(), "xxxxx", Toast.LENGTH_SHORT).show();
                        System.out.println("====================>touch le");
                        return false;
                    }
                });
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    //虚拟机策略检测
    private void setVmPolicy() {
        /**
         * 临时解决文件 FileUriExposedException
         */
   /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }*/
    }
}
