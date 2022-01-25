package com.xxf.arch.test;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.xxf.application.ApplicationProvider;
import com.xxf.arch.XXF;
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
        Log.d("====>app", "app:" + ApplicationProvider.applicationContext);
        Toast.makeText(this, "yes:" + ApplicationProvider.applicationContext, Toast.LENGTH_LONG).show();
        INSTANCE = this;
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("=============>", "", throwable);
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
        })
                .setErrorHandler(new BiConsumer<Integer, Throwable>() {
                    @Override
                    public void accept(Integer flag, Throwable throwable) throws Throwable {
                        ToastUtils.showToast("error:" + throwable, ToastUtils.ToastType.ERROR, flag);
                    }
                }));
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
