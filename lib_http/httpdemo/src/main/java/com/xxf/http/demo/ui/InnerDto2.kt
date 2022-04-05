package com.xxf.http.demo.ui

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
class InnerDto2 {
    var name: String? = null
    var des: String? = null
    var mp:Map<String,String>? =null
}