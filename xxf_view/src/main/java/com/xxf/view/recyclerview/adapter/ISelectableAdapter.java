package com.xxf.view.recyclerview.adapter;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.2.2
 * @Description
 * @date createTime：2017/12/1
 */
public interface ISelectableAdapter {
    /**
     * 是否可以选择
     *
     * @return
     */
    boolean isSelectable();

    /**
     * 是否是多选
     *
     * @return
     */
    boolean isMultiSelect();


    /**
     * 是否是单选
     *
     * @return
     */
    boolean isSingleSelect();
}
