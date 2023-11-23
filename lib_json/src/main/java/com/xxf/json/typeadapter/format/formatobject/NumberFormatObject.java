package com.xxf.json.typeadapter.format.formatobject;

import java.math.BigDecimal;

/**
 * @Description: 货币 高精度格式化对象
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/5 16:13
 */
public class NumberFormatObject extends FormatObject<BigDecimal, String> {
    public NumberFormatObject(BigDecimal origin, String format) {
        super(origin, format);
    }

    public NumberFormatObject(BigDecimal origin) {
        super(origin, origin.toPlainString());
    }
}
