package com.xxf.arch.http;

import java.io.IOException;

/**
 * Description
 *
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/4/5
 * version 1.0.0
 */
public class ResponseException extends IOException {
    public final int code;
    public final String message;


    public ResponseException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
