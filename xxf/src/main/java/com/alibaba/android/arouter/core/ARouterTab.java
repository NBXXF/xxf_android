package com.alibaba.android.arouter.core;

import com.alibaba.android.arouter.facade.model.RouteMeta;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.alibaba.android.arouter.facade.template.IRouteGroup;
import com.xxf.arch.arouter.ARouterTabLoader;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.CheckReturnValue;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 路由表
 */
public final class ARouterTab extends Warehouse {


    /**
     * 提前加载到内存表中map
     */
    public static void ____initLoad() {
        try {
            ARouterTabLoader.loadRoutes(getGroupsIndex(), getRoutes());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取路由分组
     *
     * @return
     */
    @CheckReturnValue
    public static Map<String, Class<? extends IRouteGroup>> getGroupsIndex() {
        return groupsIndex;
    }

    /**
     * 获取所有路由
     *
     * @return
     */
    @CheckReturnValue
    public static Map<String, RouteMeta> getRoutes() {
        return routes;
    }

    /**
     * 获取指定路由目标
     *
     * @param path
     * @return
     */
    @CheckReturnValue
    public static RouteMeta getRoutes(String path) {
        return routes.get(path);
    }

    /**
     * 获取 所有provider
     *
     * @return
     */
    @CheckReturnValue
    public static Map<Class, IProvider> getProviders() {
        return providers;
    }

    /**
     * 获取 所有provider分组
     *
     * @return
     */
    @CheckReturnValue
    public static Map<String, RouteMeta> getProvidersIndex() {
        return providersIndex;
    }

    /**
     * 获取所有拦截器分组
     *
     * @return
     */
    @CheckReturnValue
    public static Map<Integer, Class<? extends IInterceptor>> getInterceptorsIndex() {
        return interceptorsIndex;
    }

    /**
     * 获取所有拦截器
     *
     * @return
     */
    @CheckReturnValue
    public static List<IInterceptor> getInterceptors() {
        return interceptors;
    }
}
