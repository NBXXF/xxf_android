package com.xxf.view.recyclerview.adapter;

import android.support.annotation.IntRange;

/**
 * Description  多布局适配模型
 * Company Beijing icourt
 * author  youxuan  E-mail:xuanyouwu@163.com
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
