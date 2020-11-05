package com.xxf.arch.json.typeadapter.format;

import java.math.BigDecimal;

/**
 * @Description: 货币 高精度格式化对象
 * @Author: XGod
 * @CreateDate: 2020/11/5 16:13
 */
public class NumberFormatObject extends FormatObject<BigDecimal, String> {
    public NumberFormatObject(BigDecimal origin, String format) {
        super(origin, format);
    }
}
