package com.xxf.http.demo.ui

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class InnerDto2 {
    var name: String? = null
    var des: String? = null
}