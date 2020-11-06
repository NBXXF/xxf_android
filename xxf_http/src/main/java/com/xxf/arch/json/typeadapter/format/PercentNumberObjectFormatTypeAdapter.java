package com.xxf.arch.json.typeadapter.format;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @Description: 百分数格式化
 * @Author: XGod
 * @CreateDate: 2020/11/5 15:36
 * <p>
 * 用{@link com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject}接收
 * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
 */
public class PercentNumberObjectFormatTypeAdapter extends NumberObjectFormatTypeAdapter {

    @Override
    public String format(@NonNull BigDecimal origin) throws Exception {
        NumberFormat percentInstance = NumberFormat.getPercentInstance(Locale.CHINA);
        percentInstance.setMinimumFractionDigits(2);
        percentInstance.setMaximumFractionDigits(2);
        return percentInstance.format(origin);
    }
}
