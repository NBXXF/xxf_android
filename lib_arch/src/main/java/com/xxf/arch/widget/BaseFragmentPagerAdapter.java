package com.xxf.arch.widget;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * <p>
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/4/8
 * version 1.0.0
 * Deprecated
 * Switch to androidx.viewpager2.widget.ViewPager2 and use androidx.viewpager2.adapter.FragmentStateAdapter instead.
 * <p>
 * 或者 BaseFragmentStatePagerAdapter
 */
@Deprecated
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {


    private final List<Fragment> fragmentsList = new ArrayList<Fragment>();
    private final List<CharSequence> mFragmentTitles = new ArrayList<CharSequence>();
    private FragmentManager fm;

    public List<Fragment> getFragmentsList() {
        return fragmentsList;
    }

    /**
     * 默认 BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
     *
     * @param fm
     */
    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fm = fm;
    }

    /**
     * FragmentTransaction#setMaxLifecycle}.控制碎片最大生命周期
     *
     * @param fm
     * @param behavior
     */
    @SuppressLint("WrongConstant")
    public BaseFragmentPagerAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.fm = fm;
    }

    public void bindData(boolean isRefresh, List<? extends Fragment> datas) {
        if (datas == null) {
            return;
        }
        if (isRefresh) {
            this.fragmentsList.clear();
        }
        this.fragmentsList.addAll(datas);
        notifyDataSetChanged();
    }

    public void bindTitle(boolean isRefresh, List<? extends CharSequence> titles) {
        if (titles == null) {
            return;
        }
        if (isRefresh) {
            mFragmentTitles.clear();
        }
        mFragmentTitles.addAll(titles);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            try {
                fragmentsList.set(position, (Fragment) obj);
            } catch (Exception e) {
            }
        }
        return obj;
    }

    private Fragment primaryItem;

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        primaryItem = (Fragment) object;
    }

    public Fragment getPrimaryItem() {
        return primaryItem;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mFragmentTitles.isEmpty()) return "";
        return mFragmentTitles.get(position % mFragmentTitles.size());
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        try {
            super.finishUpdate(container);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

