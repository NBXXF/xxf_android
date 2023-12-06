package com.xxf.json.typeadapter.orgJSON;

import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ClassName JsonUtils
 * Description  支持Org包下面的json
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date 创建时间：2015/6/17 9:43
 * version
 */
public class JSONObjectTypeAdapter extends TypeAdapter<JSONObject> {

    private static final TypeAdapter<JsonElement> PROXY = TypeAdapters.JSON_ELEMENT;

    @Override
    public JSONObject read(JsonReader in) throws IOException {
        JsonElement read = PROXY.read(in);
        if (read.isJsonObject()) {
            try {
                return new JSONObject(read.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void write(JsonWriter out, JSONObject value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        PROXY.write(out, PROXY.fromJson(value.toString()));
    }
}