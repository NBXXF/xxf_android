package com.xxf.arch.json.typeadapter.format.impl.number;

import androidx.annotation.NonNull;

import com.xxf.arch.json.typeadapter.format.NumberObjectFormatTypeAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @Description: 有符号百分数格式化 2-2位小数,不会自动*100
 * @Author: XGod
 * @CreateDate: 2020/11/5 15:36
 * <p>
 * 用{@link com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject}接收
 * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
 */
public class Number_percent_2_2_DOWN_Signed_FormatTypeAdapter extends NumberObjectFormatTypeAdapter {

    @Override
    public String format(@NonNull BigDecimal origin) throws Exception {
        NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.CHINA);
        numberInstance.setGroupingUsed(false);
        numberInstance.setMinimumFractionDigits(2);
        numberInstance.setMaximumFractionDigits(2);
        numberInstance.setRoundingMode(RoundingMode.DOWN);
        try {
            DecimalFormat decimalFormat = (DecimalFormat) numberInstance;
            decimalFormat.setPositivePrefix("+");
            decimalFormat.setNegativePrefix("-");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numberInstance.format(origin);
    }
}
