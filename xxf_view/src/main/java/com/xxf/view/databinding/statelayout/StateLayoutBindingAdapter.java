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
    public static void setStateVM(final AlphaStateLayout stateLayout, final IStateLayoutVM stateLayoutVM) {
        if (stateLayoutVM == null) {
            return;
        }
        stateLayout.setViewState(stateLayoutVM.getLayoutState().get());
        stateLayout.setEmptyImage(stateLayoutVM.getEmptyIcon().get());
        stateLayout.setEmptyText(stateLayoutVM.getEmptyDesc().get());
        stateLayout.setErrorImage(stateLayoutVM.getErrorIcon().get());
        stateLayout.setErrorText(stateLayoutVM.getErrorDesc().get());
        stateLayout.setErrorRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateLayoutVM.getRetryAction().get() != null) {
                    try {
                        stateLayoutVM.getRetryAction().get().run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
