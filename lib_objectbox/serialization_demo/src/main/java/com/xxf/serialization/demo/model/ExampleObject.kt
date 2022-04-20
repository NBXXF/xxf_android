package com.xxf.serialization.demo.model

import com.xxf.objectbox.binserial.BinSerializers.getListSerializer
import com.xxf.objectbox.binserial.BinSerializers.getMapSerializer
import kotlin.Throws
import com.twitter.serial.serializer.SerializationContext
import com.twitter.serial.stream.SerializerOutput
import com.twitter.serial.stream.SerializerInput
import com.twitter.serial.serializer.CoreSerializers
import com.twitter.serial.serializer.ObjectSerializer
import com.twitter.serial.serializer.Serializer
import java.io.IOException

class ExampleObject() {
    var age: Int=0
    var name: String?=null
    var subNodes: MutableList<String> = mutableListOf()
    var map: MutableMap<String, String>?=null
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
        @Throws(IOException::class)
        override fun serializeObject(
            context: SerializationContext, output: SerializerOutput<*>,
            obj: ExampleObject
        ) {
            try {


            output
                .writeInt(obj.age) // first field
                .writeString(obj.name)
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
                .writeObject(context,obj.subNodes, listSerializer)
                .writeObject(context,obj.map, mapSerializer) // second field
            }catch (e:Throwable)
            {
                e.printStackTrace()
            }
        }

        @Throws(IOException::class, ClassNotFoundException::class)
        override fun deserializeObject(
            context: SerializationContext,
            input: SerializerInput,
            versionNumber: Int
        ): ExampleObject {
            val num = input.readInt() // first field
            val obj = input.readString() // second field
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
            val strings = input.readObject(context, listSerializer)!!
            val stringStringMap = input.readObject(context, mapSerializer)!!
            val exampleObject = ExampleObject(num, obj, strings, stringStringMap)
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

    companion object {
        val SERIALIZER: ObjectSerializer<ExampleObject> = ExampleObjectSerializer()
        var listSerializer: Serializer<MutableList<String>> = getListSerializer(CoreSerializers.STRING)
        var mapSerializer: Serializer<MutableMap<String, String>> =
            getMapSerializer(CoreSerializers.STRING, CoreSerializers.STRING)
    }
}