package com.xxf.view.recyclerviewadapter;

/**
 * Description  带选中属性的item实体
 * Company Beijing icourt
 * author  youxuan  E-mail:xuanyouwu@163.com
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
