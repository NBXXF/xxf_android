package com.xxf.arch.json.typeadapter;

import java.text.ParseException;

/**
 * Description
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/8/14
 * version 2.1.0
 */
public interface IStringConverter<T> {
    T conver2Object(String value) throws ParseException;
}
