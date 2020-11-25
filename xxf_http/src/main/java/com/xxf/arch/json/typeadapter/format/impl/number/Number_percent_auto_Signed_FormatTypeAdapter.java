package com.xxf.arch.json.typeadapter.format.impl.number;

import androidx.annotation.NonNull;

import com.xxf.arch.json.typeadapter.format.NumberObjectFormatTypeAdapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @Description: 有符号百分数格式化,安卓源码会自动*100
 * @Author: XGod
 * @CreateDate: 2020/11/5 15:36
 * <p>
 * 用{@link com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject}接收
 * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
 */
public class Number_percent_auto_Signed_FormatTypeAdapter extends NumberObjectFormatTypeAdapter {

    @Override
    public String format(@NonNull BigDecimal origin) throws Exception {
        NumberFormat percentInstance = NumberFormat.getPercentInstance(Locale.CHINA);
        percentInstance.setGroupingUsed(false);
        try {
            DecimalFormat decimalFormat = (DecimalFormat) percentInstance;
            decimalFormat.setPositivePrefix("+");
            decimalFormat.setNegativePrefix("-");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return percentInstance.format(origin);
    }
}
