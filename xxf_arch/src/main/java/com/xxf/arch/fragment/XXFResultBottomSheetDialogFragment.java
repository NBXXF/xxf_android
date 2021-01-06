package com.xxf.arch.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.reactivex.rxjava3.functions.BiConsumer;

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 支持有返回值响应的BottomSheetDialogFragment
 * @date createTime：2018/9/7
 */

public class XXFResultBottomSheetDialogFragment<R>
        extends XXFBottomSheetDialogFragment implements IResultFragment<Fragment, R> {

    private BiConsumer<Fragment, R> fragmentConsumer;

    @Override
    public void setFragmentConsumer(@Nullable BiConsumer<Fragment, R> consumer) {
        this.fragmentConsumer = consumer;
    }

    @Override
    public void setResult(R r) {
        if (fragmentConsumer != null) {
            try {
                fragmentConsumer.accept(this, r);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
