package com.xxf.json.typeadapterfactory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.xxf.json.datastructure.ListOrEmpty;
import com.xxf.json.datastructure.ListOrSingle;
import com.xxf.json.typeadapter.bool.BooleanTypeAdapter;
import com.xxf.json.typeadapter.number.IntegerTypeAdapter;
import com.xxf.json.typeadapter.number.LongTypeAdapter;
import com.xxf.json.typeadapter.number.PercentageDoubleTypeAdapter;
import com.xxf.json.typeadapter.number.PercentageFloatTypeAdapter;
import com.xxf.json.typeadapter.orgJSON.JSONArrayTypeAdapter;
import com.xxf.json.typeadapter.orgJSON.JSONObjectTypeAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @Description gson容错
 * @Author: XGod  xuanyouwu@163.com  qq:2767356582  https://github.com/NBXXF/gson_plugin
 * https://blog.csdn.net/axuanqq
 * 将生成的TypeAdapterFactory注册到gson实例中
 * val gson = GsonBuilder()
 * .registerTypeAdapterFactory(SafeTypeAdapterFactory())
 * .create()
 */
public class SafeTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (boolean.class.isAssignableFrom(typeToken.getRawType()) || Boolean.class.isAssignableFrom(typeToken.getRawType())) {
            return (TypeAdapter<T>) new BooleanTypeAdapter();
        }
        if (int.class.isAssignableFrom(typeToken.getRawType()) || Integer.class.isAssignableFrom(typeToken.getRawType())) {
            return (TypeAdapter<T>) new IntegerTypeAdapter();
        }
        if (long.class.isAssignableFrom(typeToken.getRawType()) || Long.class.isAssignableFrom(typeToken.getRawType())) {
            return (TypeAdapter<T>) new LongTypeAdapter();
        }
        if (double.class.isAssignableFrom(typeToken.getRawType()) || Double.class.isAssignableFrom(typeToken.getRawType())) {
            return (TypeAdapter<T>) new PercentageDoubleTypeAdapter();
        }
        if (float.class.isAssignableFrom(typeToken.getRawType()) || Float.class.isAssignableFrom(typeToken.getRawType())) {
            return (TypeAdapter<T>) new PercentageFloatTypeAdapter();
        }
        if (JSONObject.class.isAssignableFrom(typeToken.getRawType())) {
            return (TypeAdapter<T>) new JSONObjectTypeAdapter();
        }
        if (JSONArray.class.isAssignableFrom(typeToken.getRawType())) {
            return (TypeAdapter<T>) new JSONArrayTypeAdapter();
        }
        return null;
    }
}
