package com.xxf.view.model;

/**
 * Description  带选中属性的item实体
 * <p>
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/10/5
 * version 2.1.0
 */
public interface SelectableEntity {

    /**
     * 是否选中
     *
     * @return
     */
    boolean isItemSelected();


    /**
     * 设置选中熟悉
     *
     * @param select
     */
    void setItemSelect(boolean select);

    /**
     * 反选
     */
    void toggleItemSelect();
}
