package com.xxf.arch.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxf.arch.json.typeadapter.bool.BooleanTypeAdapter;
import com.xxf.arch.json.typeadapter.number.LongTypeAdapter;

public class GsonFactory {
    /**
     * 自定义gson
     *
     * @return
     */
    public static Gson createGson() {
        return new GsonBuilder()
                .setLenient()// json宽松
                .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                .setPrettyPrinting()// 调教格式
                .disableHtmlEscaping() //默认是GSON把HTML 转义的
                //不使用 与relam 插入更新违背
                //  .registerTypeAdapter(String.class, new StringNullAdapter())//将空字符串转换成""
                .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter())
                .registerTypeAdapter(Long.class, new LongTypeAdapter())
                .create();
    }
}
