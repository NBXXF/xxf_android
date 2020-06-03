package com.xxf.view.databinding.statelayout;

import androidx.annotation.NonNull;


import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;
import com.xxf.view.loading.ViewState;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 多状态布局rx 变换器
 */
public class XXFStateViewTransformer<T> extends UILifeTransformerImpl<T> {

    private IStateLayoutVM alphaStateViewVM;

    public IStateLayoutVM getAlphaStateViewVM() {
        return alphaStateViewVM;
    }

    public XXFStateViewTransformer(@NonNull IStateLayoutVM stateViewVM) {
        this.alphaStateViewVM = stateViewVM;
    }

    @Override
    public void onSubscribe() {
        if (alphaStateViewVM != null) {
            alphaStateViewVM.setLayoutState(ViewState.VIEW_STATE_LOADING);
        }
    }

    @Override
    public void onNext(T t) {
        if (alphaStateViewVM != null) {
            alphaStateViewVM.setLayoutState(ViewState.VIEW_STATE_CONTENT);
        }
    }

    @Override
    public void onComplete() {
       /* if (alphaStateViewVM != null) {
            alphaStateViewVM.setLayoutState(ViewState.VIEW_STATE_CONTENT);
        }*/
    }

    @Override
    public void onError(Throwable throwable) {
        if (alphaStateViewVM != null) {
            alphaStateViewVM.setLayoutState(ViewState.VIEW_STATE_ERROR);
            try {
                alphaStateViewVM.setErrorDesc(XXF.getErrorConvertFunction().apply(throwable));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCancel() {
        if (alphaStateViewVM != null) {
            alphaStateViewVM.setLayoutState(ViewState.VIEW_STATE_CONTENT);
        }
    }
}
