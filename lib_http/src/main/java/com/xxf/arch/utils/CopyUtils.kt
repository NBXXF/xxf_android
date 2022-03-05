package com.xxf.arch.utils

import com.google.gson.reflect.TypeToken
import com.xxf.arch.json.GsonFactory
import com.xxf.arch.json.JsonUtils
import com.xxf.arch.json.exclusionstrategy.ExposeDeserializeExclusionStrategy
import com.xxf.arch.json.exclusionstrategy.ExposeSerializeExclusionStrategy

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/9/3
 * Description ://深拷贝
 */

/**
 * 深拷贝
 * @param excludeUnSerializableField 去除不可以序列化的字段 也就是 @Expose(serialize  = false)  默认不去除
 * @param excludeUnDeserializableField 去除不可反序列化字段 @Expose(deserialize = false) 默认不去除
 */
inline fun <reified T> T.copy(
    excludeUnSerializableField: Boolean = false,
    excludeUnDeserializableField: Boolean = false
): T {
    var newBuilder = GsonFactory.createGson()
        .newBuilder()
    if (excludeUnSerializableField) {
        newBuilder =
            newBuilder.addSerializationExclusionStrategy(ExposeSerializeExclusionStrategy())
    }
    if (excludeUnDeserializableField) {
        newBuilder =
            newBuilder.addSerializationExclusionStrategy(ExposeDeserializeExclusionStrategy())
    }
    val gson = newBuilder.create()
    val toJson = gson.toJson(this)
    return gson.fromJson<T>(toJson, object : TypeToken<T>() {}.type)
}

/**
 * 深拷贝
 * 父类和子类对象 可以双向转换复制
 * @param excludeUnSerializableField 去除不可以序列化的字段 也就是 @Expose(serialize  = false) 默认不去除
 * @param excludeUnDeserializableField 去除不可反序列化字段 @Expose(deserialize = false) 默认不去除
 */
inline fun <reified T, R> T.copy(
    toClass: Class<R>,
    excludeUnSerializableField: Boolean = false,
    excludeUnDeserializableField: Boolean = false
): R {
    var newBuilder = GsonFactory.createGson()
        .newBuilder()
    if (excludeUnSerializableField) {
        newBuilder =
            newBuilder.addSerializationExclusionStrategy(ExposeSerializeExclusionStrategy())
    }
    if (excludeUnDeserializableField) {
        newBuilder =
            newBuilder.addSerializationExclusionStrategy(ExposeDeserializeExclusionStrategy())
    }
    val gson = newBuilder.create()
    val toJson = gson.toJson(this)
    return gson.fromJson(toJson, toClass)
}