package com.xxf.arch.json.typeadapter.format.extend;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @Description: 百分数格式化
 * @Author: XGod
 * @CreateDate: 2020/11/5 15:36
 */
public class PercentNumberObjectFormatTypeAdapter extends FractionNumberObjectFormatTypeAdapter {
    public PercentNumberObjectFormatTypeAdapter(int minimumDigits, int maximumDigits) {
        super(minimumDigits, maximumDigits);
    }

    @Override
    protected String format(@NonNull BigDecimal origin, @NonNull DecimalFormat decimalFormat) {
        decimalFormat.setMultiplier(100);
        return super.format(origin, decimalFormat);
    }
}
