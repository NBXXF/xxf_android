package com.xxf.http.demo.ui

import com.google.gson.annotations.SerializedName

class EnumClass {
    var type: Type? = null

    enum class Type {
        @SerializedName("1")
        TYPE_1,

        @SerializedName("2")
        TYPE2,

        @SerializedName("3")
        TYPE_3,

        @SerializedName("4")
        TYPE4,
        @SerializedName("5")
        TYPE5,
        @SerializedName("6")
        TYPE6,
        @SerializedName("7")
        TYPE7,
        @SerializedName("8")
        TYPE8,
        @SerializedName("9")
        TYPE9,
        @SerializedName("10")
        TYPE10,
        @SerializedName("11")
        TYPE11,
        @SerializedName("12")
        TYPE12,
        @SerializedName("13")
        TYPE13,
        @SerializedName("14")
        TYPE14,
        @SerializedName("15")
        TYPE15,
        @SerializedName("16")
        TYPE16,
        @SerializedName("17")
        TYPE17,
        @SerializedName("18")
        TYPE18,
        @SerializedName("19")
        TYPE19,
        @SerializedName("20")
        TYPE20
    }
}