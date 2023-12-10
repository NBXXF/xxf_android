package com.xxf.json.exclusionstrategy

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.annotations.Expose

/**
 * 去除 指定deserialize=false 的字段 这种方式更友好 不用强制写Expose,让不写注解默认参与解析
 *
 * 避免默认的 强制必须写Expose才能生效
 * 且初始化必须 GsonBuilder()
 *     .excludeFieldsWithoutExposeAnnotation()
 *     .create()
 *  源码   com.google.gson.internal.Excluder#164
 */
class ExposeSerializeExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        return f?.getAnnotation(Expose::class.java)?.serialize == false
    }

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }
}