package com.xxf.json.typeadapter.bool;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;



/**
 * Description
 * <p>
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/4/8
 * version 1.0.0
 */
public class BooleanTypeAdapter extends TypeAdapter<Boolean> {
    @Override
    public void write(JsonWriter out, Boolean value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value);
        }
    }

    @Override
    public Boolean read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            case BOOLEAN:
                return in.nextBoolean();
            case NULL:
                try {
                    in.nextNull();
                }catch (Throwable e) {
                    e.printStackTrace();
                }
                return false;
            case NUMBER:
                return in.nextInt() != 0;
            case STRING:
                return apply(in.nextString());
            default:
                throw new JsonParseException("Expected BOOLEAN or NUMBER but was " + peek);
        }
    }


    public Boolean apply(String value) {
        return (!TextUtils.isEmpty(value))
                &&
                (value.equalsIgnoreCase("true") || !value.equals("0"));
    }
}
