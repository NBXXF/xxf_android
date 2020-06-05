package com.alibaba.android.arouter.core;

import com.alibaba.android.arouter.facade.model.RouteMeta;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.alibaba.android.arouter.facade.template.IRouteGroup;

import java.util.List;
import java.util.Map;

import io.reactivex.annotations.CheckReturnValue;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 路由表
 */
public class ArouterTab extends Warehouse {

    /**
     * 获取路由分组
     *
     * @return
     */
    public static Map<String, Class<? extends IRouteGroup>> getGroupsIndex() {
        return groupsIndex;
    }

    /**
     * 获取所有路由
     *
     * @return
     */
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
    public static Map<Class, IProvider> getProviders() {
        return providers;
    }

    public static Map<String, RouteMeta> getProvidersIndex() {
        return providersIndex;
    }

    public static Map<Integer, Class<? extends IInterceptor>> getInterceptorsIndex() {
        return interceptorsIndex;
    }

    public static List<IInterceptor> getInterceptors() {
        return interceptors;
    }
}
