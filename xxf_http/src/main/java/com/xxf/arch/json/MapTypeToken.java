package com.xxf.arch.json;

import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 字典 不能增加构造函数
 */
public class MapTypeToken<K, V> extends TypeToken<Map<K, V>> {
}
