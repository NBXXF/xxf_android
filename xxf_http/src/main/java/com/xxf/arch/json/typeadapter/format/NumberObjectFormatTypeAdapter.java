package com.xxf.arch.json.typeadapter.format;

import androidx.annotation.NonNull;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * @Description: 货币格式化基础类,
 * @Author: XGod
 * @CreateDate: 2020/11/5 15:36
 */
public abstract class NumberObjectFormatTypeAdapter extends TypeAdapter<NumberFormatObject> {

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
            DecimalFormat decimalFormat = new DecimalFormat();

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.CHINA);
            /**
             * 处理小数点问题
             */
            symbols.setDecimalSeparator('.');
            decimalFormat.setDecimalFormatSymbols(symbols);

            /**
             * 默认 不分组
             */
            decimalFormat.setGroupingUsed(false);

            return new NumberFormatObject(bigDecimal, format(bigDecimal, decimalFormat));
        } catch (Exception e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * @param origin 原始数据
     * @return
     */
    protected abstract String format(@NonNull BigDecimal origin, @NonNull DecimalFormat decimalFormat);
}
