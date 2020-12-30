package com.xxf.view.databinding.titlebar;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import io.reactivex.rxjava3.functions.Action;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 标题栏
 */
public interface IBindingTitleBar {

    //---------------背景和高度---------------//

    /**
     * 背景
     *
     * @param drawable
     * @return
     */
    IBindingTitleBar setTitleBarBackground(Drawable drawable);

    /**
     * 背景
     *
     * @return
     */
    ObservableField<Drawable> getTitleBarBackground();

    /**
     * 高度
     *
     * @param dp
     */
    IBindingTitleBar setTitleBarHeight(int dp);


    /**
     * 高度
     *
     * @return
     */
    ObservableInt getTitleBarHeight();

    //---------------左边icon---------------//

    /**
     * 左边icon
     *
     * @param drawableRes
     */
    IBindingTitleBar setTitleBarLeftIcon(@DrawableRes int drawableRes, Action action);

    /**
     * 左边icon
     *
     * @param drawable
     */
    IBindingTitleBar setTitleBarLeftIcon(Drawable drawable, Action action);


    /**
     * 左边icon
     *
     * @return
     */
    ObservableField<Drawable> getTitleBarLeftIcon();

    /**
     * 左边icon点击事件
     *
     * @return
     */
    ObservableField<Action> getTitleBarLeftIconListener();


    //---------------中间 标题---------------//

    /**
     * 中间 标题
     *
     * @return
     */
    IBindingTitleBar setTitleBarTitle(@StringRes int id);


    /**
     * 中间 标题
     *
     * @return
     */
    IBindingTitleBar setTitleBarTitle(CharSequence text);

    /**
     * 中间 标题
     *
     * @return
     */
    IBindingTitleBar setTitleBarTitle(CharSequence text, Action action);

    /**
     * 中间 标题
     *
     * @return
     */
    ObservableField<CharSequence> getTitleBarTitle();

    /**
     * 中间 标题点击事件
     *
     * @return
     */
    ObservableField<Action> getTitleBarTitleListener();


    //---------------右边 icon---------------//


    /**
     * 右边icon
     *
     * @param drawableRes
     */
    IBindingTitleBar setTitleBarRightIcon(@DrawableRes int drawableRes, Action action);

    /**
     * 右边icon
     *
     * @param drawable
     */
    IBindingTitleBar setTitleBarRightIcon(Drawable drawable, Action action);

    /**
     * 右边icon
     *
     * @return
     */
    ObservableField<Drawable> getTitleBarRightIcon();


    /**
     * 右边icon点击事件
     *
     * @return
     */
    ObservableField<Action> getTitleBarRightIconListener();


    //---------------右边 text---------------//

    /**
     * 右边文本
     *
     * @param id
     * @param action
     * @return
     */
    IBindingTitleBar setTitleBarRightText(@StringRes int id, Action action);

    /**
     * 右边文本
     *
     * @param text
     * @param action
     * @return
     */
    IBindingTitleBar setTitleBarRightText(CharSequence text, Action action);


    /**
     * 右边文本
     *
     * @return
     */
    ObservableField<CharSequence> getTitleBarRightText();

    /**
     * 右边文本点击事件
     *
     * @return
     */
    ObservableField<Action> getTitleBarRightTextListener();


}
