package com.xxf.json.typeadapter.format;



import com.google.gson.annotations.JsonAdapter;
import com.xxf.json.typeadapter.format.formatobject.NumberFormatObject;
import com.xxf.json.typeadapter.format.formatobject.TimeFormatObject;
import com.xxf.json.typeadapter.format.impl.number.Number_percent_2_2_DOWN_Signed_FormatTypeAdapter;
import com.xxf.json.typeadapter.format.impl.number.Number_percent_auto_2_2_DOWN_FormatTypeAdapter;
import com.xxf.json.typeadapter.format.impl.number.Number_percent_auto_FormatTypeAdapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @Description: 格式化实例模型参考
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/5 20:45
 */
public class FormatDemoModel {

    /**
     * 更多模板参考com.xxf.json.typeadapter.format.impl.number包下面
     * com.xxf.json.typeadapter.format.impl.time包下面
     * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
     *
     * 测试数据
     * {
     * "num": 1948367743.1273676543,
     * "num1": 1948367743.1273676543,
     * "percent": 0.1273676543,
     * "percent2": 15.1273676543,
     * "percent3": -35.1273676543,
     * "percent4": 71.1273676543,
     * "time": 1604631895000,
     * "money": 3456435.32674335
     * }
     *
     * 输出:
     * {num=FormatObject{origin=1948367743.1273676543, format=1948367743.13},
     * num1=FormatObject{origin=1948367743.1273676543, format=1948367743.13},
     * percent=FormatObject{origin=0.1273676543, format=12.74%},
     * percent2=FormatObject{origin=15.1273676543, format=1512.74%},
     * percent3=FormatObject{origin=-35.1273676543, format=-3512.74%},
     * percent4=FormatObject{origin=71.1273676543, format=+7112.74%},
     * time=FormatObject{origin=1604631895000, format=2020-11-06 11:04},
     * money=FormatObject{origin=3456435.32674335, format=￥3456435.33}}
     */

    /**
     * 固定两位小数格式化
     */
    public class MyFractionNumberObjectFormatTypeAdapter extends NumberObjectFormatTypeAdapter {

        @Override
        public String format(BigDecimal origin) throws Exception {
            NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.CHINA);
            numberInstance.setGroupingUsed(false);
            numberInstance.setMinimumFractionDigits(2);
            numberInstance.setMaximumFractionDigits(2);
            return numberInstance.format(origin);
        }
    }

    /**
     * 最大两位小数格式化
     */
    public class MyFractionNumberObjectFormatTypeAdapter2 extends NumberObjectFormatTypeAdapter {

        @Override
        public String format(BigDecimal origin) throws Exception {
            NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.CHINA);
            numberInstance.setGroupingUsed(false);
            numberInstance.setMinimumFractionDigits(0);
            numberInstance.setMaximumFractionDigits(2);
            return numberInstance.format(origin);
        }
    }

    /**
     * 固定两位小数格式化
     */
    public class MyPercentNumberObjectFormatTypeAdapter extends Number_percent_auto_FormatTypeAdapter {

        @Override
        public String format( BigDecimal origin) throws Exception {
            NumberFormat percentInstance = NumberFormat.getPercentInstance(Locale.CHINA);
            percentInstance.setGroupingUsed(false);
            percentInstance.setMinimumFractionDigits(2);
            percentInstance.setMaximumFractionDigits(2);
            return percentInstance.format(origin);
        }
    }

    /**
     * 最大两位百分数格式化
     */
    public class MyPercentNumberObjectFormatTypeAdapter2 extends Number_percent_auto_FormatTypeAdapter {

        @Override
        public String format( BigDecimal origin) throws Exception {
            NumberFormat percentInstance = NumberFormat.getPercentInstance(Locale.CHINA);
            percentInstance.setGroupingUsed(false);
            percentInstance.setMinimumFractionDigits(0);
            percentInstance.setMaximumFractionDigits(2);
            return percentInstance.format(origin);
        }
    }

    /**
     * 有符号小数数格式化
     */
    public class MyPercentNumberObjectFormatTypeAdapter3 extends Number_percent_auto_FormatTypeAdapter {

        @Override
        public String format( BigDecimal origin) throws Exception {
            NumberFormat percentInstance = NumberFormat.getPercentInstance(Locale.CHINA);
            percentInstance.setGroupingUsed(false);
            percentInstance.setMinimumFractionDigits(0);
            percentInstance.setMaximumFractionDigits(2);

            try {
                ((DecimalFormat) percentInstance).setPositivePrefix("+");
                ((DecimalFormat) percentInstance).setNegativePrefix("-");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return percentInstance.format(origin);
        }
    }

    /**
     * 时间格式化
     */
    public class MyTimeObjectFormatTypeAdapter extends TimeObjectFormatTypeAdapter {

        @Override
        public String format( Long origin) throws Exception {
            return android.text.format.DateFormat.format("MM-dd HH:mm", origin).toString();
        }
    }

    public class MyMoneyObjectFormatTypeAdapter extends NumberObjectFormatTypeAdapter {

        @Override
        public String format( BigDecimal origin) throws Exception {
            NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.CHINA);
            currencyInstance.setGroupingUsed(false);
            currencyInstance.setMinimumFractionDigits(0);
            currencyInstance.setMaximumFractionDigits(2);
            return currencyInstance.format(origin);
        }
    }

    /**
     * {num=FormatObject{origin=1948367743.1273676543, format=1948367743.13}
     */
    @JsonAdapter(MyFractionNumberObjectFormatTypeAdapter.class)
    public NumberFormatObject num;


    /**
     * {origin=1948367743.1273676543, format=1948367743.13}
     */
    @JsonAdapter(MyFractionNumberObjectFormatTypeAdapter2.class)
    public NumberFormatObject num1;

    /**
     * 异步分割框架自动格式化处理 {origin=0.1273676543, format=12.74%},
     */
    @JsonAdapter(Number_percent_auto_2_2_DOWN_FormatTypeAdapter.class)
    public NumberFormatObject percent;

    /**
     * {origin=15.1273676543, format=1512.74%}
     */
    @JsonAdapter(MyPercentNumberObjectFormatTypeAdapter2.class)
    public NumberFormatObject percent2;

    /**
     * {origin=-35.1273676543, format=-35.74%}
     */
    @JsonAdapter(Number_percent_2_2_DOWN_Signed_FormatTypeAdapter.class)
    public NumberFormatObject percent3;

    /**
     * {origin=71.1273676543, format=+7112.74%}
     */
    @JsonAdapter(MyPercentNumberObjectFormatTypeAdapter3.class)
    public NumberFormatObject percent4;

    /**
     * {origin=1604631895000, format=2020-11-06 11:04}
     */
    @JsonAdapter(MyTimeObjectFormatTypeAdapter.class)
    public TimeFormatObject time;

    /**
     * {origin=3456435.32674335, format=￥3456435.33}
     */
    @JsonAdapter(MyMoneyObjectFormatTypeAdapter.class)
    public NumberFormatObject money;

    @Override
    public String toString() {
        return "FormatTestModel{" +
                "num=" + num +
                ", num1=" + num1 +
                ", percent=" + percent +
                ", percent2=" + percent2 +
                ", percent3=" + percent3 +
                ", percent4=" + percent4 +
                ", time=" + time +
                ", money=" + money +
                '}';
    }
}
