package com.xxf.log.parser.impl

import android.content.Intent
import android.text.TextUtils
import com.xxf.log.parser.LINE_SEPARATOR
import com.xxf.log.parser.Parser

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 解析器
 * @date createTime：2018/9/7
 */
internal class IntentParser : Parser<Intent> {

    override fun parseClassType(): Class<Intent> {
        return Intent::class.java
    }

    override fun parseString(intent: Intent): String? {
        val builder = StringBuilder(parseClassType().simpleName + " [" + LINE_SEPARATOR)
        builder.append(java.lang.String.format("%s = %s$LINE_SEPARATOR", "Scheme", intent.scheme))
        builder.append(java.lang.String.format("%s = %s$LINE_SEPARATOR", "Action", intent.action))
        builder.append(
            java.lang.String.format(
                "%s = %s$LINE_SEPARATOR",
                "DataString",
                intent.dataString
            )
        )
        builder.append(java.lang.String.format("%s = %s$LINE_SEPARATOR", "Type", intent.type))
        builder.append(
            java.lang.String.format(
                "%s = %s$LINE_SEPARATOR",
                "Package",
                intent.getPackage()
            )
        )
        builder.append(
            java.lang.String.format(
                "%s = %s$LINE_SEPARATOR",
                "ComponentInfo",
                intent.component
            )
        )
        builder.append(
            java.lang.String.format(
                "%s = %s$LINE_SEPARATOR",
                "Flags",
                getFlags(intent.flags)
            )
        )
        builder.append(
            java.lang.String.format(
                "%s = %s$LINE_SEPARATOR",
                "Categories",
                intent.categories
            )
        )
        val extras = intent.extras
        if (extras != null) {
            builder.append(
                java.lang.String.format(
                    "%s = %s$LINE_SEPARATOR", "Extras",
                    BundleParser().parseString(extras)
                )
            )
        }
        return "$builder]"
    }

    /**
     * 获取flag列表
     * 感谢涛哥提供的方法(*^__^*)
     *
     * @param flags
     * @return 包含Flag列表转字符串
     */
    private fun getFlags(flags: Int): String {
        val builder = StringBuilder()
        for (flagKey in FLAG_MAP.keys) {
            if (flagKey and flags == flagKey) {
                builder.append(FLAG_MAP[flagKey])
                builder.append(" | ")
            }
        }
        if (TextUtils.isEmpty(builder.toString())) {
            builder.append(flags)
        } else if (builder.indexOf("|") != -1) {
            builder.delete(builder.length - 2, builder.length)
        }
        return builder.toString()
    }

    companion object {
        private val FLAG_MAP: MutableMap<Int, String> = HashMap()

        init {
            val cla: Class<*> = Intent::class.java
            val fields = cla.declaredFields
            for (field in fields) {
                field.isAccessible = true
                if (field.name.startsWith("FLAG_")) {
                    var value = 0
                    try {
                        val `object` = field[cla]
                        if (`object` is Int || `object`.javaClass.simpleName == "int") {
                            value = `object` as Int
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (!FLAG_MAP.containsKey(value)) {
                        FLAG_MAP[value] = field.name
                    }
                }
            }
        }
    }
}