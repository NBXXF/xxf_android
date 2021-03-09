
package com.xxf.arch.lifecycle;

import android.annotation.SuppressLint;

import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

@SuppressLint("RestrictedApi")
public class XXFFullLifecycleObserverAdapter implements GenericLifecycleObserver {

    private final XXFFullLifecycleObserver mObserver;

    public XXFFullLifecycleObserverAdapter(XXFFullLifecycleObserver observer) {
        mObserver = observer;
    }

    @Override
    public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                mObserver.onCreate();
                break;
            case ON_START:
                mObserver.onStart();
                break;
            case ON_RESUME:
                mObserver.onResume();
                break;
            case ON_PAUSE:
                mObserver.onPause();
                break;
            case ON_STOP:
                mObserver.onStop();
                break;
            case ON_DESTROY:
                mObserver.onDestroy();
                break;
            case ON_ANY:
                throw new IllegalArgumentException("ON_ANY must not been send by anybody");
        }
    }
}
