package com.xxf.arch.json.datastructure;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.xxf.arch.json.typeadapter.enums.EnumTypeAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/26
 * Description ://是数字的枚举
 * <p>
 * 配合     @SerializedName("1")  自动序列化成整数[框架默认序列化成字符串]
 */
public interface IntEnum {
    class IntEnumTypeAdapter<T extends Enum<T>> extends EnumTypeAdapter<T> {
        public IntEnumTypeAdapter(Class<T> classOfT) {
            super(classOfT);
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            out.value(value == null ? null : Integer.valueOf(constantToName.get(value)));
        }
    }
}
