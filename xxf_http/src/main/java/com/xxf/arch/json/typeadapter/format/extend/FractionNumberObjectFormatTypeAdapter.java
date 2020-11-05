package com.xxf.arch.json.typeadapter.format.extend;

import androidx.annotation.NonNull;

import com.xxf.arch.json.typeadapter.format.NumberObjectFormatTypeAdapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @Description: 小数格式化
 * @Author: XGod
 * @CreateDate: 2020/11/5 20:02
 * <p>
 * java.math.RoundingMode：这是一种枚举类型，它定义了8种数据的舍入模式。它与java.math.BigDecimal类中定义的8个同名静态常量的作用相同，可用BigDecimal.setScale(int newScale, RoundingMode roundingMode)来设置数据的精度和舍入模式。
 * <p>
 * 1、ROUND_UP：向远离零的方向舍入。
 * <p>
 * 若舍入位为非零，则对舍入部分的前一位数字加1；若舍入位为零，则直接舍弃。即为向外取整模式。
 * <p>
 * 2、ROUND_DOWN：向接近零的方向舍入。
 * <p>
 * 不论舍入位是否为零，都直接舍弃。即为向内取整模式。
 * <p>
 * 3、ROUND_CEILING：向正无穷大的方向舍入。
 * <p>
 * 若 BigDecimal 为正，则舍入行为与 ROUND_UP 相同；若为负，则舍入行为与 ROUND_DOWN 相同。即为向上取整模式。
 * <p>
 * 4、ROUND_FLOOR：向负无穷大的方向舍入。
 * <p>
 * 若 BigDecimal 为正，则舍入行为与 ROUND_DOWN 相同；若为负，则舍入行为与 ROUND_UP 相同。即为向下取整模式。
 * <p>
 * 5、ROUND_HALF_UP：向“最接近的”整数舍入。
 * <p>
 * 若舍入位大于等于5，则对舍入部分的前一位数字加1；若舍入位小于5，则直接舍弃。即为四舍五入模式。
 * <p>
 * 6、ROUND_HALF_DOWN：向“最接近的”整数舍入。
 * <p>
 * 若舍入位大于5，则对舍入部分的前一位数字加1；若舍入位小于等于5，则直接舍弃。即为五舍六入模式。
 * <p>
 * 7、ROUND_HALF_EVEN：向“最接近的”整数舍入。
 * <p>
 * 若（舍入位大于5）或者（舍入位等于5且前一位为奇数），则对舍入部分的前一位数字加1；
 * <p>
 * 若（舍入位小于5）或者（舍入位等于5且前一位为偶数），则直接舍弃。即为银行家舍入模式。
 * <p>
 * 8、ROUND_UNNECESSARY
 * <p>
 * 断言请求的操作具有精确的结果，因此不需要舍入。
 * <p>
 * 如果对获得精确结果的操作指定此舍入模式，则抛出ArithmeticException。
 */
public class FractionNumberObjectFormatTypeAdapter extends NumberObjectFormatTypeAdapter {
    private int minimumDigits = -1;
    private int maximumDigits = -1;

    public FractionNumberObjectFormatTypeAdapter(int minimumDigits, int maximumDigits) {
        this.minimumDigits = minimumDigits;
        this.maximumDigits = maximumDigits;
    }

    @Override
    protected String format(@NonNull BigDecimal origin, @NonNull DecimalFormat decimalFormat) {
        if (minimumDigits >= 0) {
            decimalFormat.setMinimumFractionDigits(minimumDigits);
        }
        if (maximumDigits >= 0) {
            decimalFormat.setMaximumFractionDigits(maximumDigits);
        }
        return decimalFormat.format(origin);
    }
}
