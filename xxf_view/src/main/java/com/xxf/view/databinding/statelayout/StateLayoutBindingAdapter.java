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

    @BindingAdapter(value = {
            ATTR_LOADING_VIEW_STATE,
            ATTR_LOADING_VIEW_EMPTY_DESC,
            ATTR_LOADING_VIEW_EMPTY_ICON,
            ATTR_LOADING_VIEW_ERROR_DESC,
            ATTR_LOADING_VIEW_ERROR_ICON,
            ATTR_LOADING_VIEW_RETRY
    }, requireAll = false)
    public static void setStateVM(final AlphaStateLayout stateLayout,
                                  ViewState viewState,
                                  CharSequence emptyDesc, Drawable emptyIcon,
                                  CharSequence errorDesc, Drawable errorIcon,
                                  final Action retryListener) {
        stateLayout.setViewState(viewState);
        stateLayout.setEmptyImage(emptyIcon);
        stateLayout.setEmptyText(emptyDesc);
        stateLayout.setErrorImage(errorIcon);
        stateLayout.setErrorText(errorDesc);
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
