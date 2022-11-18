
package com.xxf.utils;

import android.os.Build;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;


import java.util.List;
import java.util.Objects;

/**
 * fragment工具类
 *
 * @author wuyouxuan
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FragmentUtils {

    /**
     * 移除
     *
     * @param fragmentManager
     * @param tag
     */
    public static void removeFragment(@NonNull FragmentManager fragmentManager,@Nullable String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        removeFragment(fragmentManager,fragment);
    }
    /**
     * 移除
     *
     * @param fragmentManager
     * @param fragment
     */
    public static void removeFragment(@NonNull FragmentManager fragmentManager, @Nullable Fragment fragment) {
        if (fragmentManager != null && fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }


    /**
     * 添加fragment
     *
     * @param fragmentManager
     * @param fragment
     * @param containerViewId
     */
    public static Fragment addFragment(@NonNull FragmentManager fragmentManager,
                                       @NonNull Fragment fragment, @IdRes int containerViewId) {
        Objects.requireNonNull(fragment, "fragment is null");
        Objects.requireNonNull(fragmentManager, "fragmentManager is null");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(containerViewId, fragment);
        transaction.commitAllowingStateLoss();
        return fragment;
    }

    /**
     * 添加fragment
     *
     * @param fragmentManager
     * @param fragment
     * @param tag
     */
    public static Fragment addFragment(@NonNull FragmentManager fragmentManager,
                                       @NonNull Fragment fragment,
                                       @Nullable String tag) {
        Objects.requireNonNull(fragment, "fragment is null");
        Objects.requireNonNull(fragmentManager, "fragmentManager is null");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(fragment, tag);
        transaction.commitAllowingStateLoss();
        return fragment;
    }

    /**
     * 替换碎片
     *
     * @param fragmentManager
     * @param containerViewId
     * @param fragment
     * @param tag
     * @return
     */
    public static Fragment replaceFragment(FragmentManager fragmentManager,
                                           @IdRes int containerViewId,
                                           @Nullable Fragment fragment,
                                           @Nullable String tag) {
        Objects.requireNonNull(fragmentManager, "fragmentManager is null");
        Objects.requireNonNull(fragment, "fragment is null");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, fragment, tag);
        transaction.commitAllowingStateLoss();
        return fragment;
    }

    /**
     * 设置最大生命周期
     * 必须是已经添加到队列的Fragment
     *
     * @param fragmentManager
     * @param fragment
     * @param state
     */
    public static void setMaxLifecycle(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment,
                                       @NonNull Lifecycle.State state) {
        /**
         * Cannot setMaxLifecycle for Fragment not attached to FragmentManager FragmentManager
         */
        if (fragment.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setMaxLifecycle(fragment, state);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 其他fragment 会执行onPause,一般适合Tab切换,showFragment 会重新执行OnResume,方便业务及时刷新
     *
     * @param fragmentManager
     * @param tabFragment
     * @param showFragment
     * @param containerViewId
     * @return
     */
    public static Fragment switchTabFragment(FragmentManager fragmentManager,
                                             @NonNull List<Fragment> tabFragment,
                                             @Nullable Fragment showFragment,
                                             @IdRes int containerViewId) {
        Fragment result = switchFragment(fragmentManager, showFragment, containerViewId);
        if (tabFragment != null) {
            for (Fragment fragment : tabFragment) {
                if (fragment == showFragment) {
                    FragmentUtils.setMaxLifecycle(fragmentManager, fragment, Lifecycle.State.RESUMED);
                } else {
                    FragmentUtils.setMaxLifecycle(fragmentManager, fragment, Lifecycle.State.STARTED);
                }
            }
        }
        return result;
    }

    /**
     * 显示隐藏碎片
     *
     * @param fragmentManager
     * @param fragment
     * @param containerViewId
     */
    public static Fragment switchFragment(FragmentManager fragmentManager,
                                          @Nullable Fragment fragment,
                                          @IdRes int containerViewId) {
        Objects.requireNonNull(fragmentManager, "fragmentManager is null");
        Objects.requireNonNull(fragment, "fragment is null");

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先判断是否被add过
        if (!fragment.isAdded()) {
            //hide all
            List<Fragment> fragments = fragmentManager.getFragments();
            for (Fragment f : fragments) {
                if (!f.isHidden()) {
                    transaction.hide(f);
                }
            }
            transaction
                    .add(containerViewId, fragment)
                    .commitAllowingStateLoss();
        } else {
            List<Fragment> fragments = fragmentManager.getFragments();
            for (Fragment f : fragments) {
                if (f == fragment) {
                    if (f.isHidden()) {
                        transaction.show(f);
                    }
                } else {
                    if (!f.isHidden()) {
                        transaction.hide(f);
                    }
                }
            }
            transaction.commitAllowingStateLoss();
        }
        return fragment;
    }

}
