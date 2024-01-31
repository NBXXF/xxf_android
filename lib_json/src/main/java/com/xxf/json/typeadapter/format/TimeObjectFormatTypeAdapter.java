package com.xxf.json.typeadapter.format;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.xxf.json.typeadapter.format.formatobject.TimeFormatObject;
import com.xxf.json.typeadapter.number.LongTypeAdapter;

import java.io.IOException;

/**
 * @Description: 时间格式化
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/6 10:10
 * 用{@link TimeFormatObject}接收
 * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
 */
public abstract class TimeObjectFormatTypeAdapter extends TypeAdapter<TimeFormatObject> implements FormatTypeAdapter<Long> {

    private static TypeAdapter<Long> longTypeAdapter = new LongTypeAdapter();

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
