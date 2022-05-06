package com.xxf.serialization.demo.model.cryo

import android.os.Parcel
import android.os.Parcelable

class SimpleModel() : Parcelable {
    var name:String?=null
    var age:Int?=null
    var subNode:List<String>?=null
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

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        age = parcel.readValue(Int::class.java.classLoader) as? Int
        subNode = parcel.createStringArrayList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeValue(age)
        parcel.writeStringList(subNode)
    }

    override fun describeContents(): Int {
        return 0
    }

//    override fun toString(): String {
//        return "SimpleModel(name=$name, age=$age, subNode=$subNode)"
//    }

    companion object CREATOR : Parcelable.Creator<SimpleModel> {
        override fun createFromParcel(parcel: Parcel): SimpleModel {
            return SimpleModel(parcel)
        }

        override fun newArray(size: Int): Array<SimpleModel?> {
            return arrayOfNulls(size)
        }
    }


}