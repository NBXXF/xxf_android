package com.xxf.arch.json.typeadapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.xxf.arch.json.datastructure.LongEnum;
import com.xxf.arch.json.typeadapter.enums.LongEnumTypeAdapter;

public class TypeAdapters {
    /**
     *   @SerializedName("1")  自动序列化成整数[框架默认序列化成字符串]
     */
    public static TypeAdapterFactory LONG_ENUM_FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Class<? super T> rawType = typeToken.getRawType();
            if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
                return null;
            }
            if (!rawType.isEnum()) {
                rawType = rawType.getSuperclass(); // handle anonymous subclasses
            }
            if (LongEnum.class.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) new LongEnumTypeAdapter(rawType);
            }
            return null;
        }
    };
}
