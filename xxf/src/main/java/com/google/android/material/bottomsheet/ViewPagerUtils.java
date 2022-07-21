package com.google.android.material.bottomsheet;

import android.view.View;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2018/9/7
 */
public class ViewPagerUtils {

    public static View getCurrentView(ViewPager viewPager) {
        final int currentItem = viewPager.getCurrentItem();
        for (int i = 0; i < viewPager.getChildCount(); i++) {
            final View child = viewPager.getChildAt(i);
            final ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) child.getLayoutParams();
            /*if (!layoutParams.isDecor && currentItem == layoutParams.position) {
                return child;
            }*/
            if (i==1){
                return child;
            }
        }
        return null;
    }

    public static View getCurrentView(ViewPager2 viewPager) {
        final int currentItem = viewPager.getCurrentItem();
        for (int i = 0; i < viewPager.getChildCount(); i++) {
            /*final View child = viewPager.getChildAt(i);
            final ViewPager2.LayoutParams layoutParams = child.getLayoutParams();*/
            View child=viewPager.getChildAt(currentItem);
            /*if (!layoutParams.isDecor && currentItem == layoutParams.position) {
                return child;
            }*/
            if (child!=null){
                return child;
            }
        }
        return null;
    }

}
