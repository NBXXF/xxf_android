package com.xxf.json.typeadapter.number;

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
public class BigDecimalTypeAdapter extends TypeAdapter<BigDecimal> {

    @Override
    public BigDecimal read(JsonReader in) throws IOException {
        JsonToken jsonToken = in.peek();
        switch (jsonToken) {
            case NUMBER:
            case STRING:
                String result = in.nextString();
                if (result == null || "".equals(result)) {
                    return new BigDecimal(0);
                }
                return new BigDecimal(result);
            case NULL:
                in.nextNull();
                return null;
            default:
                in.skipValue();
                throw new JsonSyntaxException("Expected BigDecimal but was " + jsonToken);
        }
    }

    @Override
    public void write(JsonWriter out, BigDecimal value) throws IOException {
        out.value(value);
    }
}