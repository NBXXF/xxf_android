package com.xxf.arch.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xxf.arch.test.databinding.ActivityTestBinding;
import com.xxf.toast.ToastType;
import com.xxf.toast.ToastUtils;


public class TestActivity extends AppCompatActivity {

    private ActivityTestBinding binding;

    String param;

    @SuppressLint({"AutoDispose", "CheckResult"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        ToastUtils.showToast("param:" + param + " name:" + getIntent().getStringExtra("name") + " age:" + getIntent().getStringExtra("age"), ToastType.SUCCESS);

        binding.btSetResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK, new Intent().putExtra("ACTIVITY_RESULT", binding.etInput.getText().toString()));
                finish();
            }
        });

        /*XXF.getApiService(LoginApiService.class)
                .getCity(CacheType.lastCache)
                .map(new Function<JsonObject, JsonObject>() {
                    @Override
                    public JsonObject apply(JsonObject jsonObject) throws Exception {
                        Log.d("==========>retry map thread:"+Thread.currentThread().getName());
                        return jsonObject;
                    }
                })
                .compose(XXF.bindToErrorNotice())
                //  .as(XXF.bindLifecycle(this, Lifecycle.Event.ON_DESTROY))
                .subscribe(new Consumer<JsonObject>() {
                    @Override
                    public void accept(JsonObject jsonObject) throws Exception {
                        Log.d("==========>retry ye:" + jsonObject + " thread:" + Thread.currentThread().getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("==========>retry no:" + throwable + " thread:" + Thread.currentThread().getName());
                    }
                });*/
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
                        Log.d("==========>retry ye2:" + o + " thread:" + Thread.currentThread().getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("==========>retry no2:" + throwable + " thread:" + Thread.currentThread().getName());
                    }
                });*/
    }
}
