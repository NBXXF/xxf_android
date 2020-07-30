package com.xxf.arch.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xxf.arch.R;

import io.reactivex.functions.BiConsumer;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description 支持有返回值响应的Fragment
 * @date createTime：2018/9/7
 */

public class XXFResultFragment<R>
        extends XXFFragment implements IResultFragment<Fragment, R> {

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
