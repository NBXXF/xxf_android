package com.xxf.arch.json.typeadapter.format.impl.number;

import androidx.annotation.NonNull;

import com.xxf.arch.json.typeadapter.format.NumberObjectFormatTypeAdapter;

import java.math.BigDecimal;

/**
 * @Description: 不格式化 去除科学计数法展示
 * @Author: XGod
 * @CreateDate: 2020/11/25 10:23
 */
public class Number_UNFormatTypeAdapter extends NumberObjectFormatTypeAdapter {
    @Override
    public String format(@NonNull BigDecimal origin) throws Exception {
        return origin.toPlainString();
    }
}
