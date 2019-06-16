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
 * @Description 整数和枚举转换器
 */
public abstract class IntEnumJsonTypeAdapter<T extends IntEnum>
        extends TypeAdapter<T>
        implements Function<Integer, T> {

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        out.value(value != null ? value.getValue() : 0);
    }

    @Override
    public T read(JsonReader jsonReader) throws IOException {
        JsonToken peek = jsonReader.peek();
        switch (peek) {
            case STRING:
                try {
                    return apply(Integer.valueOf(jsonReader.nextString()));
                } catch (Exception e) {
                    throw new IOException(e);
                }
            case NUMBER:
                int i = Integer.valueOf(jsonReader.nextInt());
                try {
                    return apply(i);
                } catch (Exception e) {
                    throw new IOException(e);
                }
            default:
                throw new JsonParseException("Expected number or string number but was " + peek);
        }
    }
}
