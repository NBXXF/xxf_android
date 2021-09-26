package com.xxf.arch.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.xxf.arch.json.datastructure.IntEnum;
import com.xxf.arch.json.datastructure.ListOrEmpty;
import com.xxf.arch.json.datastructure.ListOrSingle;
import com.xxf.arch.json.datastructure.LongEnum;
import com.xxf.arch.json.typeadapter.bool.BooleanTypeAdapter;
import com.xxf.arch.json.typeadapter.number.LongTypeAdapter;
import com.xxf.arch.json.typeadapter.number.PercentageDoubleTypeAdapter;
import com.xxf.arch.json.typeadapter.number.PercentageFloatTypeAdapter;

public class GsonFactory {
    /**
     * 自定义gson
     *
     * @return
     */
    public static Gson createGson() {
        return new GsonBuilder()
                .setLenient()// json宽松
                .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                .setPrettyPrinting()// 调教格式
                .disableHtmlEscaping() //默认是GSON把HTML 转义的
                //不使用 与relam 插入更新违背
                //  .registerTypeAdapter(String.class, new StringNullAdapter())//将空字符串转换成""
                .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter())
                .registerTypeAdapter(Long.class, new LongTypeAdapter())
                .registerTypeAdapter(Double.class, new PercentageDoubleTypeAdapter())
                .registerTypeAdapter(double.class, new PercentageDoubleTypeAdapter())
                .registerTypeAdapter(Float.class, new PercentageFloatTypeAdapter())
                .registerTypeAdapter(float.class, new PercentageFloatTypeAdapter())
                .registerTypeAdapter(ListOrSingle.class, new ListOrSingle.ListOrSingleTypeAdapter())
                .registerTypeAdapter(ListOrEmpty.class, new ListOrEmpty.ListOrEmptyTypeAdapter())
                .registerTypeAdapterFactory(NUMBER_ENUM_FACTORY)
                .create();
    }

    static TypeAdapterFactory NUMBER_ENUM_FACTORY = new TypeAdapterFactory() {
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
            if (IntEnum.class.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) new IntEnum.IntEnumTypeAdapter(rawType);
            }
            if (LongEnum.class.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) new LongEnum.LongEnumTypeAdapter(rawType);
            }
            return TypeAdapters.ENUM_FACTORY.create(gson, typeToken);
        }
    };
}
