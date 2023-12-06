package com.xxf.json.typeadapter.number;

import com.google.gson.JsonParseException;
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
            case NUMBER:
                try {
                    return jsonReader.nextLong();
                } catch (NumberFormatException e) {
                    // 如果带小数点则会抛出这个异常
                    return new BigDecimal(jsonReader.nextString()).longValue();
                }
            case STRING:
                String result = jsonReader.nextString();
                if (result == null || "".equals(result)) {
                    return 0L;
                }
                try {
                    return Long.parseLong(result);
                } catch (NumberFormatException e) {
                    // 如果带小数点则会抛出这个异常
                    return new BigDecimal(result).longValue();
                }
            case NULL:
                jsonReader.nextNull();
                return null;
            default:
                throw new JsonParseException("Expected long but was " + peek);
        }
    }
}
