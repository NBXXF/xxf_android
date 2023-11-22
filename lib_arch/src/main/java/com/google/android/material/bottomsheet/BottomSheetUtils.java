package com.google.android.material.bottomsheet;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.xxf.arch.R;

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2018/9/7
 * 处理 bottomsheet 嵌套viewpager
 */
public final class BottomSheetUtils {

    public static void setupViewPager(ViewPager viewPager) {
        /**
         * 避免重复添加
         */
        Object viewpageListener = viewPager.getTag(R.id.navigation_page_bottom_sheet_listener);
        if (!(viewpageListener instanceof BottomSheetViewPagerListener)) {
            final View bottomSheetParent = findBottomSheetParent(viewPager);
            if (bottomSheetParent != null) {
                viewpageListener = new BottomSheetViewPagerListener(viewPager, bottomSheetParent);
                viewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener) viewpageListener);
                viewPager.setTag(R.id.navigation_page_bottom_sheet_listener, viewpageListener);
            } else if (!viewPager.isAttachedToWindow()) {
                Object attachListener = viewPager.getTag(R.id.navigation_page_bottom_sheet_attach_state_listener);
                if (!(attachListener instanceof View.OnAttachStateChangeListener)) {
                    attachListener = new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {
                            setupViewPager((ViewPager) v);
                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {

                        }
                    };
                    viewPager.addOnAttachStateChangeListener((View.OnAttachStateChangeListener) attachListener);
                    viewPager.setTag(R.id.navigation_page_bottom_sheet_attach_state_listener, attachListener);
                }
            }
        }
    }

    public static void setupViewPager(ViewPager2 viewPager) {
        /**
         * 避免重复添加
         */
        Object viewpageListener = viewPager.getTag(R.id.navigation_page_bottom_sheet_listener);
        if (!(viewpageListener instanceof BottomSheetViewPager2Listener)) {
            final View bottomSheetParent = findBottomSheetParent(viewPager);
            if (bottomSheetParent != null) {
                viewpageListener = new BottomSheetViewPager2Listener(viewPager, bottomSheetParent);
                viewPager.registerOnPageChangeCallback((ViewPager2.OnPageChangeCallback) viewpageListener);
                viewPager.setTag(R.id.navigation_page_bottom_sheet_listener, viewpageListener);
            } else if (!viewPager.isAttachedToWindow()) {
                Object attachListener = viewPager.getTag(R.id.navigation_page_bottom_sheet_attach_state_listener);
                if (!(attachListener instanceof View.OnAttachStateChangeListener)) {
                    attachListener = new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {
                            setupViewPager((ViewPager2) v);
                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {

                        }
                    };
                    viewPager.addOnAttachStateChangeListener((View.OnAttachStateChangeListener) attachListener);
                    viewPager.setTag(R.id.navigation_page_bottom_sheet_attach_state_listener, attachListener);
                }
            }
        }
    }

    private static class BottomSheetViewPagerListener extends ViewPager.SimpleOnPageChangeListener {
        private final ViewPager viewPager;
        private final AndroidBottomSheetBehavior<View> behavior;

        private BottomSheetViewPagerListener(ViewPager viewPager, View bottomSheetParent) {
            this.viewPager = viewPager;
            this.behavior = (AndroidBottomSheetBehavior<View>) AndroidBottomSheetBehavior.from(bottomSheetParent);
        }

        @Override
        public void onPageSelected(int position) {
            viewPager.post(behavior::invalidateScrollingChild);
        }
    }

    private static class BottomSheetViewPager2Listener extends ViewPager2.OnPageChangeCallback {
        private final ViewPager2 viewPager;
        private final AndroidBottomSheetBehavior<View> behavior;

        private BottomSheetViewPager2Listener(ViewPager2 viewPager, View bottomSheetParent) {
            this.viewPager = viewPager;
            this.behavior = (AndroidBottomSheetBehavior<View>) AndroidBottomSheetBehavior.from(bottomSheetParent);
        }

        @Override
        public void onPageSelected(int position) {
            viewPager.post(behavior::invalidateScrollingChild);
        }
    }

    private static View findBottomSheetParent(final View view) {
        View current = view;
        while (current != null) {
            final ViewGroup.LayoutParams params = current.getLayoutParams();
            if (params instanceof CoordinatorLayout.LayoutParams && ((CoordinatorLayout.LayoutParams) params).getBehavior() instanceof AndroidBottomSheetBehavior) {
                return current;
            }
            final ViewParent parent = current.getParent();
            current = parent == null || !(parent instanceof View) ? null : (View) parent;
        }
        return null;
    }

}
