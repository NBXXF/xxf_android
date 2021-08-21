package com.xxf.arch.presenter;

import android.net.ConnectivityManager;
import android.net.Network;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.arch.XXF;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 网络监听的特殊Presenter
 * @date createTime：2018/9/7
 */
public abstract class XXFNetwrokPresenter<V> extends XXFPresenter<V> {
    private boolean isFirstNetonAvailable;
    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            if (!isFirstNetonAvailable) {
                isFirstNetonAvailable = true;
                return;
            }
            XXFNetwrokPresenter.this.onNetworkAvailable(network);
        }

        @Override
        public void onLost(Network network) {
            super.onLost(network);
            XXFNetwrokPresenter.this.onNetworkLost(network);
        }
    };

    /**
     * @param lifecycleOwner
     * @param view           可以为空
     */
    public XXFNetwrokPresenter(@NonNull LifecycleOwner lifecycleOwner, @Nullable V view) {
        super(lifecycleOwner, view);
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        XXF.registerNetworkCallback(networkCallback);
    }

    @CallSuper
    @Override
    public void onPause() {
        super.onPause();
        XXF.unregisterNetworkCallback(networkCallback);
    }

    /**
     * 有网
     *
     * @param network
     */
    protected abstract void onNetworkAvailable(Network network);

    /**
     * 无网络
     *
     * @param network
     */
    protected abstract void onNetworkLost(Network network);
}
