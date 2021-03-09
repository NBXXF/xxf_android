package com.xxf.view.databinding.statelayout;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.databinding.BindingAdapter;

import com.xxf.view.loading.ViewState;
import com.xxf.view.loading.XXFStateLayout;

import io.reactivex.rxjava3.functions.Action;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description @{@link com.xxf.view.loading.XXFStateLayout} mvvm 绑定
 * <p>
 * 多状态布局 控制绑定
 */
public class StateLayoutBindingAdapter {
    private static final String ATTR_LOADING_VIEW_STATE = "layoutState";
    private static final String ATTR_LOADING_VIEW_EMPTY_DESC = "emptyDesc";
    private static final String ATTR_LOADING_VIEW_EMPTY_ICON = "emptyIcon";
    private static final String ATTR_LOADING_VIEW_ERROR_DESC = "errorDesc";
    private static final String ATTR_LOADING_VIEW_ERROR_ICON = "errorIcon";
    private static final String ATTR_LOADING_VIEW_RETRY = "retryListener";

    //拆分方法 高效一些
    @BindingAdapter(value = {
            ATTR_LOADING_VIEW_STATE,
    })
    public static void setStateVM(final XXFStateLayout stateLayout,
                                  ViewState viewState) {
        if (viewState != null) {
            stateLayout.setViewState(viewState);
        }
    }

    @BindingAdapter(value = {
            ATTR_LOADING_VIEW_EMPTY_DESC,
    }, requireAll = false)
    public static void setViewState(final XXFStateLayout stateLayout,
                                    CharSequence emptyDesc) {
        stateLayout.setEmptyText(emptyDesc);
    }


    @BindingAdapter(value = {
            ATTR_LOADING_VIEW_EMPTY_ICON
    })
    public static void setEmptyIcon(final XXFStateLayout stateLayout,
                                    Drawable emptyIcon) {
        stateLayout.setEmptyImage(emptyIcon);
    }


    @BindingAdapter(value = {
            ATTR_LOADING_VIEW_ERROR_DESC
    })
    public static void setErrorDesc(final XXFStateLayout stateLayout,
                                    CharSequence errorDesc) {
        stateLayout.setErrorText(errorDesc);
    }

    @BindingAdapter(value = {
            ATTR_LOADING_VIEW_ERROR_ICON
    }, requireAll = false)
    public static void setErrorIcon(final XXFStateLayout stateLayout,
                                    Drawable errorIcon) {
        stateLayout.setErrorImage(errorIcon);
    }

    @BindingAdapter(value = {
            ATTR_LOADING_VIEW_RETRY
    })
    public static void setRetryListener(final XXFStateLayout stateLayout,
                                        final Action retryListener) {
        stateLayout.setErrorRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (retryListener != null) {
                    try {
                        retryListener.run();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
