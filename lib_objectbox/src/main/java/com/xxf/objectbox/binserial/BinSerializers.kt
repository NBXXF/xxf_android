package com.xxf.objectbox.binserial

import com.twitter.serial.serializer.ObjectSerializer
import kotlin.Throws
import com.twitter.serial.serializer.SerializationContext
import com.twitter.serial.serializer.Serializer
import com.twitter.serial.stream.SerializerOutput
import com.twitter.serial.stream.SerializerInput
import com.twitter.serial.util.InternalSerialUtils
import com.twitter.serial.util.SerializationUtils
import java.io.IOException
import java.util.ArrayList
import java.util.LinkedHashMap

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/7/16 17:34
 * Description :二进制序列化工具
 * 文档地址参考:https://github.com/twitter/Serial/blob/master/README-CHINESE.rst
 */
object BinSerializers {
    /**
     * @param itemSerializer of the `T`
     * @param <T>            the object in the list.
     * @return a [Serializer] for list T.
    </T> */
    fun <T> getListSerializer(itemSerializer: Serializer<T>): Serializer<MutableList<T>> {
        return object : ObjectSerializer<MutableList<T>>() {
            @Throws(IOException::class)
            override fun serializeObject(
                context: SerializationContext,
                output: SerializerOutput<*>, list: MutableList<T>
            ) {
                serializeList(context, output, list, itemSerializer)
            }

            @Throws(IOException::class, ClassNotFoundException::class)
            override fun deserializeObject(
                context: SerializationContext,
                input: SerializerInput,
                versionNumber: Int
            ): MutableList<T> {
                return InternalSerialUtils.checkIsNotNull(
                    deserializeList(
                        context,
                        input,
                        itemSerializer
                    )
                )
            }
        }
    }

    @Throws(IOException::class)
    private fun <T> serializeList(
        context: SerializationContext,
        output: SerializerOutput<*>, list: List<T>?,
        serializer: Serializer<T>
    ) {
        if (!SerializationUtils.writeNullIndicator(output, list)) {
            output.writeInt(list!!.size)
            for (item in list) {
                serializer.serialize(context, output, item)
            }
        }
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun <T> deserializeList(
        context: SerializationContext,
        input: SerializerInput,
        serializer: Serializer<T>
    ): MutableList<T>? {
        if (SerializationUtils.readNullIndicator(input)) {
            return null
        }
        val size = input.readInt()
        val list: MutableList<T> = ArrayList((size / 0.75).toInt() + 1)
        for (i in 0 until size) {
            list.add(serializer.deserialize(context, input) as T)
        }
        return list
    }

    /**
     * 底层是LinkedHashmap
     * @param keySerializer   serializer for the key
     * @param valueSerializer serializer for the value
     * @return a [Serializer] for the map.
     */
    fun <K, V> getMapSerializer(
        keySerializer: Serializer<K>,
        valueSerializer: Serializer<V>
    ): Serializer<MutableMap<K, V>> {
        return object : ObjectSerializer<MutableMap<K, V>>() {
            @Throws(IOException::class)
            override fun serializeObject(
                context: SerializationContext,
                output: SerializerOutput<out SerializerOutput<*>>,
                map: MutableMap<K, V>
            ) {
                serializeMap(context, output, map, keySerializer, valueSerializer)
            }

            @Throws(IOException::class, ClassNotFoundException::class)
            override fun deserializeObject(
                context: SerializationContext,
                input: SerializerInput,
                versionNumber: Int
            ): MutableMap<K, V> {
                return InternalSerialUtils.checkIsNotNull(deserializeMap(context, input, keySerializer, valueSerializer))
            }
        }
    }

    @Throws(IOException::class)
    private fun <K, V> serializeMap(
        context: SerializationContext,
        output: SerializerOutput<*>, map: MutableMap<K, V>,
        keySerializer: Serializer<K>, valueSerializer: Serializer<V>
    ) {
        if (!SerializationUtils.writeNullIndicator(output, map)) {
            output.writeInt(map!!.size)
            for ((key, value) in map) {
                keySerializer.serialize(context, output, key)
                valueSerializer.serialize(context, output, value)
            }
        }
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun <K, V> deserializeMap(
        context: SerializationContext,
        input: SerializerInput,
        keySerializer: Serializer<K>,
        valueSerializer: Serializer<V>
    ): MutableMap<K, V>? {
        if (SerializationUtils.readNullIndicator(input)) {
            return null
        }
        val size = input.readInt()
        val map: MutableMap<K, V> = LinkedHashMap((size / 0.75).toInt() + 1)
        for (i in 0 until size) {
            val key = if (SerializationUtils.readNullIndicator(input)) null else input.readObject(
                context, keySerializer
            )
            val value = if (SerializationUtils.readNullIndicator(input)) null else input.readObject(
                context, valueSerializer
            )
            map[key!!] = value!!
        }
        return map
    }
}