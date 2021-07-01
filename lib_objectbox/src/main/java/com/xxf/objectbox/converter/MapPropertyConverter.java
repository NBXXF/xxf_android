package com.xxf.objectbox.converter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import io.objectbox.converter.PropertyConverter;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/23/19 10:46 AM
 * Description: map 转换
 */
public class MapPropertyConverter implements PropertyConverter<Map<String, Object>, String> {

    @Override
    public Map<String, Object> convertToEntityProperty(String databaseValue) {
        if (TextUtils.isEmpty(databaseValue)) {
            return new HashMap<>();
        }
        return new Gson().fromJson(databaseValue, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    @Override
    public String convertToDatabaseValue(Map<String, Object> entityProperty) {
        return new Gson().toJson(entityProperty);
    }
}
