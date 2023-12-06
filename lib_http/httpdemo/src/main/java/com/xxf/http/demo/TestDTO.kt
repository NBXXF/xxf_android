package com.xxf.http.demo

import com.google.gson.annotations.SerializedName

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/26
 * Description ://TODO
 */
class TestDTO(val name: String, val type: Type, val type2: Type2,val gen:Gender) {

    enum class Type(val v: Int) {
        @SerializedName("1")
        TYPE_A(1),

        @SerializedName("2")
        TYPE_B(2);

        override fun toString(): String {
            return "Type(v=$v)"
        }
    }


    enum class Type2(val value: Long) {
        @SerializedName("1")
        TYPE_A2(1),

        @SerializedName("2")
        TYPE_B2(2);
    }

    enum class Gender(val v: Int) {
        @SerializedName("m")
        MAIL(3),

        @SerializedName("f")
        FEMAIL(4);

        override fun toString(): String {
            return "Gender(v=$v)"
        }
    }

    override fun toString(): String {
        return "TestDTO(name='$name', type=$type, type2=$type2, gen=$gen)"
    }


}