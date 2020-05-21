package com.xxf.view.actiondialog;

import com.xxf.view.recyclerview.adapter.SelectableEntity;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-09-21 15:45
 * item 渲染控制模型
 */
public interface ItemMenu<T> extends SelectableEntity {

    T getItem();

    int getItemColor();

    CharSequence getItemTitle();

    boolean isItemDisable();

}
