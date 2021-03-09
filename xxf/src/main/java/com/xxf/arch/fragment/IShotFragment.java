package com.xxf.arch.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;

/**
 * @Description: 子页面截图约束
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/10/23 10:39
 */
public interface IShotFragment {

    /**
     * 截去当前页面为图片
     *
     * @param shotArgs
     * @return
     */
    @Nullable
    @CheckResult
    Bitmap shotFragment(@Nullable Bundle shotArgs);
}
