package com.xxf.arch.json.exclusionstrategy

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.annotations.Expose

/**
 * 去除 指定serialize=false 的字段
 */
class ExposeSerializeExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        return f?.getAnnotation(Expose::class.java)?.serialize == false
    }

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }
}