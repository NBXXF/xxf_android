package com.xxf.json.exclusionstrategy

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.annotations.Expose

/**
 * 去除 指定deserialize=false 的字段
 */
class ExposeDeserializeExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        return f?.getAnnotation(Expose::class.java)?.deserialize == false
    }

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }
}