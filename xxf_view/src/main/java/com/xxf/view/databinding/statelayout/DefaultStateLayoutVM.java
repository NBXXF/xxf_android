package com.xxf.view.databinding.statelayout;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.xxf.view.R;
import com.xxf.view.loading.ViewState;

import io.reactivex.functions.Action;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 默认多状态布局 控制vm
 */
public class DefaultStateLayoutVM implements IStateLayoutVM {
    final ObservableField<ViewState> viewState = new ObservableField<>(ViewState.VIEW_STATE_CONTENT);
    final ObservableInt emptyIcon = new ObservableInt(R.mipmap.alpha_default_empty_data);
    final ObservableInt emptyDesc = new ObservableInt(R.string.alpha_default_no_data);
    final ObservableField<String> errorDesc = new ObservableField<>("加载失败");
    final ObservableInt errorIcon = new ObservableInt(R.mipmap.alpha_default_load_error);
    final ObservableField<Action> retryAction = new ObservableField<Action>();

    public DefaultStateLayoutVM(Action retryAction) {
        this.setRetryAction(retryAction);
    }

    @Override
    public ObservableField<ViewState> getLayoutState() {
        return this.viewState;
    }

    @Override
    public IStateLayoutVM setLayoutState(ViewState viewState) {
        this.viewState.set(viewState);
        return this;
    }

    @Override
    public IStateLayoutVM setEmptyIcon(int emptyIcon) {
        this.emptyIcon.set(emptyIcon);
        return this;
    }

    @Override
    public ObservableInt getEmptyIcon() {
        return this.emptyIcon;
    }

    @Override
    public IStateLayoutVM setEmptyDesc(int emptyDesc) {
        this.emptyDesc.set(emptyDesc);
        return this;
    }

    @Override
    public ObservableInt getEmptyDesc() {
        return this.emptyDesc;
    }

    @Override
    public IStateLayoutVM setErrorIcon(int errorIcon) {
        this.errorIcon.set(errorIcon);
        return this;
    }

    @Override
    public ObservableInt getErrorIcon() {
        return this.errorIcon;
    }

    @Override
    public IStateLayoutVM setErrorDesc(String errorDesc) {
        this.setErrorDesc(errorDesc);
        return this;
    }

    @Override
    public ObservableField<String> getErrorDesc() {
        return this.errorDesc;
    }


    @Override
    public IStateLayoutVM setRetryAction(Action retryAction) {
        this.retryAction.set(retryAction);
        return this;
    }

    @Override
    public ObservableField<Action> getRetryAction() {
        return this.retryAction;
    }
}
