package com.xxf.view.databinding.statelayout;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.xxf.view.loading.ViewState;

import io.reactivex.functions.Action;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @version 2.3.0
 * @Description 特性:  1:设置多状态布局的状态
 * 2:设置错误布局的重试
 * 3：设置多状态布局的加载信息
 * 4: 设置空布局的空文案,空文案,空图标,一定要是icon资源类型,字符串资源
 * @date createTime：2017/12/24
 */
public interface IStateLayoutVM {
    /**
     * 设置多状态布局的状态
     *
     * @param viewState
     * @return
     */
    IStateLayoutVM setLayoutState(ViewState viewState);


    /**
     * 多状态布局的状态
     *
     * @return
     */
    ObservableField<ViewState> getLayoutState();


    /**
     * 设置空状态布局的icon
     *
     * @param emptyIconRes
     * @return
     */
    IStateLayoutVM setEmptyIcon(@DrawableRes int emptyIconRes);


    /**
     * 设置空状态布局的icon
     *
     * @param emptyIcon
     * @return
     */
    IStateLayoutVM setEmptyIcon(Drawable emptyIcon);

    /**
     * 空布局的icon
     *
     * @return
     */
    ObservableField<Drawable> getEmptyIcon();


    /**
     * 设置空布局的描述
     *
     * @param emptyDescRes
     * @return
     */
    IStateLayoutVM setEmptyDesc(@StringRes int emptyDescRes);


    /**
     * 设置空布局的描述
     *
     * @param emptyDesc
     * @return
     */
    IStateLayoutVM setEmptyDesc(CharSequence emptyDesc);

    /**
     * 获取空文案提示
     *
     * @return
     */
    ObservableField<CharSequence> getEmptyDesc();


    /**
     * 设置错误布局的icon
     *
     * @param errorIconRes
     * @return
     */
    IStateLayoutVM setErrorIcon(@DrawableRes int errorIconRes);


    /**
     * 设置错误布局的icon
     *
     * @param errorIcon
     * @return
     */
    IStateLayoutVM setErrorIcon(Drawable errorIcon);

    /**
     * 获取错误布局的icon
     *
     * @return
     */
    ObservableField<Drawable> getErrorIcon();

    /**
     * 设置错误布局的描述
     *
     * @param errorDescRes
     * @return
     */
    IStateLayoutVM setErrorDesc(@StringRes int errorDescRes);

    /**
     * 设置错误布局的描述
     *
     * @param errorDesc
     * @return
     */
    IStateLayoutVM setErrorDesc(CharSequence errorDesc);


    /**
     * 获取错误布局的描述
     */
    ObservableField<CharSequence> getErrorDesc();


    /**
     * 重试点击
     *
     * @param retryAction
     * @return
     */
    IStateLayoutVM setRetryAction(Action retryAction);


    /**
     * 重试点击
     *
     * @return
     */
    ObservableField<Action> getRetryAction();
}
