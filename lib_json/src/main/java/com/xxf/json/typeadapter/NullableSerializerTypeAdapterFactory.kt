package com.xxf.json.typeadapter

import com.google.gson.*
import com.google.gson.internal.Streams
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.xxf.json.JsonUtils

/**
 * Author: xiangning
 * Date: 2022/3/12 8:05 下午
 * Description:
 *  用法   @JsonAdapter(OnlySerializerNullable::class, nullSafe = false)
 *  字段为null 参与序列化 边框字典 数组里面的null
 */
class NullableSerializerTypeAdapterFactory : TypeAdapterFactory {

    override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T> {
        return object : TypeAdapter<T>() {
            override fun write(writer: JsonWriter?, value: T?) {
                val oldSerializeNulls = writer?.serializeNulls
                writer?.serializeNulls = true
                Streams.write(
                    JsonUtils.getGson(
                        excludeUnSerializableField = false,
                        excludeUnDeserializableField = false
                    )
                        .toJsonTree(value), writer
                )
                writer?.serializeNulls = oldSerializeNulls!!
            }

            override fun read(reader: JsonReader?): T? {
                val jsonElement = Streams.parse(reader)
                return Gson()
                    .fromJson(jsonElement, type!!.type)
            }

        }
    }

}