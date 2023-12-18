package com.xxf.json.typeadapterfactory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.xxf.json.typeadapter.bool.BooleanTypeAdapter;
import com.xxf.json.typeadapter.number.BigDecimalTypeAdapter;
import com.xxf.json.typeadapter.number.IntegerTypeAdapter;
import com.xxf.json.typeadapter.number.LongTypeAdapter;
import com.xxf.json.typeadapter.number.PercentageDoubleTypeAdapter;
import com.xxf.json.typeadapter.number.PercentageFloatTypeAdapter;
import com.xxf.json.typeadapter.orgJSON.JSONArrayTypeAdapter;
import com.xxf.json.typeadapter.orgJSON.JSONObjectTypeAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;

import kotlin.Deprecated;

/**
 * @Description gson容错
 * @Author: XGod  xuanyouwu@163.com  qq:2767356582  https://github.com/NBXXF/gson_plugin
 * https://blog.csdn.net/axuanqq
 * 将生成的TypeAdapterFactory注册到gson实例中
 * val gson = GsonBuilder()
 * .registerTypeAdapterFactory(SafeTypeDelegateAdapterFactory())
 * .create()
 */
@Deprecated(message = "不推荐,这个多少有问题")
@java.lang.Deprecated
public class SafeTypeDelegateAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                try {
                    return delegate.read(in);
                } catch (Throwable e) {

                    //方式1： 只要有报错的并且还有下一个就执行跳过操
                    //缺陷有 beginArray 但是没有endArray
//                    if (in.hasNext()) {
//                        in.skipValue();
//                    }

                    //方式2
                    consumeAll(in);
                    return null;
                }
            }

            private void consumeAll(JsonReader in) throws IOException {
                if (in.hasNext()) {
                    JsonToken peek = in.peek();
                    if (peek == JsonToken.STRING) {
                        in.skipValue();
                    } else if (peek == JsonToken.BEGIN_ARRAY) {
                        in.beginArray();
                        consumeAll(in);
                        in.endArray();
                    } else if (peek == JsonToken.BEGIN_OBJECT) {
                        in.beginObject();
                        consumeAll(in);
                        in.endObject();
                    } else if (peek == JsonToken.END_ARRAY) {
                        in.endArray();
                    } else if (peek == JsonToken.END_OBJECT) {
                        in.endObject();
                    } else if (peek == JsonToken.NUMBER) {
                        in.skipValue();
                    } else if (peek == JsonToken.BOOLEAN) {
                        in.skipValue();
                    } else if (peek == JsonToken.NAME) {
                        in.skipValue();
                        consumeAll(in);
                    } else if (peek == JsonToken.NULL) {
                        in.nextNull();
                    }
                }
            }
        };
    }
}
