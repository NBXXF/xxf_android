package com.xxf.view.databinding.statelayout;

import android.databinding.BindingAdapter;
import android.view.View;

import com.xxf.view.loading.AlphaStateLayout;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description @{@link com.xxf.view.loading.AlphaStateLayout} mvvm 绑定
 */
public class StateLayoutBindingAdapter {

    @BindingAdapter(value = {"android:stateLayoutVM"}, requireAll = false)
    public static void setStateVM(AlphaStateLayout stateLayout, final IStateLayoutVM stateLayoutVM) {
        if (stateLayoutVM == null) {
            return;
        }
        stateLayout.setViewState(stateLayoutVM.getLayoutState().get());
        stateLayout.setEmptyImage(stateLayoutVM.getEmptyIcon().get());
        stateLayout.setEmptyText(stateLayoutVM.getEmptyDesc().get());
        stateLayout.setErrorImage(stateLayoutVM.getErrorIcon().get());
        stateLayout.setErrorText(stateLayoutVM.getErrorDesc().get());
        if (stateLayoutVM.getRetryAction().get() == null) {
            stateLayout.setErrorRetryListener(null);
        } else {
            stateLayout.setErrorRetryListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        stateLayoutVM.getRetryAction().get().run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
