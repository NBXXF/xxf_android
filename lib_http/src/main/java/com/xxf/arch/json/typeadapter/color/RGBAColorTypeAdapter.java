package com.xxf.arch.json.typeadapter.color;

import android.graphics.Color;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：8/16/21
 * Description
 * 传输永远是#RGBA
 * 反序列化(服务器到本地模型):将8位的#RGBA转换成8位的#ARGB
 * 序列化（本地模型到服务器）:将8位的#AGBB转换成8位的#RGBA
 */
public class RGBAColorTypeAdapter extends TypeAdapter<String> {

    @Override
    public void write(JsonWriter out, String argbColor) throws IOException {
        if(TextUtils.isEmpty(argbColor)) {
          throw new JsonParseException("color format error");
        }
        if(!argbColor.startsWith("#")) {
            throw new JsonParseException("color format error");
        }
        try {
            Color.parseColor(argbColor);
            //#FFFF00FF
            if(argbColor.length()==9) {
                String rgbaColor = argbColor.substring(0, 1) + argbColor.substring(3, 9) + argbColor.substring(1, 3);
                out.value(rgbaColor);
            }else {
                out.value(argbColor);
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new JsonParseException("color format error");
        }
    }

    @Override
    public String read(JsonReader  jsonReader) throws IOException {
        JsonToken peek = jsonReader.peek();
        jsonReader.setLenient(true);
        switch (peek) {
            case STRING:
                String rgbaColor = jsonReader.nextString();
                if(TextUtils.isEmpty(rgbaColor)) {
                    throw new JsonParseException("color format error");
                }
                if(!rgbaColor.startsWith("#")) {
                    throw new JsonParseException("color format error");
                }
                try {
                    //#FFFF00FF
                    if (rgbaColor.length() == 9) {
                        String argbColor = rgbaColor.substring(0, 1) + rgbaColor.substring(7, 9) + rgbaColor.substring(1, 7);
                        Color.parseColor(argbColor);
                        return argbColor;
                    } else {
                        Color.parseColor(rgbaColor);
                        return rgbaColor;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    throw new JsonParseException("color format error");
                }
            default:
                throw new JsonParseException("Expected colorStr but was " + peek);
        }
    }
}
