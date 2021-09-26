package com.xxf.arch.json.typeadapter.enums;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/26
 * Description ://处理枚举 copy from TypeAdapters
 */
public class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
    protected final Map<String, T> nameToConstant = new HashMap<String, T>();
    protected final Map<T, Integer> constantToName = new HashMap<T, Integer>();

    public EnumTypeAdapter(Class<T> classOfT) {
        try {
            for (T constant : classOfT.getEnumConstants()) {
                String name = constant.name();
                SerializedName annotation = classOfT.getField(name).getAnnotation(SerializedName.class);
                if (annotation != null) {
                    name = annotation.value();
                    for (String alternate : annotation.alternate()) {
                        nameToConstant.put(alternate, constant);
                    }
                }
                nameToConstant.put(name, constant);
                constantToName.put(constant, Integer.valueOf(name));
            }
        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public T read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return nameToConstant.get(in.nextString());
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        out.value(value == null ? null : constantToName.get(value));
    }
}