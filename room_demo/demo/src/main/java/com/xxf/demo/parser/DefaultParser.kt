package com.xxf.demo.parser

import android.text.TextUtils
import com.xxf.demo.model.Action
import com.xxf.demo.model.ActionPair
import java.util.function.Function
/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2023/11/29
 */
class DefaultParser : Function<String?, ActionPair?> {
    override fun apply(t: String?): ActionPair? {
        if (!TextUtils.isEmpty(t)) {
            return Action.values().firstOrNull {
                t!!.contains(it.value,true)
            }?.let {
                val split = t!!.split(it.value)
                return ActionPair(split[0].toBigDecimal() to split[1].toBigDecimal(), it)
            }
        }
        return null
    }
}