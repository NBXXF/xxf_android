package com.xxf.objectbox.converter

import com.google.gson.Gson
import com.google.gson.JsonParseException
import io.objectbox.converter.PropertyConverter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/23/19 10:50 AM
 * Description: 模型作为属性 转换
 */
class BeanPropertyConverter<T> : PropertyConverter<T, String?> {
    override fun convertToEntityProperty(databaseValue: String?): T {
        var type: Type? = null
        type = try {
            (javaClass.genericSuperclass as ParameterizedType?)!!.actualTypeArguments[0]
        } catch (e: Exception) {
            e.printStackTrace()
            throw JsonParseException("获取嵌套tClass异常")
        }
        return Gson().fromJson(databaseValue, type)
    }

    override fun convertToDatabaseValue(entityProperty: T): String? {
        return Gson().toJson(entityProperty)
    }
}