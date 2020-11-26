package com.xxf.arch.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.JsonObject;
import com.xxf.arch.XXF;
import com.xxf.arch.activity.XXFActivity;
import com.xxf.arch.test.databinding.ActivityTestBinding;
import com.xxf.arch.test.http.LoginApiService;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.Consumer;
import retrofit2.CacheType;

@Route(path = "/activity/test")
public class TestActivity extends XXFActivity {

    private ActivityTestBinding binding;

    @Autowired(name = ACTIVITY_PARAM)
    String param;

    @SuppressLint({"AutoDispose", "CheckResult"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        ToastUtils.showToast("param:" + param + " name:" + getIntent().getStringExtra("name") + " age:" + getIntent().getStringExtra("age"), ToastUtils.ToastType.SUCCESS);

        binding.btSetResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK, new Intent().putExtra(ACTIVITY_RESULT, binding.etInput.getText().toString()));
                finish();
            }
        });

        XXF.getApiService(LoginApiService.class)
                .getCity(CacheType.firstCache)
                .compose(XXF.bindToErrorNotice())
                //  .as(XXF.bindLifecycle(this, Lifecycle.Event.ON_DESTROY))
                .subscribe(new Consumer<JsonObject>() {
                    @Override
                    public void accept(JsonObject jsonObject) throws Exception {
                        XXF.getLogger().d("==========>retry ye:" + jsonObject + " thread:" + Thread.currentThread().getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        XXF.getLogger().d("==========>retry no:" + throwable + " thread:" + Thread.currentThread().getName());
                    }
                });
       /* Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                emitter.onNext(1);

                Thread.sleep(100);
                emitter.onError(new RuntimeException("xxxx"));
                emitter.onNext(2);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        XXF.getLogger().d("==========>retry ye2:" + o + " thread:" + Thread.currentThread().getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        XXF.getLogger().d("==========>retry no2:" + throwable + " thread:" + Thread.currentThread().getName());
                    }
                });*/
    }
}
