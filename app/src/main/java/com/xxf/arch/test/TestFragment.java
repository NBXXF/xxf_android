package com.xxf.arch.test;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.arch.XXF;
import com.xxf.arch.lifecycle.XXFFullLifecycleObserver;
import com.xxf.arch.lifecycle.XXFFullLifecycleObserverAdapter;
import com.xxf.arch.presenter.XXFLifecyclePresenter;

import io.reactivex.Observable;

public class TestFragment extends Fragment {
    public TestFragment() {
        new Presenter(this, null);
    }

    class Presenter extends XXFLifecyclePresenter<Object> {

        public Presenter(@NonNull LifecycleOwner lifecycleOwner, Object view) {
            super(lifecycleOwner, view);
        }

        @Override
        public void onCreate() {
            super.onCreate();
            Observable.just(1)
                    .compose(XXF.bindToLifecycle(this))
                    .subscribe();
            Log.d("================>p2", "onCreate");
        }

        @Override
        public void onPause() {
            super.onPause();
            Log.d("================>p2", "onPause");
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.d("================>p2", "onDestroy");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getLifecycle().addObserver(new XXFFullLifecycleObserverAdapter(new XXFFullLifecycleObserver() {
            @Override
            public void onCreate() {
                Log.d("================>p3", "onCreate");
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onResume() {

            }

            @Override
            public void onPause() {
                Log.d("================>p3", "onPause");
            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDestroy() {
                Log.d("================>p3", "onDestroy");
            }
        }));
    }
}
