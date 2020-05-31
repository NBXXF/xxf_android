package com.xxf.arch.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xxf.arch.activity.ActivityForResultKeyProvider;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description
 * @date createTime：2018/9/7
 */

public class XXFFragment
        extends Fragment implements ActivityForResultKeyProvider {

    private View contentView;

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public final void setContentView(@LayoutRes int layoutResID) {
        this.contentView = getLayoutInflater().inflate(layoutResID, null);
    }

    public final void setContentView(View view) {
        this.contentView = view;
    }

    /***
     * 禁止复写
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (this.contentView != null) {
            ViewGroup parent = (ViewGroup) this.contentView.getParent();
            if (parent != null) {
                parent.removeView(this.contentView);
            }
        }
        return this.contentView;
    }

    /**
     * 会重复调用
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 会重复调用 禁止复写
     */
    @Override
    public final void onDestroyView() {
        super.onDestroyView();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
