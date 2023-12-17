package com.xxf.serialization.demo.model

import kotlin.Throws
import com.twitter.serial.serializer.SerializationContext
import com.twitter.serial.stream.SerializerOutput
import com.twitter.serial.stream.SerializerInput
import com.twitter.serial.serializer.CoreSerializers
import com.twitter.serial.serializer.ObjectSerializer
import com.twitter.serial.serializer.Serializer
import com.xxf.serialization.demo.BinSerializers
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
    var type:Type=Type.A27
    var type2:Type=Type.A27
    var type3:Type=Type.A27

    enum class Type(val value:String)
    {
        A("a"),
        B("b"),
        C("c"),
        D("d"),
        E("e"),
        F("f"),
        G("g"),
        H("h"),
        K("k"),
        L("l"),
        M("m"),
        N("n"),
        O("o"),
        P("p"),
        Q("q"),
        R("r"),
        S("s"),
        T("t"),
        U("u"),
        V("v"),
        W("w"),
        X("x"),
        Y("y"),
        Z("z"),
        A1("A1"),
        A2("A2"),
        A3("A3"),
        A4("A4"),
        A5("A5"),
        A6("A6"),
        A7("A7"),
        A8("A8"),
        A9("A9"),
        A10("A10"),
        A11("A11"),
        A12("A12"),
        A13("A13"),
        A14("A14"),
        A15("A15"),
        A16("A16"),
        A17("A17"),
        A18("A18"),
        A19("A19"),
        A20("A20"),
        A21("A21"),
        A22("A22"),
        A23("A23"),
        A24("A24"),
        A25("A25"),
        A26("A26"),
        A27("A27")
    }

    override fun toString(): String {
        return "ExampleObject{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ",type="+type+
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
                output.writeObject(context,obj.type,CoreSerializers.getEnumSerializer(Type::class.java))
            output.writeObject(context,obj.type2,CoreSerializers.getEnumSerializer(Type::class.java))
            output.writeObject(context,obj.type3,CoreSerializers.getEnumSerializer(Type::class.java))
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
            val type=input.readObject(context,CoreSerializers.getEnumSerializer(Type::class.java))
            val type2=input.readObject(context,CoreSerializers.getEnumSerializer(Type::class.java))
            val type3=input.readObject(context,CoreSerializers.getEnumSerializer(Type::class.java))

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
            exampleObject.type=type?:Type.A27
            exampleObject.type2=type?:Type.A27
            exampleObject.type3=type?:Type.A27
            return exampleObject
        }
    }
}