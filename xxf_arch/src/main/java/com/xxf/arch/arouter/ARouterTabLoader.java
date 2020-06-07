package com.xxf.arch.arouter;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.model.RouteMeta;
import com.alibaba.android.arouter.facade.template.IRouteGroup;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 自动导入到内存表中(map)
 */
public final class ARouterTabLoader {
    private ARouterTabLoader() {
    }

    public static void loadRoutes(@NonNull Map<String, Class<? extends IRouteGroup>> fromGroupsIndex, @NonNull Map<String, RouteMeta> toRouteMetaMap) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Objects.requireNonNull(fromGroupsIndex);
        Objects.requireNonNull(toRouteMetaMap);
        Iterator<Map.Entry<String, Class<? extends IRouteGroup>>> it = fromGroupsIndex.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Class<? extends IRouteGroup>> entry = it.next();
            Class<? extends IRouteGroup> groupMeta = entry.getValue();
            IRouteGroup iGroupInstance = groupMeta.getConstructor().newInstance();
            iGroupInstance.loadInto(toRouteMetaMap);
            it.remove();
        }
    }


    /**
     * 有init初始化问题,arouter发现表中没有才会初始化并init,如果在这里加载，一开始就Init了,设计不好
     *
     * @param fromProvidersIndex
     * @param toProviderMap
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     *//*
    public static void loadProvider(@NonNull Map<String, RouteMeta> fromProvidersIndex, @NonNull Map<Class, IProvider> toProviderMap) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Map.Entry<String, RouteMeta> entry : fromProvidersIndex.entrySet()) {
            RouteMeta routeMeta = entry.getValue();
            if (routeMeta != null && routeMeta.getType() == RouteType.PROVIDER) {
                Class<? extends IProvider> providerMeta = (Class<? extends IProvider>) routeMeta.getDestination();
                IProvider provider = providerMeta.getConstructor().newInstance();
                provider.init(mContext);
                toProviderMap.put(providerMeta, provider);
            }
        }
    }*/
}
