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
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        Charset charSet = StandardCharsets.UTF_8;
        if (value.contentType() != null) {
            charSet = value.contentType().charset(StandardCharsets.UTF_8);
        }
        String jsonStr = value.source().readString(charSet);
        JsonReader jsonReader = gson.newJsonReader(new StringReader(jsonStr));
        try {
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } catch (Throwable jsonException) {
            /**
             * 包裹异常 携带全部jsonStr
             * 大部分是这三种异常
             */
            String TAG_ADAPTER = " in adapter:";
            String TAG_JSON = " for jsonStr:";
            String newMessage = "" +
                    jsonException.getMessage() +
                    TAG_ADAPTER +
                    adapter.getClass().getName() +
                    TAG_JSON +
                    jsonStr;
            if (jsonException instanceof JsonIOException) {
                throw new JsonIOException(newMessage, jsonException.getCause());
            } else if (jsonException instanceof JsonSyntaxException) {
                throw new JsonSyntaxException(newMessage, jsonException.getCause());
            } else if (jsonException instanceof JsonParseException) {
                throw new JsonParseException(newMessage, jsonException.getCause());
            } else if (jsonException instanceof EOFException){
                throw new EOFException(newMessage);
            }
            throw jsonException;
        } finally {
            value.close();
        }
    }

}
