package com.xxf.arch.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description
 * @date createTime：2018/9/7
 */
public class XXFActivity extends AppCompatActivity {
    /**
     * 统一返回结果(一般情况只有一个返回值)
     */
    public static final String KEY_ACTIVITY_RESULT = "ActivityResult";


    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 增加 类似fragment 获取上下文
     *
     * @return
     */
    public Context getContext() {
        return this;
    }

    /**
     * 增加 类似fragment 获取上下文
     *
     * @return
     */
    public AppCompatActivity getActivity() {
        return this;
    }


}
