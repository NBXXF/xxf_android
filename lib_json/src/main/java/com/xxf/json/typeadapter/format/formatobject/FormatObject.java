package com.xxf.json.typeadapter.format.formatobject;



import java.io.Serializable;

/**
 * @Description: 框架格式化数据模型 且保留元数据
 * 作用:0.333333333333,UI只展示0.33
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/5 15:13
 * <p>
 * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
 */
public class FormatObject<O, F> implements Serializable {
    /**
     * 原始数据
     */
    private O origin;
    /**
     * 格式化好的数据
     */
    private F format;

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

    public void setOrigin(O origin) {
        this.origin = origin;
    }

    public void setFormat(F format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "FormatObject{" +
                "origin=" + origin +
                ", format=" + format +
                '}';
    }

    @Override
    public int hashCode() {
        int result = origin != null ? origin.hashCode() : 0;
        result = 31 * result + (format != null ? format.hashCode() : 0);
        return result;
    }
}
