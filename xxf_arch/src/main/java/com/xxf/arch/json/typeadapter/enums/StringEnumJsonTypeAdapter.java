package com.xxf.arch.json.typeadapter.enums;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import io.reactivex.functions.Function;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 字符串和枚举转换器
 */
public abstract class StringEnumJsonTypeAdapter<T extends StringEnum>
        extends TypeAdapter<T>
        implements Function<String, T> {

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.getValue());
        }
    }

    @Override
    public T read(JsonReader jsonReader) throws IOException {
        JsonToken peek = jsonReader.peek();
        switch (peek) {
            case STRING:
                try {
                    return apply(jsonReader.nextString());
                } catch (Exception e) {
                    throw new IOException(e);
                }
            default:
                throw new JsonParseException("Expected string but was " + peek);
        }
    }
}
