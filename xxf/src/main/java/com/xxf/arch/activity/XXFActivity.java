package com.xxf.arch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xxf.activityresult.ActivityResult;
import com.xxf.arch.component.WindowComponent;
import com.xxf.arch.lifecycle.XXFLifecycleObserver;

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2018/9/7
 */
public class XXFActivity extends AppCompatActivity implements WindowComponent {
    private static final String KEY_ACTIVITY_RESULT = "KEY_XXF_ACTIVITY_RESULT";

    public XXFActivity() {
    }

    public XXFActivity(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }


    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLifecycle().addObserver(new XXFLifecycleObserver() {
            @Override
            public void onPause() {
                getIntent().removeExtra(KEY_ACTIVITY_RESULT);
            }
        });
    }

    /**
     * 获取结果
     * 有效周期是在onPause之前
     * onPause会清理
     *
     * @return
     */
    @CheckResult
    @Nullable
    public ActivityResult getActivityResult() {
        if (getIntent() != null
                && getIntent().hasExtra(KEY_ACTIVITY_RESULT)) {
            return (ActivityResult) getIntent().getParcelableExtra(KEY_ACTIVITY_RESULT);
        }
        return null;
    }


    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getIntent().putExtra(KEY_ACTIVITY_RESULT, new ActivityResult(requestCode, resultCode, data));
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setWindowSize(int width, int height) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = width;
            attributes.height = height;
            window.setAttributes(attributes);
        }
    }

    @Override
    public void setWindowWidth(int width) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = width;
            window.setAttributes(attributes);
        }
    }

    @Override
    public void setWindowHeight(int height) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.height = height;
            window.setAttributes(attributes);
        }
    }


    @Nullable
    @Override
    public FrameLayout getDecorView() {
        Window window = getWindow();
        if (window != null) {
            return (FrameLayout) window.getDecorView();
        }
        return null;
    }

    @Nullable
    @Override
    public FrameLayout getContentParent() {
        Window window = getWindow();
        if (window != null) {
            return (FrameLayout) window.findViewById(android.R.id.content);
        }
        return null;
    }

    @Override
    public void setDimAmount(float amount) {
        Window window = getWindow();
        if (window != null) {
            window.setDimAmount(amount);
        }
    }

    @Override
    public void setGravity(int gravity) {
        Window window = getWindow();
        if (window != null) {
            window.setGravity(gravity);
        }
    }
}
