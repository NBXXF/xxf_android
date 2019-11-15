package com.xxf.arch.json;

import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 数组
 */
public class ListTypeToken<T> extends TypeToken<List<T>> {
    public ListTypeToken() {
        super();
    }
}
