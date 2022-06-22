/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xxf.arch.http.converter.gson;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.xxf.arch.http.converter.OnGsonConvertFailListener;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    static OnGsonConvertFailListener onConvertFailListener;
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        Charset charSet=StandardCharsets.UTF_8;
        if(value.contentType()!=null) {
            charSet=value.contentType().charset(StandardCharsets.UTF_8);
        }
        String jsonStr = value.source().readString(charSet);
        JsonReader jsonReader = gson.newJsonReader(new StringReader(jsonStr));
        //宽容解析
        jsonReader.setLenient(true);
        try {
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } catch (Throwable e) {
            /**
             * 全局监听 解析错误
             */
            if (onConvertFailListener != null) {
                try {
                    onConvertFailListener.onResponseConvertFail(gson, adapter, jsonStr, e);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            value.close();
        }
    }

}
