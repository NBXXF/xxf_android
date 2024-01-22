package com.xxf.json.typeadapter.loop

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.xxf.json.gsonTypeToken
import java.lang.reflect.Type


/**
 * 为了解析不循环,也解决了
 *
 *         internal class NoLoopConfigsDTO(
 *             private var supportAccess1: Boolean? = null,
 *             private var flashGuest1: Boolean? = null
 *         ) : ConfigsDTO(supportAccess1, flashGuest1) {
 *
 *         }
 *
 *         override fun deserialize(
 *             json: JsonElement?,
 *             typeOfT: Type?,
 *             context: JsonDeserializationContext?
 *         ): ConfigsDTO? {
 *             return context?.deserializeUnLoop<NoLoopConfigsDTO>(json, typeOfT)
 *         }
 */
inline fun <reified T> JsonDeserializationContext.deserializeUnLoop(
    json: JsonElement?,
    typeOfT: Type?
): T {
    if (T::class.java == typeOfT) {
        throw JsonSyntaxException("为了不循环,可以采用内部类 class B:继承原始类A 这里传class B")
    }
    //解析 带双引号的问题
    return if (json?.isJsonPrimitive == true) {
        //去除双引号
        this.deserialize(
            //宽松模式解析
            JsonParser.parseString(json.asJsonPrimitive?.asString),
            gsonTypeToken<T>()
        )
    } else {
        this.deserialize(
            json,
            gsonTypeToken<T>()
        )
    }
}