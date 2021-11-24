package com.xxf.view.model;

import androidx.annotation.DrawableRes;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-09-21 15:45
 * item 渲染控制模型
 */
public interface ItemMenu<T> extends SelectableEntity {

    T getItem();

    CharSequence getItemTitle();

    /**
     * 支持id int
     * 支持 string
     *
     * @return
     */
    Object getItemIcon();

    boolean isItemDisable();

}
