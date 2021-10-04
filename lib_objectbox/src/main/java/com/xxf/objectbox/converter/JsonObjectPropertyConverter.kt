package com.xxf.objectbox.converter

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.objectbox.converter.PropertyConverter

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/10/4
 * Description ://JsonObject db 转换
 */
open class JsonObjectPropertyConverter : PropertyConverter<JsonObject?, String?> {

    override fun convertToEntityProperty(databaseValue: String?): JsonObject? {
        if (!TextUtils.isEmpty(databaseValue)) {
            return Gson().fromJson(JsonPrimitive(databaseValue).asString, JsonObject::class.java)
        }
        return null
    }

    override fun convertToDatabaseValue(entityProperty: JsonObject?): String? {
        if (entityProperty != null) {
            return entityProperty.toString()
        }
        return null
    }

}