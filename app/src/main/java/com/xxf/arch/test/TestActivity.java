package com.xxf.arch.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.xxf.arch.activity.XXFActivity;
import com.xxf.arch.test.databinding.ActivityTestBinding;
import com.xxf.arch.utils.ToastUtils;

@Route(path = "/activity/test")
public class TestActivity extends XXFActivity {

    private ActivityTestBinding binding;

    @Autowired(name = ACTIVITY_PARAM)
    String param;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        ToastUtils.showToast("param:" + param,ToastUtils.ToastType.SUCCESS);
        binding.btSetResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK, new Intent().putExtra(ACTIVITY_RESULT, "XXX"));
                finish();
            }
        });
    }
}
