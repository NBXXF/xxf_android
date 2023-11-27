package com.xxf.arch.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.arch.component.WindowComponent;
import com.xxf.view.round.CornerUtil;

import org.jetbrains.annotations.NotNull;

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2018/9/7
 */
public class XXFActivity extends AppCompatActivity implements WindowComponent {
    private static final String KEY_ACTIVITY_RESULT = "KEY_XXF_ACTIVITY_RESULT";
    private boolean mCancelable = true;

    public XXFActivity() {
    }

    public XXFActivity(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }


    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLifecycle().addObserver(new DefaultLifecycleObserver(){
            @Override
            public void onPause(@NonNull LifecycleOwner owner) {
                DefaultLifecycleObserver.super.onPause(owner);
                getIntent().removeExtra(KEY_ACTIVITY_RESULT);
            }
        });
    }



    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
    public void setWindowDimAmount(float amount) {
        Window window = getWindow();
        if (window != null) {
            /**
             * activity 需要强制设置 主题默认是false
             * R.styleable.Window_backgroundDimEnabled
             */
            setWindowBackgroundDimEnabled(amount > 0);
            window.setDimAmount(amount);
        }
    }

    @Override
    public void setWindowGravity(int gravity) {
        Window window = getWindow();
        if (window != null) {
            window.setGravity(gravity);
        }
    }

    @Override
    public void setWindowBackground(@NotNull Drawable drawable) {
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(drawable);
        }
    }

    @Override
    public void setWindowBackground(int color) {
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    @Override
    public void setWindowBackgroundDimEnabled(boolean enabled) {
        Window window = getWindow();
        if (window != null) {
            if (enabled) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }
    }

    @Override
    public void setWindowRadius(float radius) {
        FrameLayout decorView = getDecorView();
        if (decorView != null) {
            CornerUtil.INSTANCE.clipViewRadius(decorView, radius);
        }
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        if (cancel && !mCancelable) {
            mCancelable = true;
        }
        this.setFinishOnTouchOutside(cancel);
    }


    @Override
    public void setCancelable(boolean flag) {
        mCancelable = flag;
    }

    /**
     * activity 内部的是否能返回请用 OnBackPressedDispatcher
     */
    @Override
    public void onBackPressed() {
        if (mCancelable) {
            super.onBackPressed();
        }
    }
}
