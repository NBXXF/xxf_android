package com.xxf.demo.model

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2023/11/29
 */
enum class Action(val value: String) {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/")
}

data class ActionPair(val input: Pair<BigDecimal, BigDecimal>, val action: Action)

inline val ActionPair.result: BigDecimal
    get() = when (this.action) {
        Action.ADD -> this.input.first.plus(this.input.second)
        Action.SUBTRACT -> this.input.first.minus(this.input.second)
        Action.MULTIPLY -> this.input.first.times(this.input.second)
        Action.DIVIDE -> this.input.first.divide(this.input.second,20, RoundingMode.HALF_UP)
        else -> throw IllegalAccessException("暂不支持的运算符")
    }
