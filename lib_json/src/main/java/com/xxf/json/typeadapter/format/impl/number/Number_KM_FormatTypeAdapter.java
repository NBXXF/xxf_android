package com.xxf.json.typeadapter.format.impl.number;



import com.xxf.json.typeadapter.format.NumberObjectFormatTypeAdapter;

import java.math.BigDecimal;

/**
 * @Description: 转换大数 至k,M单位,最多两位小数
 * @CreateDate: 2020/11/16 14:55
 */
public class Number_KM_FormatTypeAdapter extends NumberObjectFormatTypeAdapter {
    @Override
    public String format(BigDecimal origin) throws Exception {
        return parseKM(origin);
    }

    public static String parseKM(BigDecimal number) {
        if (number == null) {
            return String.valueOf(0);
        }
        if (number.doubleValue() > 1000000) {
            try {
                return new Number_0_2_DOWN_FormatTypeAdapter().format(number.divide(new BigDecimal(1000000))) + "M";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (number.doubleValue() > 1000) {
            try {
                return new Number_0_2_DOWN_FormatTypeAdapter().format(number.divide(new BigDecimal(1000))) + "K";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return number.toPlainString();
    }
}
