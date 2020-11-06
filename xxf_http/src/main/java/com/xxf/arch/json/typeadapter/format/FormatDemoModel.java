package com.xxf.arch.json.typeadapter.format;

import androidx.annotation.NonNull;

import com.google.gson.annotations.JsonAdapter;
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject;
import com.xxf.arch.json.typeadapter.format.formatobject.TimeFormatObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @Description: 格式化实例模型参考
 * @Author: XGod
 * @CreateDate: 2020/11/5 20:45
 */
public class FormatDemoModel {

    /**
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
     */

    /**
     * 固定两位小数格式化
     */
    public class MyFractionNumberObjectFormatTypeAdapter extends NumberObjectFormatTypeAdapter {

        @Override
        public String format(@NonNull BigDecimal origin) throws Exception {
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
        public String format(@NonNull BigDecimal origin) throws Exception {
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
    public class MyPercentNumberObjectFormatTypeAdapter extends PercentNumberObjectFormatTypeAdapter {

        @Override
        public String format(@NonNull BigDecimal origin) throws Exception {
            NumberFormat percentInstance = NumberFormat.getPercentInstance(Locale.CHINA);
            percentInstance.setGroupingUsed(false);
            percentInstance.setMinimumFractionDigits(2);
            percentInstance.setMinimumFractionDigits(2);
            return percentInstance.format(origin);
        }
    }

    /**
     * 最大两位百分数格式化
     */
    public class MyPercentNumberObjectFormatTypeAdapter2 extends PercentNumberObjectFormatTypeAdapter {

        @Override
        public String format(@NonNull BigDecimal origin) throws Exception {
            NumberFormat percentInstance = NumberFormat.getPercentInstance(Locale.CHINA);
            percentInstance.setGroupingUsed(false);
            percentInstance.setMinimumFractionDigits(0);
            percentInstance.setMinimumFractionDigits(2);
            return percentInstance.format(origin);
        }
    }

    /**
     * 有符号小数数格式化
     */
    public class MyPercentNumberObjectFormatTypeAdapter3 extends PercentNumberObjectFormatTypeAdapter {

        @Override
        public String format(@NonNull BigDecimal origin) throws Exception {
            NumberFormat percentInstance = NumberFormat.getPercentInstance(Locale.CHINA);
            percentInstance.setGroupingUsed(false);
            percentInstance.setMinimumFractionDigits(0);
            percentInstance.setMinimumFractionDigits(2);

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
        public String format(@NonNull Long origin) throws Exception {
            return android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", origin).toString();
        }
    }

    public class MyMoneyObjectFormatTypeAdapter extends NumberObjectFormatTypeAdapter {

        @Override
        public String format(@NonNull BigDecimal origin) throws Exception {
            NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.CHINA);
            currencyInstance.setGroupingUsed(false);
            currencyInstance.setMinimumFractionDigits(0);
            currencyInstance.setMinimumFractionDigits(2);
            return currencyInstance.format(origin);
        }
    }

    @JsonAdapter(MyFractionNumberObjectFormatTypeAdapter.class)
    public NumberFormatObject num;


    @JsonAdapter(MyFractionNumberObjectFormatTypeAdapter2.class)
    public NumberFormatObject num1;

    @JsonAdapter(MyPercentNumberObjectFormatTypeAdapter.class)
    public NumberFormatObject percent;

    @JsonAdapter(MyPercentNumberObjectFormatTypeAdapter2.class)
    public NumberFormatObject percent2;

    @JsonAdapter(MyPercentNumberObjectFormatTypeAdapter3.class)
    public NumberFormatObject percent3;

    @JsonAdapter(MyPercentNumberObjectFormatTypeAdapter3.class)
    public NumberFormatObject percent4;

    @JsonAdapter(MyTimeObjectFormatTypeAdapter.class)
    public TimeFormatObject time;

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
    /**
     * 输出结果:
     * FormatTestModel{num=FormatObject{origin=1948367743.1273676543, format=1948367743.13},
     * num1=FormatObject{origin=1948367743.1273676543, format=1948367743.13},
     * percent=FormatObject{origin=0.1273676543, format=12.74%},
     * percent2=FormatObject{origin=15.1273676543, format=1512.74%},
     * percent3=FormatObject{origin=-35.1273676543, format=-3512.74%},
     * percent4=FormatObject{origin=71.1273676543, format=+7112.74%},
     * time=FormatObject{origin=1604631895000, format=2020-11-06 11:04},
     * money=FormatObject{origin=3456435.32674335, format=￥3456435.33}}
     */
}
