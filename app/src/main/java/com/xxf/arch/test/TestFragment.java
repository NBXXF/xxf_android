package com.xxf.arch.test;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.arch.XXF;
import com.xxf.arch.presenter.XXFPresenter;

import io.reactivex.rxjava3.core.Observable;


public class TestFragment extends Fragment {
    public TestFragment() {
       // new Presenter(this, null);
    }

//    class Presenter extends XXFPresenter<Object> {
//
//        public Presenter(@NonNull LifecycleOwner lifecycleOwner, Object view) {
//            super(lifecycleOwner, view);
//        }
//
//        @Override
//        public void onCreate() {
//            super.onCreate();
//            Observable.just(1)
//                    .to(XXF.bindLifecycle(getLifecycleOwner()))
//                    .subscribe();
//            Log.d("================>p2", "onCreate");
//        }
//
//        @Override
//        public void onPause() {
//            super.onPause();
//            Log.d("================>p2", "onPause");
//        }
//
//        @Override
//        public void onDestroy() {
//            super.onDestroy();
//            Log.d("================>p2", "onDestroy");
//        }
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
