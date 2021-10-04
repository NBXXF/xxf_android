package com.xxf.objectbox.converter

import android.text.TextUtils
import com.google.gson.*
import io.objectbox.converter.PropertyConverter

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/10/4
 * Description ://JsonArray db 转换
 */
open class JsonArrayPropertyConverter : PropertyConverter<JsonArray?, String?> {

    override fun convertToEntityProperty(databaseValue: String?): JsonArray? {
        if (!TextUtils.isEmpty(databaseValue)) {
            return Gson().fromJson(JsonPrimitive(databaseValue).asString, JsonArray::class.java)
        }
        return null
    }

    override fun convertToDatabaseValue(entityProperty: JsonArray?): String? {
        if (entityProperty != null) {
            return entityProperty.toString()
        }
        return null
    }

}