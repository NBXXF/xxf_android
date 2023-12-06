package com.xxf.arch.exceptions;

import androidx.annotation.Nullable;

import java.io.IOException;

/**
 * Description
 * <p>
 * 为retrofit body 整理的exception
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/4/5
 * version 1.0.0
 */
public class ResponseException extends IOException {
    public final int code;
    @Nullable
    public final Object body;


    public ResponseException(int code, String message) {
        this(code, message, null);
    }

    public ResponseException(int code, String message, Object body) {
        super(message);
        this.code = code;
        this.body = body;
    }

    @Override
    public String toString() {
        return "ResponseException{" +
                "code=" + code + super.getMessage() +
                ", body=" + body +
                '}';
    }
}
