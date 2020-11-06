package com.xxf.arch.json.typeadapter.format;

import androidx.annotation.NonNull;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @Description: 货币格式化基础类,
 * 注意:typeadapter 不要用成员变量,gson本身有class缓存
 * <p>
 * java.math.RoundingMode：这是一种枚举类型，它定义了8种数据的舍入模式。它与java.math.BigDecimal类中定义的8个同名静态常量的作用相同，可用BigDecimal.setScale(int newScale, RoundingMode roundingMode)来设置数据的精度和舍入模式。
 * <p>
 * 1、ROUND_UP：向远离零的方向舍入。
 * 若舍入位为非零，则对舍入部分的前一位数字加1；若舍入位为零，则直接舍弃。即为向外取整模式。
 * <p>
 * 2、ROUND_DOWN：向接近零的方向舍入。
 * 不论舍入位是否为零，都直接舍弃。即为向内取整模式。
 * <p>
 * 3、ROUND_CEILING：向正无穷大的方向舍入。
 * 若 BigDecimal 为正，则舍入行为与 ROUND_UP 相同；若为负，则舍入行为与 ROUND_DOWN 相同。即为向上取整模式。
 * <p>
 * 4、ROUND_FLOOR：向负无穷大的方向舍入。
 * 若 BigDecimal 为正，则舍入行为与 ROUND_DOWN 相同；若为负，则舍入行为与 ROUND_UP 相同。即为向下取整模式。
 * <p>
 * 5、ROUND_HALF_UP：向“最接近的”整数舍入。
 * 若舍入位大于等于5，则对舍入部分的前一位数字加1；若舍入位小于5，则直接舍弃。即为四舍五入模式。
 * <p>
 * 6、ROUND_HALF_DOWN：向“最接近的”整数舍入。
 * 若舍入位大于5，则对舍入部分的前一位数字加1；若舍入位小于等于5，则直接舍弃。即为五舍六入模式。
 * <p>
 * 7、ROUND_HALF_EVEN：向“最接近的”整数舍入。
 * 若（舍入位大于5）或者（舍入位等于5且前一位为奇数），则对舍入部分的前一位数字加1；
 * 若（舍入位小于5）或者（舍入位等于5且前一位为偶数），则直接舍弃。即为银行家舍入模式。
 * <p>
 * 8、ROUND_UNNECESSARY
 * 断言请求的操作具有精确的结果，因此不需要舍入。
 * 如果对获得精确结果的操作指定此舍入模式，则抛出ArithmeticException。
 * <p>
 * 小数. 国际化问题
 * DecimalFormat decimalFormat = new DecimalFormat();
 * DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.CHINA);
 * symbols.setDecimalSeparator('.');
 * decimalFormat.setDecimalFormatSymbols(symbols);
 * @Author: XGod
 * @CreateDate: 2020/11/5 15:36
 * <p>
 * 用{@link NumberFormatObject}接收
 * <p>
 * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
 */
public abstract class NumberObjectFormatTypeAdapter extends TypeAdapter<NumberFormatObject> implements FormatTypeAdapter<BigDecimal> {

    @Override
    public final void write(JsonWriter out, NumberFormatObject value) throws IOException {
        if (value.getOrigin() != null) {
            out.value(value.getOrigin().toPlainString());
        } else {
            out.value(String.valueOf(0));
        }
    }

    @Override
    public final NumberFormatObject read(JsonReader jsonReader) throws IOException {
        JsonToken peek = jsonReader.peek();
        jsonReader.setLenient(true);
        BigDecimal bigDecimal;
        if (peek == JsonToken.NULL) {
            try {
                jsonReader.nextNull();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            bigDecimal = new BigDecimal(0);
        } else {
            try {
                bigDecimal = new BigDecimal(jsonReader.nextString());
            } catch (Exception e) {
                e.printStackTrace();
                throw new JsonSyntaxException(e);
            }
        }
        try {
            return new NumberFormatObject(bigDecimal, format(bigDecimal));
        } catch (Exception e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * 注意小数点 国际化问题
     * DecimalFormat decimalFormat = new DecimalFormat();
     * <p>
     * DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.CHINA);
     * symbols.setDecimalSeparator('.');
     * decimalFormat.setDecimalFormatSymbols(symbols);
     * decimalFormat.setGroupingUsed(false);
     * 建议使用
     * NumberFormat.getNumberInstance(Locale.CHINA);
     * NumberFormat.getPercentInstance(Locale.CHINA);
     * NumberFormat.getCurrencyInstance(Locale.CHINA);
     * android.text.format.DateFormat.format(DEFAULT_FORMAT, origin).toString();
     *
     * @param origin
     * @return
     * @throws Exception
     */
    @Override
    public abstract String format(@NonNull BigDecimal origin) throws Exception;
}
