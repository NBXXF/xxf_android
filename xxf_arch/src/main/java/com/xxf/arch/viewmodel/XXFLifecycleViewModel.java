package com.xxf.arch.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.xxf.arch.lifecycle.LifecycleOwnerProvider;

import java.util.Objects;

/**
 * @Description: 带生命周期的vm can use XXF.bindToLifecycle(this);
 * @Author: XGod
 * @CreateDate: 2020/7/7 16:56
 */
public class XXFLifecycleViewModel extends ViewModel implements LifecycleOwnerProvider {
    private LifecycleOwner lifecycleOwner;

    /**
     * @param lifecycleOwner 不能为空
     */
    public XXFLifecycleViewModel(@NonNull LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = Objects.requireNonNull(lifecycleOwner);
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        return this.lifecycleOwner;
    }
}
