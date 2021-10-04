package com.xxf.objectbox.converter

import android.text.TextUtils
import com.google.gson.JsonObject
import io.objectbox.converter.PropertyConverter
import org.json.JSONArray
import org.json.JSONObject

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/10/4
 * Description ://JSONArray db 转换
 */
open class JsonArrayOrgPropertyConverter : PropertyConverter<JSONArray?, String?> {
    override fun convertToDatabaseValue(entityProperty: JSONArray?): String? {
        if (entityProperty != null) {
            return entityProperty.toString()
        }
        return null
    }

    override fun convertToEntityProperty(databaseValue: String?): JSONArray? {
        if (!TextUtils.isEmpty(databaseValue)) {
            return JSONArray(databaseValue)
        }
        return null
    }

}