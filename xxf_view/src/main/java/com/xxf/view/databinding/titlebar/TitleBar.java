package com.xxf.view.databinding.titlebar;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;

import com.xxf.view.utils.ResourcesUtil;

import io.reactivex.functions.Action;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description token 标题栏
 */
public class TitleBar implements ITitleBar {

    final ObservableField<Drawable> titleBarBackground = new ObservableField<Drawable>();
    final ObservableInt titleBarHeight = new ObservableInt();


    final ObservableField<Drawable> titleBarLeftIcon = new ObservableField<>();
    final ObservableField<Action> titleBarLeftIconListener = new ObservableField<>();


    final ObservableField<CharSequence> titleBarTitle = new ObservableField<>();
    final ObservableField<Action> titleBarTitleListener = new ObservableField<>();


    final ObservableField<Drawable> titleBarRightIcon = new ObservableField<>();
    final ObservableField<Action> titleBarRightIconListener = new ObservableField<>();


    final ObservableField<CharSequence> titleBarRightText = new ObservableField<>();
    final ObservableField<Action> titleBarRightTextListener = new ObservableField<>();

    @Override
    public ITitleBar setTitleBarBackground(Drawable drawable) {
        this.titleBarBackground.set(drawable);
        return this;
    }

    @Override
    public ObservableField<Drawable> getTitleBarBackground() {
        return this.titleBarBackground;
    }

    @Override
    public ITitleBar setTitleBarHeight(int dp) {
        this.titleBarHeight.set(dp);
        return this;
    }

    @Override
    public ObservableInt getTitleBarHeight() {
        return this.titleBarHeight;
    }

    @Override
    public ITitleBar setTitleBarLeftIcon(int drawableRes, Action action) {
        return this.setTitleBarLeftIcon(ResourcesUtil.getDrawable(drawableRes), action);
    }

    @Override
    public ITitleBar setTitleBarLeftIcon(Drawable drawable, Action action) {
        this.titleBarLeftIcon.set(drawable);
        this.titleBarLeftIconListener.set(action);
        return this;
    }

    @Override
    public ObservableField<Drawable> getTitleBarLeftIcon() {
        return this.titleBarLeftIcon;
    }

    @Override
    public ObservableField<Action> getTitleBarLeftIconListener() {
        return this.titleBarLeftIconListener;
    }

    @Override
    public ITitleBar setTitleBarTitle(int id) {
        return this.setTitleBarTitle(ResourcesUtil.getString(id));
    }

    @Override
    public ITitleBar setTitleBarTitle(CharSequence text) {
        return this.setTitleBarTitle(text, null);
    }

    @Override
    public ITitleBar setTitleBarTitle(CharSequence text, Action action) {
        this.titleBarTitle.set(text);
        this.titleBarTitleListener.set(action);
        return this;
    }

    @Override
    public ObservableField<CharSequence> getTitleBarTitle() {
        return this.titleBarTitle;
    }

    @Override
    public ObservableField<Action> getTitleBarTitleListener() {
        return this.titleBarTitleListener;
    }

    @Override
    public ITitleBar setTitleBarRightIcon(int drawableRes, Action action) {
        return this.setTitleBarRightIcon(ResourcesUtil.getDrawable(drawableRes), action);
    }

    @Override
    public ITitleBar setTitleBarRightIcon(Drawable drawable, Action action) {
        this.titleBarRightIcon.set(drawable);
        this.titleBarRightIconListener.set(action);
        return this;
    }

    @Override
    public ObservableField<Drawable> getTitleBarRightIcon() {
        return this.titleBarRightIcon;
    }

    @Override
    public ObservableField<Action> getTitleBarRightIconListener() {
        return this.titleBarRightIconListener;
    }

    @Override
    public ITitleBar setTitleBarRightText(int id, Action action) {
        return this.setTitleBarRightText(ResourcesUtil.getString(id), action);
    }

    @Override
    public ITitleBar setTitleBarRightText(CharSequence text, Action action) {
        this.titleBarRightText.set(text);
        this.titleBarRightTextListener.set(action);
        return this;
    }


    @Override
    public ObservableField<CharSequence> getTitleBarRightText() {
        return this.titleBarRightText;
    }

    @Override
    public ObservableField<Action> getTitleBarRightTextListener() {
        return this.titleBarRightTextListener;
    }
}
