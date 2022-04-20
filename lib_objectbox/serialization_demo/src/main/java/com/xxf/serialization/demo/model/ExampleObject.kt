package com.xxf.serialization.demo.model

import kotlin.Throws
import com.twitter.serial.serializer.SerializationContext
import com.twitter.serial.stream.SerializerOutput
import com.twitter.serial.stream.SerializerInput
import com.twitter.serial.serializer.CoreSerializers
import com.twitter.serial.serializer.ObjectSerializer
import com.twitter.serial.serializer.Serializer
import com.xxf.objectbox.binserial.BinSerializers
import java.io.IOException

/**
 * 二进制序列化 demo
 */
class ExampleObject() {
    companion object {
        val BIN_SERIALIZER: ObjectSerializer<ExampleObject> = ExampleObjectSerializer()
        var BIN_LIST_STRING_SERIALIZER: Serializer<MutableList<String>> =
            BinSerializers.getListSerializer(CoreSerializers.STRING)
        var BIN_MAP_STRTING_Serializer: Serializer<MutableMap<String, String>> =
            BinSerializers.getMapSerializer(CoreSerializers.STRING, CoreSerializers.STRING)
    }
    var age: Int = 0
    var name: String? = null
    var subNodes: MutableList<String>? = null
    var map: MutableMap<String, String>? = null
    var int1 = 0
    var int2 = 0
    var int3 = 0
    var int4 = 0
    var str1: String? = "x"
    var str2: String? = "x"
    var str3: String? = "x"
    var str4: String? = "x"
    var bool1 = true
    var bool2 = true
    var bool3 = true
    var bool4 = true
    override fun toString(): String {
        return "ExampleObject{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", subNodes=" + subNodes +
                ", map=" + map +
                '}'
    }

    private class ExampleObjectSerializer : ObjectSerializer<ExampleObject>() {
        override fun serializeObject(
            context: SerializationContext,
            output: SerializerOutput<out SerializerOutput<*>>,
            obj: ExampleObject
        ) {
            output
                .writeInt(obj.age) // first field
                .writeString(obj.name)
            output.writeObject(context, obj.subNodes, BIN_LIST_STRING_SERIALIZER);
            output.writeObject(context, obj.map, BIN_MAP_STRTING_Serializer) // second field
                .writeInt(obj.int1)
                .writeInt(obj.int2)
                .writeInt(obj.int3)
                .writeInt(obj.int4)
                .writeString(obj.str1)
                .writeString(obj.str2)
                .writeString(obj.str3)
                .writeString(obj.str4)
                .writeBoolean(obj.bool1)
                .writeBoolean(obj.bool2)
                .writeBoolean(obj.bool3)
                .writeBoolean(obj.bool4)
        }


        @Throws(IOException::class, ClassNotFoundException::class)
        override fun deserializeObject(
            context: SerializationContext,
            input: SerializerInput,
            versionNumber: Int
        ): ExampleObject {
            val exampleObject = ExampleObject()
            val age = input.readInt() // first field
            val obj = input.readString() // second field
            val strings = input.readObject(context, BIN_LIST_STRING_SERIALIZER)
            val stringStringMap = input.readObject(context, BIN_MAP_STRTING_Serializer)
            val int1 = input.readInt()
            val int2 = input.readInt()
            val int3 = input.readInt()
            val int4 = input.readInt()
            val str1 = input.readString()
            val str2 = input.readString()
            val str3 = input.readString()
            val str4 = input.readString()
            val bool1 = input.readBoolean()
            val bool2 = input.readBoolean()
            val bool3 = input.readBoolean()
            val bool4 = input.readBoolean()

            exampleObject.age = age
            exampleObject.name = obj
            exampleObject.subNodes = strings
            exampleObject.map = stringStringMap

            exampleObject.int1 = int1
            exampleObject.int2 = int2
            exampleObject.int3 = int3
            exampleObject.int4 = int4
            exampleObject.str1 = str1
            exampleObject.str2 = str2
            exampleObject.str3 = str3
            exampleObject.str4 = str4
            exampleObject.bool1 = bool1
            exampleObject.bool2 = bool2
            exampleObject.bool3 = bool3
            exampleObject.bool4 = bool4
            return exampleObject
        }
    }
}