package com.xxf.log.parser.impl

import com.xxf.log.parser.LINE_SEPARATOR
import com.xxf.log.parser.Parser

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 解析器
 * @date createTime：2018/9/7
 */
internal class MapParser : Parser<Map<*, *>> {
    override fun parseClassType(): Class<Map<*, *>> {
        return Map::class.java
    }

    override fun parseString(map: Map<*, *>): String? {
        val msg = StringBuilder(map.javaClass.name + " [" + LINE_SEPARATOR)
        val keys = map.keys
        for (key in keys) {
            val itemString = "%s -> %s$LINE_SEPARATOR"
            var value = map[key]
            if (value != null) {
                if (value is String) {
                    value = "\"" + value + "\""
                } else if (value is Char) {
                    value = "\'" + value + "\'"
                }
            }
            msg.append(
                java.lang.String.format(
                    itemString, "$key",
                    "${(value)}"
                )
            )
        }
        return "$msg]"
    }
}