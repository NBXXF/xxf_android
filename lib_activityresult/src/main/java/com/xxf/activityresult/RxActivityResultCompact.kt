package com.xxf.activityresult;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;


public class RxActivityResultCompact {
    private static final String FRAGMENT_TAG = RxActivityResultCompact.class.getName();

    public static Observable<ActivityResult> startActivityForResult(
            @NonNull FragmentActivity activity, @NonNull Intent intent, int requestCode) {

        return startActivityForResult(activity.getSupportFragmentManager(), intent, requestCode, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Observable<ActivityResult> startActivityForResult(
            @NonNull FragmentActivity activity, @NonNull Intent intent, int requestCode, @Nullable Bundle options) {

        return startActivityForResult(activity.getSupportFragmentManager(), intent, requestCode, options);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static Observable<ActivityResult> startActivityForResult(
            @NonNull Fragment fragment, @NonNull Intent intent, int requestCode) {

        return startActivityForResult(fragment.getChildFragmentManager(), intent, requestCode, null);
    }

    public static Observable<ActivityResult> startActivityForResult(
            @NonNull Fragment fragment, @NonNull Intent intent, int requestCode, @NonNull Bundle options) {

        return startActivityForResult(fragment.getChildFragmentManager(), intent, requestCode, options);
    }

    private static Observable<ActivityResult> startActivityForResult(
            @NonNull FragmentManager fragmentManager, @NonNull final Intent intent, final int requestCode, @Nullable final Bundle options) {

        ResultHandleV4Fragment _fragment = (ResultHandleV4Fragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (_fragment == null) {
            _fragment = new ResultHandleV4Fragment();

            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(_fragment, FRAGMENT_TAG);
            transaction.commit();

        } else if (_fragment.isDetached()) {
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.attach(_fragment);
            transaction.commit();
        }

        final ResultHandleV4Fragment fragment = _fragment;
        return fragment.getIsAttachedBehavior()
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean isAttached) throws Throwable {
                        return isAttached;
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<ActivityResult>>() {
                    @Override
                    public ObservableSource<ActivityResult> apply(Boolean aBoolean) throws Exception {
                        fragment.startActivityForResult(intent, requestCode, options);
                        return fragment.getResultPublisher();
                    }
                })
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test( ActivityResult result) throws Exception {
                        return result.getRequestCode() == requestCode;
                    }
                }).take(1);
    }
}
