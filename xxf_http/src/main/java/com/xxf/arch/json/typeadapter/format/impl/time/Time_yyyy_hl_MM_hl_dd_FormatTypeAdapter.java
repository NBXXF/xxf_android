package com.xxf.arch.json.typeadapter.format.impl.time;

import androidx.annotation.NonNull;

import com.xxf.arch.json.typeadapter.format.TimeObjectFormatTypeAdapter;

/**
 * @Description: 时间格式化yyyy-MM-dd
 * 分割符 s:代表斜杠 hl:代表中横线  c:代表冒号
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/25 9:47
 */
public class Time_yyyy_hl_MM_hl_dd_FormatTypeAdapter extends TimeObjectFormatTypeAdapter {

    @Override
    public String format(@NonNull Long origin) throws Exception {
        return android.text.format.DateFormat.format("yyyy-MM-dd", origin).toString();
    }
}