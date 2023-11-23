package com.xxf.room.demo;

import com.google.gson.annotations.JsonAdapter;
import com.xxf.json.typeadapter.color.RGBAColorHexIntTypeAdapter;
import com.xxf.json.typeadapter.color.RGBAColorStringTypeAdapter;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：8/16/21
 * Description ://TODO
 */
public class TestColorModel {
    @JsonAdapter(RGBAColorStringTypeAdapter.class)
   public String color;

    @JsonAdapter(RGBAColorHexIntTypeAdapter.class)
    public int colorInt;


    public TestColorModel(String color, int colorInt) {
        this.color = color;
        this.colorInt = colorInt;
    }

    @Override
    public String toString() {
        return "TestColorModel{" +
                "color='" + color + '\'' +
                ", colorInt='" + colorInt + '\'' +
                '}';
    }
}
