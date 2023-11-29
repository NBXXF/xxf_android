package com.xxf.json

import com.google.gson.Gson
import java.util.concurrent.ConcurrentHashMap

/**
 * ClassName JsonUtils
 * Description  json处理工具类，暂时核心封装Gson解析方式
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date 创建时间：2015/6/17 9:43
 * version
 */
internal object GsonUtils {

    /**
     *  避免频繁创建gson 提高性能
     */
    private val gsonCache: ConcurrentHashMap<String, Gson> = ConcurrentHashMap<String, Gson>()

    /**
     * @param excludeUnSerializableField 去除不可以序列化的字段 也就是 @Expose(serialize  = false) 默认不去除
     * @param excludeUnDeserializableField 去除不可反序列化字段 @Expose(deserialize = false) 默认不去除
     */
    internal fun getGson(
        excludeUnSerializableField: Boolean = false,
        excludeUnDeserializableField: Boolean = false
    ): Gson {
        val key =
            "excludeUnSerializableField=${excludeUnSerializableField}_excludeUnDeserializableField=${excludeUnDeserializableField}"
        return gsonCache.getOrPut(key) {
            GsonFactory.createGson(excludeUnSerializableField, excludeUnDeserializableField)
        }
    }

}