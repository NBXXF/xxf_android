package com.xxf.arch.json.typeadapter.format;

import java.io.Serializable;

/**
 * @Description: 框架格式化数据模型 且保留元数据
 * 作用:0.333333333333,UI只展示0.33
 * @Author: XGod
 * @CreateDate: 2020/11/5 15:13
 */
public class FormatObject<O, F> implements Serializable {
    /**
     * 原始数据
     */
    private final O origin;
    /**
     * 格式化好的数据
     */
    private final F format;

    public FormatObject(O origin, F format) {
        this.origin = origin;
        this.format = format;
    }

    public F getFormat() {
        return format;
    }

    public O getOrigin() {
        return origin;
    }

    @Override
    public String toString() {
        return "FormatObject{" +
                "origin=" + origin +
                ", format=" + format +
                '}';
    }
}
