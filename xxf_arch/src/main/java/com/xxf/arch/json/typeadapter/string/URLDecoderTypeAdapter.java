package com.xxf.arch.json.typeadapter.string;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * Description
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/6/8
 * version 1.0.0
 */
public class URLDecoderTypeAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value);
        }
    }

    @Override
    public String read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            case STRING:
                String content = in.nextString();
                try {
                    return URLDecoder.decode(content, "utf-8");
                } catch (IllegalArgumentException e) {
                    //IllegalArgumentException: URLDecoder: Incomplete trailing escape (%) pattern
                    if (!TextUtils.isEmpty(content)) {
                        content = content.replaceAll("%", "%25");
                    }
                }
                return URLDecoder.decode(content, "utf-8");
        }
        return "";
    }
}
