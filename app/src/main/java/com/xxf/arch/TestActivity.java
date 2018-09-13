package com.xxf.arch;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xxf.arch.activity.XXFActivity;
import com.xxf.arch.annotation.BindVM;
import com.xxf.arch.annotation.BindView;
import com.xxf.arch.databinding.ActivityTestBinding;
import com.xxf.arch.viewmodel.XXFViewModel;

import java.util.Arrays;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description
 * @Company Beijing icourt
 * @date createTime：2018/9/9
 */
@BindView(R.layout.activity_test)
@BindVM(XXFViewModel.class)
public class TestActivity extends XXFActivity {
    BaseFragmentAdapter baseFragmentAdapter;
    ActivityTestBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getBinding();
        baseFragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        binding.pager.setAdapter(baseFragmentAdapter);
        baseFragmentAdapter.bindData(true, Arrays.asList(new TestFragment(), new TestFragment(), new TestFragment()));

     View view = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {

                }
            });
        }
    }
}
