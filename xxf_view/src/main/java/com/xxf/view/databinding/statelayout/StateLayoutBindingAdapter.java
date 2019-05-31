package com.xxf.view.databinding.statelayout;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.xxf.view.loading.AlphaStateLayout;
import com.xxf.view.loading.ViewState;

import io.reactivex.functions.Action;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description @{@link com.xxf.view.loading.AlphaStateLayout} mvvm 绑定
 * <p>
 * 多状态布局 控制绑定
 */
public class StateLayoutBindingAdapter {
    private static final String ATTR_LOADING_VIEW_STATE = "viewState";
    private static final String ATTR_LOADING_VIEW_EMPTY_DESC = "emptyDesc";
    private static final String ATTR_LOADING_VIEW_EMPTY_ICON = "emptyIcon";
    private static final String ATTR_LOADING_VIEW_ERROR_DESC = "errorDesc";
    private static final String ATTR_LOADING_VIEW_ERROR_ICON = "errorIcon";
    private static final String ATTR_LOADING_VIEW_RETRY = "retryListener";

    //拆分方法 高效一些
    @BindingAdapter(value = {
            ATTR_LOADING_VIEW_STATE,
    })
    public static void setStateVM(final AlphaStateLayout stateLayout,
                                  ViewState viewState) {
        stateLayout.setViewState(viewState);
    }

    @BindingAdapter(value = {
            ATTR_LOADING_VIEW_EMPTY_DESC,
    }, requireAll = false)
    public static void setViewState(final AlphaStateLayout stateLayout,
                                    CharSequence emptyDesc) {
        stateLayout.setEmptyText(emptyDesc);
    }


    @BindingAdapter(value = {
            ATTR_LOADING_VIEW_EMPTY_ICON
    })
    public static void setEmptyIcon(final AlphaStateLayout stateLayout,
                                    Drawable emptyIcon) {
        stateLayout.setEmptyImage(emptyIcon);
    }


    @BindingAdapter(value = {
            ATTR_LOADING_VIEW_ERROR_DESC
    })
    public static void setErrorDesc(final AlphaStateLayout stateLayout,
                                    CharSequence errorDesc) {
        stateLayout.setErrorText(errorDesc);
    }

    @BindingAdapter(value = {
            ATTR_LOADING_VIEW_ERROR_ICON
    }, requireAll = false)
    public static void setErrorIcon(final AlphaStateLayout stateLayout,
                                    Drawable errorIcon) {
        stateLayout.setErrorImage(errorIcon);
    }

    @BindingAdapter(value = {
            ATTR_LOADING_VIEW_RETRY
    })
    public static void setRetryListener(final AlphaStateLayout stateLayout,
                                        final Action retryListener) {
        stateLayout.setErrorRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (retryListener != null) {
                    try {
                        retryListener.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
