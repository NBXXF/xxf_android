package com.xxf.view.databinding.titlebar;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import io.reactivex.functions.Action;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 标题栏
 */
public interface ITitleBar {

    //---------------背景和高度---------------//

    /**
     * 背景
     *
     * @param drawable
     * @return
     */
    ITitleBar setTitleBarBackground(Drawable drawable);

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
    ITitleBar setTitleBarHeight(int dp);


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
    ITitleBar setTitleBarLeftIcon(@DrawableRes int drawableRes, Action action);

    /**
     * 左边icon
     *
     * @param drawable
     */
    ITitleBar setTitleBarLeftIcon(Drawable drawable, Action action);


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
    ITitleBar setTitleBarTitle(@StringRes int id);


    /**
     * 中间 标题
     *
     * @return
     */
    ITitleBar setTitleBarTitle(CharSequence text);

    /**
     * 中间 标题
     *
     * @return
     */
    ITitleBar setTitleBarTitle(CharSequence text, Action action);

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
    ITitleBar setTitleBarRightIcon(@DrawableRes int drawableRes, Action action);

    /**
     * 右边icon
     *
     * @param drawable
     */
    ITitleBar setTitleBarRightIcon(Drawable drawable, Action action);

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
    ITitleBar setTitleBarRightText(@StringRes int id, Action action);

    /**
     * 右边文本
     *
     * @param text
     * @param action
     * @return
     */
    ITitleBar setTitleBarRightText(CharSequence text, Action action);


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
