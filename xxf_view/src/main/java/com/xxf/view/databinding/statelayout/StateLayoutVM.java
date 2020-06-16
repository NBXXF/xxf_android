package com.xxf.view.databinding.statelayout;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;
import android.graphics.drawable.Drawable;

import com.xxf.view.R;
import com.xxf.view.loading.ViewState;
import com.xxf.view.utils.ResourcesUtil;

import io.reactivex.functions.Action;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 默认多状态布局 控制vm
 */
public class StateLayoutVM
        extends BaseObservable
        implements IStateLayoutVM {
    final ObservableField<ViewState> layoutStateOb = new ObservableField<>(ViewState.VIEW_STATE_CONTENT);
    final ObservableField<Drawable> emptyIconOb = new ObservableField<Drawable>(ResourcesUtil.getDrawable(R.mipmap.xxf_default_empty_data));
    final ObservableField<CharSequence> emptyDescOb = new ObservableField<CharSequence>(ResourcesUtil.getString(R.string.alpha_default_no_data));
    final ObservableField<CharSequence> errorDescOb = new ObservableField<CharSequence>("加载失败");
    final ObservableField<Drawable> errorIconOb = new ObservableField<Drawable>(ResourcesUtil.getDrawable(R.mipmap.xxf_default_load_error));
    final ObservableField<Action> retryActionOb = new ObservableField<Action>(new Action() {
        @Override
        public void run() throws Exception {

        }
    });

    public StateLayoutVM(Action retryAction) {
        this.retryActionOb.set(retryAction);
    }

    @Override
    public IStateLayoutVM setLayoutState(ViewState viewState) {
        this.layoutStateOb.set(viewState);
        return this;
    }

    @Override
    public ObservableField<ViewState> getLayoutState() {
        return this.layoutStateOb;
    }

    @Override
    public IStateLayoutVM setEmptyIcon(int emptyIconRes) {
        return this.setEmptyIcon(ResourcesUtil.getDrawable(emptyIconRes));
    }

    @Override
    public IStateLayoutVM setEmptyIcon(Drawable emptyIcon) {
        this.emptyIconOb.set(emptyIcon);
        return this;
    }

    @Override
    public ObservableField<Drawable> getEmptyIcon() {
        return this.emptyIconOb;
    }

    @Override
    public IStateLayoutVM setEmptyDesc(int emptyDescRes) {
        return this.setEmptyDesc(ResourcesUtil.getString(emptyDescRes));
    }

    @Override
    public IStateLayoutVM setEmptyDesc(CharSequence emptyDesc) {
        this.emptyDescOb.set(emptyDesc);
        return this;
    }

    @Override
    public ObservableField<CharSequence> getEmptyDesc() {
        return this.emptyDescOb;
    }

    @Override
    public IStateLayoutVM setErrorIcon(int errorIconRes) {
        return this.setErrorIcon(ResourcesUtil.getDrawable(errorIconRes));
    }

    @Override
    public IStateLayoutVM setErrorIcon(Drawable errorIcon) {
        this.errorIconOb.set(errorIcon);
        return this;
    }

    @Override
    public ObservableField<Drawable> getErrorIcon() {
        return this.errorIconOb;
    }

    @Override
    public IStateLayoutVM setErrorDesc(int errorDescRes) {
        return this.setErrorDesc(ResourcesUtil.getString(errorDescRes));
    }

    @Override
    public IStateLayoutVM setErrorDesc(CharSequence errorDesc) {
        this.errorDescOb.set(errorDesc);
        return this;
    }


    @Override
    public ObservableField<CharSequence> getErrorDesc() {
        return this.errorDescOb;
    }

    @Override
    public IStateLayoutVM setRetryAction(Action retryAction) {
        this.retryActionOb.set(retryAction);
        return this;
    }

    @Override
    public ObservableField<Action> getRetryAction() {
        return this.retryActionOb;
    }
}
