package com.xxf.objectbox.converter

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.objectbox.converter.PropertyConverter
import java.util.*

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/23/19 10:46 AM
 * Description: map 转换
 */
class MapPropertyConverter : PropertyConverter<Map<String, Any>, String?> {
    override fun convertToEntityProperty(databaseValue: String?): Map<String, Any> {
        return if (TextUtils.isEmpty(databaseValue)) {
            HashMap()
        } else Gson().fromJson(databaseValue, object : TypeToken<Map<String?, Any?>?>() {}.type)
    }

    override fun convertToDatabaseValue(entityProperty: Map<String, Any>): String? {
        return Gson().toJson(entityProperty)
    }
}