package com.xxf.json

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.io.Reader
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType

/**
 * Description  为gson 增加kt 函数方式,更简洁
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date 创建时间：2023/11/29 9:43
 * version
 */


object Json {

    /**
     * 初始化默认的 [com.google.gson.Gson] 转换器
     */
    var innerDefaultGson: Gson = GsonFactory.createGson()

    inline fun <reified T : Any> fromJson(
        json: String,
        noinline gsonBuilder: ((gson: Gson) -> Gson) = { it }
    ): T = gsonBuilder(
        innerDefaultGson
    ).fromJson(json, typeToken<T>())

    inline fun <reified T : Any> fromJson(
        json: Reader,
        noinline gsonBuilder: ((gson: Gson) -> Gson) = { it }
    ): T = gsonBuilder(
        innerDefaultGson
    ).fromJson(json, typeToken<T>())

    inline fun <reified T : Any> fromJson(
        json: JsonReader,
        noinline gsonBuilder: ((gson: Gson) -> Gson) = { it }
    ): T = gsonBuilder(
        innerDefaultGson
    ).fromJson(json, typeToken<T>())

    inline fun <reified T : Any> fromJson(
        json: JsonElement,
        noinline gsonBuilder: ((gson: Gson) -> Gson) = { it }
    ): T = gsonBuilder(
        innerDefaultGson
    ).fromJson(json, typeToken<T>())

    inline fun toJson(
        obj: Any?,
        noinline gsonBuilder: ((gson: Gson) -> Gson) = { it }
    ): String =
        gsonBuilder(
            innerDefaultGson
        ).toJson(obj)


    inline fun toJsonTree(
        obj: Any?,
        noinline gsonBuilder: ((gson: Gson) -> Gson) = { it }
    ): JsonElement = gsonBuilder(innerDefaultGson).toJsonTree(obj)

}


@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
inline fun <reified T : Any> typeToken(): Type {
    val type = gsonTypeToken<T>()

    if (type is ParameterizedType && type.isWildcard())
        return type.rawType

    return removeTypeWildcards(type)
}

@Suppress("PROTECTED_CALL_FROM_PUBLIC_INLINE")
inline fun <reified T : Any> gsonTypeToken(): Type = object : TypeToken<T>() {}.type

fun ParameterizedType.isWildcard(): Boolean {
    var hasAnyWildCard = false
    var hasBaseWildCard = false
    var hasSpecific = false

    val cls = this.rawType as Class<*>
    cls.typeParameters.forEachIndexed { i, variable ->
        val argument = actualTypeArguments[i]

        if (argument is WildcardType) {
            val hit = variable.bounds.firstOrNull { it in argument.upperBounds }
            if (hit != null) {
                if (hit == Any::class.java)
                    hasAnyWildCard = true
                else
                    hasBaseWildCard = true
            } else
                hasSpecific = true
        } else
            hasSpecific = true

    }

    if (hasAnyWildCard && hasSpecific)
        throw IllegalArgumentException("Either none or all type parameters can be wildcard in $this")

    return hasAnyWildCard || (hasBaseWildCard && !hasSpecific)
}

fun removeTypeWildcards(type: Type): Type {
    if (type is ParameterizedType) {
        val arguments = type.actualTypeArguments
            .map { if (it is WildcardType) it.upperBounds[0] else it }
            .map { removeTypeWildcards(it) }
            .toTypedArray()
        return TypeToken.getParameterized(type.rawType, *arguments).type
    }

    return type
}


/**
 * 深拷贝
 * 可以拷贝自己的类型 也可以
 * 父类和子类对象 可以双向转换复制
 */
inline fun <reified OUT : Any> Any.copy(
    noinline gsonBuilder: ((gson: Gson) -> Gson) = { it }
): OUT {
    val jsonStr: String = Json.toJson(this, gsonBuilder)
    return Json.fromJson<OUT>(jsonStr, gsonBuilder)
}
