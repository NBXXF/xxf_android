package com.xxf.json.typeadapter.format.impl.number;



import com.xxf.json.typeadapter.format.NumberObjectFormatTypeAdapter;
import com.xxf.json.typeadapter.format.formatobject.NumberFormatObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @Description: 百分数格式化,安卓源码会自动*100
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/5 15:36
 * <p>
 * 用{@link NumberFormatObject}接收
 * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
 */
public class Number_percent_auto_FormatTypeAdapter extends NumberObjectFormatTypeAdapter {

    @Override
    public String format(BigDecimal origin) throws Exception {
        NumberFormat percentInstance = NumberFormat.getPercentInstance(Locale.CHINA);
        percentInstance.setGroupingUsed(false);
        return percentInstance.format(origin);
    }
}
