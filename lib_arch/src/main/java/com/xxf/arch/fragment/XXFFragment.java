package com.xxf.arch.fragment;


import android.os.Bundle;
import android.util.Pair;
import android.view.View;


import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xxf.application.lifecycle.ViewLifecycleOwner;
import com.xxf.arch.component.ObservableComponent;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2018/9/7
 */

public class XXFFragment<R>
        extends Fragment implements ObservableComponent<Fragment, R> {

    public XXFFragment() {
    }

    public XXFFragment(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }


    private final Subject<Object> componentSubject = PublishSubject.create().toSerialized();

    @Override
    public Observable<Pair<Fragment, R>> getComponentObservable() {
        return componentSubject.ofType(Object.class)
                .map(new Function<Object, Pair<Fragment, R>>() {
                    @Override
                    public Pair<Fragment, R> apply(Object o) throws Throwable {
                        return (Pair<Fragment, R>) o;
                    }
                });
    }

    @Override
    public void setComponentResult(R result) {
        componentSubject.onNext(Pair.create(this, result));
    }


    /**
     * 会重复调用
     *
     * @param view
     * @param savedInstanceState
     */
    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewLifecycleOwner.set(view,this);
    }

}
