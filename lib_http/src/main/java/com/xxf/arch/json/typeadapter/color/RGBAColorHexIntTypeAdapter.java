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
public class RGBAColorHexIntTypeAdapter extends TypeAdapter<Integer> {
   private RGBAColorStringTypeAdapter parse=new RGBAColorStringTypeAdapter();
    @Override
    public void write(JsonWriter out, Integer argbColor) throws IOException {
        String argbColorStr = "#"+Integer.toHexString(argbColor);
        parse.write(out,argbColorStr);
    }

    @Override
    public Integer read(JsonReader in) throws IOException {
        String argbColorStr = parse.read(in);
        return Color.parseColor(argbColorStr);
    }
}
