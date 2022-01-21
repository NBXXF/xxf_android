package com.xxf.view.actiondialog;

import com.xxf.view.model.ItemMenu;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public class ItemMenuImpl<T> implements ItemMenu<T> {
    public Object itemIcon;
    public T item;
    public CharSequence itemTitle;
    public boolean itemDisable;
    public boolean isItemSelected;

    public ItemMenuImpl(T item, CharSequence itemTitle) {
        this.item = item;
        this.itemTitle = itemTitle;
    }

    public ItemMenuImpl(T item, Object itemIcon, CharSequence itemTitle) {
        this.item = item;
        this.itemIcon = itemIcon;
        this.itemTitle = itemTitle;
    }


    public ItemMenuImpl(T item, CharSequence itemTitle, boolean isItemSelected) {
        this.item = item;
        this.itemTitle = itemTitle;
        this.isItemSelected = isItemSelected;
    }

    public ItemMenuImpl(T item, CharSequence itemTitle, boolean isItemSelected, boolean itemDisable) {
        this.item = item;
        this.itemTitle = itemTitle;
        this.isItemSelected = isItemSelected;
        this.itemDisable = itemDisable;
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
    public Object getItemIcon() {
        return itemIcon;
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

    @Override
    public int hashCode() {
        int result = itemIcon != null ? itemIcon.hashCode() : 0;
        result = 31 * result + (item != null ? item.hashCode() : 0);
        result = 31 * result + (itemTitle != null ? itemTitle.hashCode() : 0);
        result = 31 * result + (itemDisable ? 1 : 0);
        result = 31 * result + (isItemSelected ? 1 : 0);
        return result;
    }
}
