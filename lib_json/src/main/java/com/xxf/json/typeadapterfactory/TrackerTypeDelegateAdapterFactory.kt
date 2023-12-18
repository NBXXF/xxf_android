package com.xxf.json.typeadapterfactory

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

/**
 * @Description gson监控异常
 * @Author: XGod  xuanyouwu@163.com  qq:2767356582  https://github.com/NBXXF/gson_plugin
 * https://blog.csdn.net/axuanqq
 * 将生成的TypeAdapterFactory注册到gson实例中 最早注册！！！
 * val gson = GsonBuilder()
 * .registerTypeAdapterFactory(TrackerTypeDelegateAdapterFactory())
 * .create()
 *
 *  采集信息 可以获取 reader.path type.rawType
 *
 *  缺点 不能拿到json
 *  否则需要反射获取reader.in
 *  还有 JsonTreeReader 的问题
 */
class TrackerTypeDelegateAdapterFactory(
    val writeCallBack: ((writer: JsonWriter, type: TypeToken<*>, e: Throwable) -> Unit)? = null,
    val readCallBack: (reader: JsonReader, type: TypeToken<*>, e: Throwable) -> Unit
) :
    TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
        val delegate = gson.getDelegateAdapter(this, type)
        return object : TypeAdapter<T>() {
            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: T) {
                try {
                    delegate.write(out, value)
                } catch (e: Throwable) {
                    writeCallBack?.let { it(out, type, e) }
                }
            }

            @Throws(IOException::class)
            override fun read(reader: JsonReader): T {
                return try {
                    delegate.read(reader)
                } catch (e: Throwable) {
                    //可以获取信息
                    //reader.path
                    //type.rawType
                    readCallBack(reader, type, e)
                    throw e
                }
            }
        }
    }
}