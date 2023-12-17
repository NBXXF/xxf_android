package com.xxf.serialization.demo.model

import android.os.Parcel
import android.os.Parcelable
import kotlin.Throws
import com.twitter.serial.serializer.SerializationContext
import com.twitter.serial.stream.SerializerOutput
import com.twitter.serial.stream.SerializerInput
import com.twitter.serial.serializer.CoreSerializers
import com.twitter.serial.serializer.ObjectSerializer
import com.twitter.serial.serializer.Serializer
import java.io.IOException
import java.util.ArrayList
import java.util.LinkedHashMap

/**
 * 二进制序列化 demo
 */
class ExampleObject3() : Parcelable {

    var age: Int = 0
    var name: String? = null
    var subNodes: List<String>? = null
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
    var type:Type=Type.A

    constructor(parcel: Parcel) : this() {
        age = parcel.readInt()
        name = parcel.readString()
        subNodes = parcel.createStringArrayList()
        int1 = parcel.readInt()
        int2 = parcel.readInt()
        int3 = parcel.readInt()
        int4 = parcel.readInt()
        str1 = parcel.readString()
        str2 = parcel.readString()
        str3 = parcel.readString()
        str4 = parcel.readString()
        bool1 = parcel.readByte() != 0.toByte()
        bool2 = parcel.readByte() != 0.toByte()
        bool3 = parcel.readByte() != 0.toByte()
        bool4 = parcel.readByte() != 0.toByte()
    }

    enum class Type(val value:String)
    {
        A("A"),
        B("B")
    }

    override fun toString(): String {
        return "ExampleObject{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", subNodes=" + subNodes +
                ", map=" + map +
                '}'
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(age)
        parcel.writeString(name)
        parcel.writeStringList(subNodes)
        parcel.writeInt(int1)
        parcel.writeInt(int2)
        parcel.writeInt(int3)
        parcel.writeInt(int4)
        parcel.writeString(str1)
        parcel.writeString(str2)
        parcel.writeString(str3)
        parcel.writeString(str4)
        parcel.writeByte(if (bool1) 1 else 0)
        parcel.writeByte(if (bool2) 1 else 0)
        parcel.writeByte(if (bool3) 1 else 0)
        parcel.writeByte(if (bool4) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExampleObject3> {
        override fun createFromParcel(parcel: Parcel): ExampleObject3 {
            return ExampleObject3(parcel)
        }

        override fun newArray(size: Int): Array<ExampleObject3?> {
            return arrayOfNulls(size)
        }
    }


}