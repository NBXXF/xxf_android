package com.xxf.arch.test

import com.xxf.arch.json.JsonUtils

object TestNumber {
    class TestModel {
        var num: Float? = null
        override fun toString(): String {
            return "TestModel(num=$num)"
        }
    }

    fun test() {
        println("===============>test cacse 8.8f================")
       test(8.8f)

        println("===============>test cacse 8.6f================")
        test(8.6f)


        println("===============>test cacse 78.6f================")
        test(78.6f)

        println("===============>test cacse 8.800000000001f================")
        test(8.800000000001f)
    }

    fun test(float:Float)
    {
        val apply = TestModel().apply {
            this.num = float
        }
        val json = JsonUtils.toJsonObject(apply)
        println("==============>ser json:$json")


        val model = JsonUtils.toBean(json, TestModel::class.java)
        println("==============>deser model:$model")

        if (model.num == apply.num) {
            println("===========>反序列化 相等")
        } else {
            println("===========>反序列化 不相等")
        }
    }
}