package com.xxf.room.demo;

import com.google.gson.annotations.JsonAdapter;
import com.xxf.arch.json.typeadapter.color.RGBAColorTypeAdapter;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：8/16/21
 * Description ://TODO
 */
public class TestColorModel {
    @JsonAdapter(RGBAColorTypeAdapter.class)
   public String color;

    public TestColorModel(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "TestColorModel{" +
                "color='" + color + '\'' +
                '}';
    }
}
