package com.xxf.arch.json.typeadapter.format.formatobject;

/**
 * @Description: 时间格式化对象
 * @Author: XGod
 * @CreateDate: 2020/11/5 16:13
 */
public class TimeFormatObject extends FormatObject<Long, String> {
    public TimeFormatObject(Long origin, String format) {
        super(origin, format);
    }
}
