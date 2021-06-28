package com.xxf.arch.test;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.xxf.arch.fragment.XXFAlertDialogFragment;
import com.xxf.arch.test.databinding.DialogFragmentTestBinding;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：6/28/21
 * Description :
 */
public class TestDialogFragment extends XXFAlertDialogFragment<String> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogFragmentTestBinding biding = DialogFragmentTestBinding.inflate(getLayoutInflater());
        setContentView(biding.getRoot());
        biding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setComponentResult("TestDialogFragment yes event:" + System.currentTimeMillis());
            }
        });
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = 1000;
        params.height=1000;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = 1000;
        params.height=1000;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setComponentResult("TestDialogFragment yes event:" + System.currentTimeMillis());
    }
}
