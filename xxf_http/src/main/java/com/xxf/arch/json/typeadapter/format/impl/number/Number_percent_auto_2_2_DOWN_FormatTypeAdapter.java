package com.xxf.arch.json.typeadapter.format.impl.number;

import androidx.annotation.NonNull;

import com.xxf.arch.json.typeadapter.format.NumberObjectFormatTypeAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @Description: 百分数格式化 2-2位小数, 安卓源码会自动*100
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/5 15:36
 * <p>
 * 用{@link com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject}接收
 * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
 */
public class Number_percent_auto_2_2_DOWN_FormatTypeAdapter extends NumberObjectFormatTypeAdapter {

    @Override
    public String format(@NonNull BigDecimal origin) throws Exception {
        NumberFormat percentInstance = NumberFormat.getPercentInstance(Locale.CHINA);
        percentInstance.setGroupingUsed(false);
        percentInstance.setMinimumFractionDigits(2);
        percentInstance.setMaximumFractionDigits(2);
        percentInstance.setRoundingMode(RoundingMode.DOWN);
        return percentInstance.format(origin);
    }
}
