package com.xxf.view.actiondialog;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class ItemMenuImpl<T> implements ItemMenu<T> {
    public T item;
    public int itemColor = 0xFF333333;//默认#333333
    public CharSequence itemTitle;
    public boolean itemDisable;

    public ItemMenuImpl(T item, CharSequence itemTitle) {
        this.item = item;
        this.itemTitle = itemTitle;
    }

    public ItemMenuImpl(T item, int itemColor, CharSequence itemTitle, boolean itemDisable) {
        this.item = item;
        this.itemColor = itemColor;
        this.itemTitle = itemTitle;
        this.itemDisable = itemDisable;
    }

    @Override
    public T getItem() {
        return item;
    }

    @Override
    public int getItemColor() {
        return itemColor;
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
    public void doAction() {

    }
}
