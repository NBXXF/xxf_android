package com.xxf.arch.http;

import androidx.annotation.Nullable;

import java.io.IOException;

/**
 * Description
 * <p>
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/4/5
 * version 1.0.0
 */
public class ResponseException extends IOException {
    public final int code;
    public final String message;
    @Nullable
    public final Object body;


    public ResponseException(int code, String message) {
        this(code, message, null);
    }

    public ResponseException(int code, String message, Object body) {
        super(message);
        this.code = code;
        this.message = message;
        this.body = body;
    }

    @Override
    public String toString() {
        return "ResponseException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
