package com.xxf.view.recyclerview.adapter;

import androidx.annotation.IntRange;

/**
 * Description  多布局适配模型
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/10/4
 * version 2.1.0
 */
public interface MultiViewEntity {
    /**
     * 布局
     *
     * @return
     */
    @IntRange(from = 0)
    int getViewType();
}
