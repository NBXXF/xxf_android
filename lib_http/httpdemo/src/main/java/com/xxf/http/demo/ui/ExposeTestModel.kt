package com.xxf.http.demo.ui

import com.google.gson.annotations.Expose

class ExposeTestModel {
    var name: String? = null

    @Expose(serialize  = false)
   // @Expose(deserialize = false)
  //  @Expose(serialize  = false,deserialize = false)
    var des: String? = null
    override fun toString(): String {
        return "ExposeTestModel(name=$name, des=$des)"
    }

}