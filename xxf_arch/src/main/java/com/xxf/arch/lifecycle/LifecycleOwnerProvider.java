package com.xxf.arch.lifecycle;


import androidx.lifecycle.LifecycleOwner;

/**
 * @Description: LifecycleOwner provider
 * @Author: XGod
 * @CreateDate: 2020/6/25 9:03
 */
public interface LifecycleOwnerProvider {
    LifecycleOwner getLifecycleOwner();
}
