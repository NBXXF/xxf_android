package com.xxf.objectbox.converter;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.objectbox.converter.PropertyConverter;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/23/19 10:50 AM
 * Description: 模型作为属性 转换
 */
public class BeanPropertyConverter<T> implements PropertyConverter<T, String> {
    @Override
    public T convertToEntityProperty(String databaseValue) {
        Type type = null;
        try {
            type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (Exception e) {
            e.printStackTrace();
            throw new JsonParseException("获取嵌套tClass异常");
        }
        return new Gson().fromJson(databaseValue, type);
    }

    @Override
    public String convertToDatabaseValue(T entityProperty) {
        return new Gson().toJson(entityProperty);
    }
}
