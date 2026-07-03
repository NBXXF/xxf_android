package com.xxf.view.titlebar;


public interface OnTitleBarListener {

    /**
     * 左边的标题被点击
     *
     * @param titleBar      标题栏对象（非空）
     */
    default void onLeftClick(TitleBar titleBar) {}

    /**
     * 中间的标题被点击
     *
     * @param titleBar      标题栏对象（非空）
     */
    default void onTitleClick(TitleBar titleBar) {}

    /**
     * 右边的标题被点击
     *
     * @param titleBar      标题栏对象（非空）
     */
    default void onRightClick(TitleBar titleBar) {}
}