package com.xxf.arch.json.typeadapter.number;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import io.reactivex.functions.Function;

/**
 * Description
 * <p>
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/8/14
 * version 2.1.0
 */
public class LongTypeAdapter extends TypeAdapter<Long> {
    @Override
    public void write(JsonWriter jsonWriter, Long s) throws IOException {
        if (null == s) {
            jsonWriter.value(0);
        } else {
            jsonWriter.value(s);
        }
    }

    @Override
    public Long read(JsonReader jsonReader) throws IOException {
        JsonToken peek = jsonReader.peek();
        jsonReader.setLenient(true);
        switch (peek) {
            case STRING:
                return Long.valueOf(jsonReader.nextString());
            case NULL:
                try {
                    jsonReader.nextNull();
                }catch (Throwable e) {
                    e.printStackTrace();
                }
                return Long.valueOf(0);
            case NUMBER:
                return Long.valueOf(jsonReader.nextLong());
            default:
                throw new JsonParseException("Expected long but was " + peek);
        }
    }
}
