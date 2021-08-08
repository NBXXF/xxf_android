package com.xxf.arch.arouter

import com.alibaba.android.arouter.facade.model.RouteMeta
import com.alibaba.android.arouter.facade.template.IRouteGroup
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 自动导入到内存表中(map)
 */
object ARouterTabLoader {
    @JvmStatic
    @Throws(NoSuchMethodException::class, IllegalAccessException::class, InvocationTargetException::class, InstantiationException::class)
    fun loadRoutes(fromGroupsIndex: MutableMap<String?, Class<out IRouteGroup>>, toRouteMetaMap: Map<String?, RouteMeta?>) {
        Objects.requireNonNull(fromGroupsIndex)
        Objects.requireNonNull(toRouteMetaMap)
        val it: MutableIterator<Map.Entry<String?, Class<out IRouteGroup>>> = fromGroupsIndex.entries.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            val groupMeta = entry.value
            try {
                val iGroupInstance = groupMeta.getConstructor().newInstance()
                iGroupInstance.loadInto(toRouteMetaMap)
                it.remove()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
     */
    /*
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