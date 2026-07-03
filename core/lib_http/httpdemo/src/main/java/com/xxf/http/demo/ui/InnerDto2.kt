package com.xxf.http.demo.ui

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
class InnerDto2 {
    @Json(name="name")
    var name: String? = null
    @Json(name="des")
    var des: String? = null

//    @Json(name="subNodes")
//    var subNodes:List<String>?=null
//    @Json(name="mp")
//    var mp:Map<String,String>? =null
}