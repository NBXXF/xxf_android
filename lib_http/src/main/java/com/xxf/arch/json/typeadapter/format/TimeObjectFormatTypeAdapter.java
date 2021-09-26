package com.xxf.arch.json.typeadapter.format;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.xxf.arch.json.typeadapter.format.formatobject.TimeFormatObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @Description: 时间格式化
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/6 10:10
 * 用{@link TimeFormatObject}接收
 * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
 */
public abstract class TimeObjectFormatTypeAdapter extends TypeAdapter<TimeFormatObject> implements FormatTypeAdapter<Long> {
    /**
     * 专门适配时间
     */
    public static class TimeTypeAdapter extends TypeAdapter<Long> {
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
                    String s = jsonReader.nextString();
                    /**
                     * 先固定兼容 "2020-12-04T06:36:20" 这种UTC 时间字符串
                     * 笨办法 寻求更完善的方法 兼容各种格式化字符串反解析
                     */
                    if (!TextUtils.isEmpty(s) && s.contains("T")) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                            return sdf.parse(s).getTime();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return Long.valueOf(jsonReader.nextString());
                case NULL:
                    try {
                        jsonReader.nextNull();
                    } catch (Throwable e) {
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

    private static TypeAdapter<Long> longTypeAdapter = new TimeTypeAdapter();

    @Override
    public void write(JsonWriter out, TimeFormatObject value) throws IOException {
        longTypeAdapter.write(out, value.getOrigin());
    }

    @Override
    public TimeFormatObject read(JsonReader in) throws IOException {
        Long read = longTypeAdapter.read(in);
        try {
            return onCreateFormatObject(read, format(read));
        } catch (Exception e) {
            e.printStackTrace();
            throw new JsonSyntaxException(e);
        }
    }

    protected TimeFormatObject onCreateFormatObject(Long read, String format) {
        return new TimeFormatObject(read, format);
    }
}
