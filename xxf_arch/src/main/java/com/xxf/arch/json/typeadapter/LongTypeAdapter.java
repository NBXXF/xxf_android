package com.xxf.arch.json.typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Description
 * Company Beijing icourt
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/8/14
 * version 2.1.0
 */
public class LongTypeAdapter extends TypeAdapter<Long>
        implements IStringConverter<Long> {
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
        switch (peek) {
            case STRING:
                return conver2Object(jsonReader.nextString());
            case NUMBER:
                return Long.valueOf(jsonReader.nextLong());
        }
        return Long.valueOf(0);
    }

    @Override
    public Long conver2Object(String s) {
        try {
            return Long.valueOf(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return Long.valueOf(0);
    }
}
