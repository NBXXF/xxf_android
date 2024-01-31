package com.xxf.json.typeadapter.number;

import android.annotation.SuppressLint;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Objects;


/**
 * Description
 * <p>
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/8/14
 * version 2.1.0
 */
@SuppressLint("SimpleDateFormat")
public class LongTypeAdapter extends TypeAdapter<Long> {
    static SimpleDateFormat DEFAULT_SDF;

    static {
        //2021-07-15 16:13:35
        DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

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
                // 支持时间格式化 2021-07-15 16:13:35  yyyy-MM-dd'T'HH:mm:ss
                if (result.contains("-") && result.contains(":")) {
                    if (result.contains("T")) {
                        try {
                            return ISO8601Utils.parse(result, new ParsePosition(0)).getTime();
                        } catch (ParseException e) {
                            throw new JsonSyntaxException("Expected long but was " + peek);
                        }
                    } else {
                        try {
                            return Objects.requireNonNull(DEFAULT_SDF.parse(result)).getTime();
                        } catch (Exception e) {
                            throw new JsonSyntaxException("Expected long but was " + peek);
                        }
                    }
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
                throw new JsonSyntaxException("Expected long but was " + peek);
        }
    }
}
