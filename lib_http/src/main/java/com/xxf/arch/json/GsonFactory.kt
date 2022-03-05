package com.xxf.arch.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xxf.arch.json.typeadapter.bool.BooleanTypeAdapter
import com.xxf.arch.json.typeadapter.number.IntegerTypeAdapter
import com.xxf.arch.json.typeadapter.number.LongTypeAdapter
import com.xxf.arch.json.typeadapter.number.PercentageDoubleTypeAdapter
import com.xxf.arch.json.typeadapter.number.PercentageFloatTypeAdapter
import com.xxf.arch.json.datastructure.ListOrSingle
import com.xxf.arch.json.datastructure.ListOrSingle.ListOrSingleTypeAdapter
import com.xxf.arch.json.datastructure.ListOrEmpty
import com.xxf.arch.json.datastructure.ListOrEmpty.ListOrEmptyTypeAdapter
import com.xxf.arch.json.datastructure.IntEnum
import com.xxf.arch.json.datastructure.LongEnum
import com.xxf.arch.json.exclusionstrategy.ExposeDeserializeExclusionStrategy
import com.xxf.arch.json.exclusionstrategy.ExposeSerializeExclusionStrategy

object GsonFactory {
    /**
     * 自定义gson
     * @param excludeUnSerializableField 去除不可以序列化的字段 也就是 @Expose(serialize  = false) 默认不去除
     * @param excludeUnDeserializableField 去除不可反序列化字段 @Expose(deserialize = false) 默认不去除
     * @return
     */
    @JvmStatic
    fun createGson(
        excludeUnSerializableField: Boolean = false,
        excludeUnDeserializableField: Boolean = false
    ): Gson {
        var newBuilder = GsonBuilder()
            .setLenient() // json宽松
            .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
            .setPrettyPrinting() // 调教格式
            .disableHtmlEscaping() //默认是GSON把HTML 转义的
            //不使用 与relam 插入更新违背
            //  .registerTypeAdapter(String.class, new StringNullAdapter())//将空字符串转换成""
            .registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
            .registerTypeAdapter(Int::class.java, IntegerTypeAdapter())
            .registerTypeAdapter(Int::class.javaPrimitiveType, IntegerTypeAdapter())
            .registerTypeAdapter(Long::class.java, LongTypeAdapter())
            .registerTypeAdapter(Double::class.java, PercentageDoubleTypeAdapter())
            .registerTypeAdapter(Double::class.javaPrimitiveType, PercentageDoubleTypeAdapter())
            .registerTypeAdapter(Float::class.java, PercentageFloatTypeAdapter())
            .registerTypeAdapter(Float::class.javaPrimitiveType, PercentageFloatTypeAdapter())
            .registerTypeAdapter(ListOrSingle::class.java, ListOrSingleTypeAdapter<Any?>())
            .registerTypeAdapter(ListOrEmpty::class.java, ListOrEmptyTypeAdapter<Any?>())
            .registerTypeAdapterFactory(IntEnum.INT_ENUM_FACTORY)
            .registerTypeAdapterFactory(LongEnum.LONG_ENUM_FACTORY)
        if (excludeUnSerializableField) {
            newBuilder =
                newBuilder.addSerializationExclusionStrategy(ExposeSerializeExclusionStrategy())
        }
        if (excludeUnDeserializableField) {
            newBuilder =
                newBuilder.addDeserializationExclusionStrategy(ExposeDeserializeExclusionStrategy())
        }
        return newBuilder.create()
    }
}