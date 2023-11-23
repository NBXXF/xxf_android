package com.xxf.json.typeadapter.format.impl.number;



import com.xxf.json.typeadapter.format.NumberObjectFormatTypeAdapter;
import com.xxf.json.typeadapter.format.formatobject.NumberFormatObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @Description: 有符号百分数格式化 2-2位小数, 安卓源码会自动*100
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/5 15:36
 * <p>
 * 用{@link NumberFormatObject}接收
 * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
 */
public class Number_percent_auto_2_2_DOWN_Signed_FormatTypeAdapter extends NumberObjectFormatTypeAdapter {

    @Override
    public String format(BigDecimal origin) throws Exception {
        NumberFormat percentInstance = NumberFormat.getPercentInstance(Locale.CHINA);
        percentInstance.setGroupingUsed(false);
        percentInstance.setMinimumFractionDigits(2);
        percentInstance.setMaximumFractionDigits(2);
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
