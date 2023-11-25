package com.xxf.log.parser.impl

import android.annotation.SuppressLint
import com.xxf.log.parser.LINE_SEPARATOR
import com.xxf.log.parser.Parser

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 解析器
 * @date createTime：2018/9/7
 */
internal class CollectionParser : Parser<Collection<*>> {
    override fun parseClassType(): Class<Collection<*>> {
        return Collection::class.java
    }

    @SuppressLint("DefaultLocale")
    override fun parseString(collection: Collection<*>): String? {
        val simpleName = collection.javaClass.name
        val msg = StringBuilder(
            java.lang.String.format(
                "%s size = %d [$LINE_SEPARATOR", simpleName,
                collection.size
            )
        )
        if (!collection.isEmpty()) {
            val iterator = collection.iterator()
            var flag = 0
            while (iterator.hasNext()) {
                val itemString = "[%d]:%s%s"
                val item = iterator.next()!!
                msg.append(
                    java.lang.String.format(
                        itemString, flag, "$item",
                        if (flag++ < collection.size - 1) ",$LINE_SEPARATOR" else LINE_SEPARATOR
                    )
                )
            }
        }
        return "$msg]"
    }
}