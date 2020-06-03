package com.xxf.view.recyclerview.transformer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;
import com.xxf.view.loading.ViewState;
import com.xxf.view.loading.XXFStateLayout;

import java.util.Objects;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description loading..
 */
public class XXFStateRecyclerViewTransformer<T> extends UILifeTransformerImpl<T> {
    private RecyclerView recyclerView;
    private XXFStateLayout stateLayout;

    public XXFStateLayout getStateLayout() {
        return stateLayout;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public XXFStateRecyclerViewTransformer(@NonNull XXFStateLayout stateLayout, @NonNull RecyclerView recyclerView) {
        this.stateLayout = Objects.requireNonNull(stateLayout);
        this.recyclerView = Objects.requireNonNull(recyclerView);
    }

    @Override
    public void onSubscribe() {
        if (this.recyclerView.getAdapter() == null
                || this.recyclerView.getAdapter().getItemCount() <= 0) {
            this.stateLayout.setViewState(ViewState.VIEW_STATE_LOADING);
        }
    }

    @Override
    public void onNext(T t) {
        this.stateLayout.setViewState(ViewState.VIEW_STATE_CONTENT);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable throwable) {
        if (this.recyclerView.getAdapter() == null
                || this.recyclerView.getAdapter().getItemCount() <= 0) {
            this.stateLayout.setViewState(ViewState.VIEW_STATE_ERROR);
            try {
                this.stateLayout.setErrorText((XXF.getErrorConvertFunction().apply(throwable)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCancel() {
        this.stateLayout.setViewState(ViewState.VIEW_STATE_CONTENT);
    }
}
