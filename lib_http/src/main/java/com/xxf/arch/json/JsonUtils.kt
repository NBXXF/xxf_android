package com.xxf.arch.json

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * ClassName JsonUtils
 * Description  json处理工具类，暂时核心封装Gson解析方式
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date 创建时间：2015/6/17 9:43
 * version
 */
object JsonUtils {

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
        return gsonCache.getOrPut(key, {
            GsonFactory.createGson(excludeUnSerializableField, excludeUnDeserializableField)
        })
    }

    /**
     * @param excludeUnSerializableField 去除不可以序列化的字段 也就是 @Expose(serialize  = false) 默认不去除
     */
    @Throws(JsonParseException::class)
    fun toJsonObject(obj: Any?, excludeUnSerializableField: Boolean = false): JsonObject {
        return getGson(excludeUnSerializableField = excludeUnSerializableField).toJsonTree(obj) as JsonObject
    }

    /**
     *@param excludeUnSerializableField 去除不可以序列化的字段 也就是 @Expose(serialize  = false) 默认不去除
     */
    @Throws(JsonParseException::class)
    fun <T> toJsonArray(obj: List<T>?, excludeUnSerializableField: Boolean = false): JsonArray {
        return getGson(excludeUnSerializableField = excludeUnSerializableField).toJsonTree(obj) as JsonArray
    }

    /**
     *@param excludeUnSerializableField 去除不可以序列化的字段 也就是 @Expose(serialize  = false) 默认不去除
     */
    @Throws(JsonParseException::class)
    fun toJsonElement(obj: Any?, excludeUnSerializableField: Boolean = false): JsonElement {
        return getGson(excludeUnSerializableField = excludeUnSerializableField).toJsonTree(obj)
    }

    /**
     *@param excludeUnSerializableField 去除不可以序列化的字段 也就是 @Expose(serialize  = false) 默认不去除
     */
    @JvmStatic
    @Throws(JsonParseException::class)
    fun toJsonString(obj: Any?, excludeUnSerializableField: Boolean = false): String {
        //⚠️ 有转义字符了 增加了引号,不建议用gson.toJson(object);
        return toJsonElement(
            obj,
            excludeUnSerializableField = excludeUnSerializableField
        ).toString()
    }

    /**
     *@param excludeUnSerializableField 去除不可以序列化的字段 也就是 @Expose(serialize  = false) 默认不去除
     */
    fun toJsonStringSafe(obj: Any?, excludeUnSerializableField: Boolean = false): String? {
        try {
            return toJsonString(obj, excludeUnSerializableField = excludeUnSerializableField)
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * @param excludeUnDeserializableField 去除不可反序列化字段 @Expose(deserialize = false) 默认不去除
     */
    @Throws(JsonParseException::class)
    fun <T> toType(
        json: String?,
        typeOfT: Type?,
        excludeUnDeserializableField: Boolean = false
    ): T {
        return getGson(excludeUnDeserializableField = excludeUnDeserializableField).fromJson(
            json,
            typeOfT
        )
    }

    /**
     * @param excludeUnDeserializableField 去除不可反序列化字段 @Expose(deserialize = false) 默认不去除
     */
    @Throws(JsonParseException::class)
    fun <T> toType(
        json: JsonElement?,
        typeOfT: Type?,
        excludeUnDeserializableField: Boolean = false
    ): T {
        return getGson(excludeUnDeserializableField = excludeUnDeserializableField).fromJson(
            json,
            typeOfT
        )
    }

    /**
     * @param excludeUnDeserializableField 去除不可反序列化字段 @Expose(deserialize = false) 默认不去除
     */
    @Throws(JsonParseException::class)
    fun <T> toBean(
        json: String?,
        tClass: Class<T>?,
        excludeUnDeserializableField: Boolean = false
    ): T {
        return getGson(excludeUnDeserializableField = excludeUnDeserializableField).fromJson(
            json,
            tClass
        )
    }

    /**
     * 解析范型类  eg. User<T> ; you can use JsonUtils.toBean(json,User.class,T.class)
     *
     *
     * public static class User<T> {
     * private T t;
    </T> *
     *
     * public User(T t) {
     * this.t = t;
     * }
     * }
     *
     * @param json
     * @param tClass        注意不能是成员类或者方法类,可以是静态内部类或者直接声明.java文件的类
     * @param argumentsType 范型列表
     * @param excludeUnDeserializableField 去除不可反序列化字段 @Expose(deserialize = false) 默认不去除
     * @param <T>
     * @return
     * @throws JsonParseException 解析范型类  eg. User<T> ; you can use JsonUtils.toBean(json,User.class,T.class)
    </T></T></T> */
    @Throws(JsonParseException::class)
    fun <T> toBean(
        json: String?,
        tClass: Class<T>?,
        vararg argumentsType: Class<*>?,
        excludeUnDeserializableField: Boolean = false
    ): T {
        val type = TypeToken.getParameterized(tClass, *argumentsType).type
        return toType(json, type, excludeUnDeserializableField = excludeUnDeserializableField)
    }

    /**
     *  @param excludeUnDeserializableField 去除不可反序列化字段 @Expose(deserialize = false) 默认不去除
     */
    @Throws(JsonParseException::class)
    fun <T> toBean(
        json: JsonElement?,
        tClass: Class<T>?,
        excludeUnDeserializableField: Boolean = false
    ): T {
        return getGson(excludeUnDeserializableField = excludeUnDeserializableField).fromJson(
            json,
            tClass
        )
    }

    /**
     * 解析范型类  eg. User<T> ; you can use JsonUtils.toBean(json,User.class,T.class)
     *
     *
     * public static class User<T> {
     * private T t;
    </T> *
     *
     * public User(T t) {
     * this.t = t;
     * }
     * }
     *
     * @param json
     * @param tClass        注意不能是成员类或者方法类,可以是静态内部类或者直接声明.java文件的类
     * @param argumentsType 范型列表
     *  @param excludeUnDeserializableField 去除不可反序列化字段 @Expose(deserialize = false) 默认不去除
     * @param <T>
     * @return
     * @throws JsonParseException 解析范型类  eg. User<T> ; you can use JsonUtils.toBean(json,User.class,T.class)
    </T></T></T> */
    @Throws(JsonParseException::class)
    fun <T> toBean(
        json: JsonElement?,
        tClass: Class<T>?,
        vararg argumentsType: Class<*>?,
        excludeUnDeserializableField: Boolean = false
    ): T {
        val type = TypeToken.getParameterized(tClass, *argumentsType).type
        return toType(json, type, excludeUnDeserializableField = excludeUnDeserializableField)
    }

    /**
     *  @param excludeUnDeserializableField 去除不可反序列化字段 @Expose(deserialize = false) 默认不去除
     */
    @Throws(JsonParseException::class)
    fun <T> toBeanList(
        json: String?,
        typeToken: ListTypeToken<T>,
        excludeUnDeserializableField: Boolean = false
    ): List<T> {
        return getGson(excludeUnDeserializableField = excludeUnDeserializableField).fromJson(
            json,
            typeToken.type
        )
    }

    /**
     * 解析集合
     * List<T> lst = new ArrayList<T>();
     * JsonArray array = new JsonParser().parse(json).getAsJsonArray();
     * for (final JsonElement elem : array) {
     * lst.add(gson.fromJson(elem, classz));
     * }
     * return lst;
     *
     * @param json
     * @param classz
     *  @param excludeUnDeserializableField 去除不可反序列化字段 @Expose(deserialize = false) 默认不去除
     * @param <T>
     * @return
     * @throws JsonParseException
    </T></T></T> */
    @JvmStatic
    @Throws(JsonParseException::class)
    fun <T> toBeanList(
        json: String?,
        classz: Class<T>?,
        excludeUnDeserializableField: Boolean = false
    ): List<T> {
        val arr: Array<T> =
            getGson(excludeUnDeserializableField = excludeUnDeserializableField).fromJson(
                json,
                TypeToken.getArray(classz).type
            )
                ?: return ArrayList()
        return Arrays.asList(*arr)
    }

    /**
     *  @param excludeUnDeserializableField 去除不可反序列化字段 @Expose(deserialize = false) 默认不去除
     */
    @Throws(JsonParseException::class)
    fun <K, V> toMap(
        json: String?,
        typeToken: MapTypeToken<K, V>,
        excludeUnDeserializableField: Boolean = false
    ): Map<K, V> {
        return getGson(excludeUnDeserializableField = excludeUnDeserializableField).fromJson(
            json,
            typeToken.type
        )
    }
}