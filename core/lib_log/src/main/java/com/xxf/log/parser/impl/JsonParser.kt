package com.xxf.log.parser.impl

import com.xxf.log.parser.Parser
import org.json.JSONArray
import org.json.JSONObject

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 解析器
 * @date createTime：2018/9/7
 */
class JsonParser : Parser<String> {
    override fun parseClassType(): Class<String> {
        return String::class.java
    }

    override fun parseString(json: String): String? {
        var formattedString: String? = null
        if (json == null || json.trim { it <= ' ' }.isEmpty()) {
            return ""
        }
        formattedString = try {
            if (json.startsWith("{")) {
                val jsonObject = JSONObject(json)
                jsonObject.toString(JSON_INDENT)
            } else if (json.startsWith("[")) {
                val jsonArray = JSONArray(json)
                jsonArray.toString(JSON_INDENT)
            } else {
                return json
            }
        } catch (e: Exception) {
            return json
        }
        return formattedString
    }

    companion object {
        private const val JSON_INDENT = 4
    }
}