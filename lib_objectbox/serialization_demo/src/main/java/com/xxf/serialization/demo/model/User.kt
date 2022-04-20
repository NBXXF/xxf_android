package com.xxf.serialization.demo.model

class User : java.io.Serializable {
    var name: String = "xxx"
    var age: Int = 23

    var int1 = 0
    var int2 = 0
    var int3 = 0
    var int4 = 0

    var str1 = "x"
    var str2 = "x"
    var str3 = "x"
    var str4 = "x"


    var bool1 = true
    var bool2 = true
    var bool3 = true
    var bool4 = true
    var subNodes:List<String> = listOf()
    var map:Map<String,String> = mapOf()
    override fun toString(): String {
        return "User(name='$name', age=$age, subNodes=$subNodes, map=$map)"
    }

}