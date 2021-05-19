package com.xxf.view.exception;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：5/19/21
 * Description ://文件类型不匹配
 */
public class FileNotMatchTypeException extends RuntimeException {
    public FileNotMatchTypeException(String message) {
        super(message);
    }
}
