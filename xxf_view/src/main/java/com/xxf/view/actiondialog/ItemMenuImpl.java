package com.xxf.view.actiondialog;

import com.xxf.view.model.ItemMenu;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public class ItemMenuImpl<T> implements ItemMenu<T> {
    public T item;
    public CharSequence itemTitle;
    public boolean itemDisable;
    public boolean isItemSelected;

    public ItemMenuImpl(T item, CharSequence itemTitle) {
        this.item = item;
        this.itemTitle = itemTitle;
    }

    public ItemMenuImpl(T item, CharSequence itemTitle, boolean itemDisable) {
        this.item = item;
        this.itemTitle = itemTitle;
        this.itemDisable = itemDisable;
    }

    public ItemMenuImpl(T item, CharSequence itemTitle, boolean itemDisable, boolean isItemSelected) {
        this.item = item;
        this.itemTitle = itemTitle;
        this.itemDisable = itemDisable;
        this.isItemSelected = isItemSelected;
    }

    @Override
    public T getItem() {
        return item;
    }


    @Override
    public CharSequence getItemTitle() {
        return itemTitle;
    }

    @Override
    public boolean isItemDisable() {
        return itemDisable;
    }

    @Override
    public boolean isItemSelected() {
        return this.isItemSelected;
    }

    @Override
    public void setItemSelect(boolean select) {
        this.isItemSelected = select;
    }

    @Override
    public void toggleItemSelect() {
        this.isItemSelected = !this.isItemSelected;
    }
}
