package com.xxf.http.demo

import com.google.gson.annotations.SerializedName
import com.xxf.arch.json.datastructure.IntEnum
import com.xxf.arch.json.datastructure.LongEnum
/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/26
 * Description ://TODO
 */
class TestDTO(val name: String, val type: Type,val type2:Type2) {

   enum class Type(val v: Int) :IntEnum
   {
        @SerializedName("1")
        TYPE_A(1),

        @SerializedName("2")
        TYPE_B(2);

        override fun toString(): String {
            return "Type(v=$v)"
        }
    }


    enum class Type2(val v: Long):LongEnum {
        @SerializedName("1")
        TYPE_A2(1),

        @SerializedName("2")
        TYPE_B2(2);

        override fun toString(): String {
            return "Type2(v=$v)"
        }
    }

    override fun toString(): String {
        return "TestDTO(name='$name', type=$type, type2=$type2)"
    }


}