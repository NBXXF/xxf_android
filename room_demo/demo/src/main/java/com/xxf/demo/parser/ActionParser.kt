package com.xxf.demo.parser


import com.xxf.demo.model.ActionPair
import java.util.function.Function
/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2023/11/29
 */

internal val dispatchList: MutableList<Function<String?, ActionPair?>> by lazy {
    mutableListOf()
}

fun <T : Function<String?, ActionPair?>> addParser(parser: T) {
    dispatchList.add(0, parser)
}

fun <T : Function<String?, ActionPair?>> removeParser(parser: T):Boolean {
    return dispatchList.remove(parser)
}

fun String.parseAction(): ActionPair? {
    dispatchList.forEach {
        it.apply(this)?.let { pair ->
            return pair
        }
    }
    return null
}
