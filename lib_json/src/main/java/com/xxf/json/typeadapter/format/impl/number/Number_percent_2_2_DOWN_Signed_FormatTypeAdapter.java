package com.xxf.json.typeadapter.format.impl.number;



import com.xxf.json.typeadapter.format.NumberObjectFormatTypeAdapter;
import com.xxf.json.typeadapter.format.formatobject.NumberFormatObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @Description: 有符号百分数格式化 2-2位小数,不会自动*100
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/5 15:36
 * <p>
 * 用{@link NumberFormatObject}接收
 * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
 */
public class Number_percent_2_2_DOWN_Signed_FormatTypeAdapter extends NumberObjectFormatTypeAdapter {

    @Override
    public String format(BigDecimal origin) throws Exception {
        NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.CHINA);
        numberInstance.setGroupingUsed(false);
        numberInstance.setMinimumFractionDigits(2);
        numberInstance.setMaximumFractionDigits(2);
        numberInstance.setRoundingMode(RoundingMode.DOWN);
        try {
            DecimalFormat decimalFormat = (DecimalFormat) numberInstance;
            decimalFormat.setPositivePrefix("+");
            decimalFormat.setPositiveSuffix("%");
            decimalFormat.setNegativePrefix("-");
            decimalFormat.setNegativeSuffix("%");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numberInstance.format(origin);
    }
}
