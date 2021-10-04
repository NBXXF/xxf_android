package com.xxf.objectbox.converter

import android.text.TextUtils
import com.google.gson.JsonObject
import io.objectbox.converter.PropertyConverter
import org.json.JSONObject

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/10/4
 * Description ://JSONObject db 转换
 */
open class JsonObjectOrgPropertyConverter : PropertyConverter<JSONObject?, String?> {
    override fun convertToDatabaseValue(entityProperty: JSONObject?): String? {
        if (entityProperty != null) {
            return entityProperty.toString()
        }
        return null
    }

    override fun convertToEntityProperty(databaseValue: String?): JSONObject? {
        if (!TextUtils.isEmpty(databaseValue)) {
            return JSONObject(databaseValue)
        }
        return null
    }

}