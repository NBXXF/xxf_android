package com.xxf.arch.json.typeadapter.number;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;


/**
 * Description
 * <p>
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/8/14
 * version 2.1.0
 */
public class LongTypeAdapter extends TypeAdapter<Long> {
    @Override
    public void write(JsonWriter jsonWriter, Long s) throws IOException {
        if (null == s) {
            jsonWriter.nullValue();
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
                /**
                 * 解决 传递来的数据是其他类型 比如浮点数
                 */
                try {
                    return new BigDecimal(jsonReader.nextString()).longValue();
                } catch (NumberFormatException e) {
                    throw new JsonSyntaxException(e);
                }
            case NULL:
                jsonReader.nextNull();
                return null;
            case NUMBER:
                try {
                    return jsonReader.nextLong();
                } catch (NumberFormatException e) {
                    throw new JsonSyntaxException(e);
                }
            default:
                throw new JsonParseException("Expected long but was " + peek);
        }
    }
}
