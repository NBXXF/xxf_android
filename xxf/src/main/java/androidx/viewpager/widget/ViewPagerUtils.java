package androidx.viewpager.widget;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2018/9/7
 */
public class ViewPagerUtils {

    @Nullable
    public static View getCurrentView(ViewPager viewPager) {
        final int currentItem = viewPager.getCurrentItem();
        for (int i = 0; i < viewPager.getChildCount(); i++) {
            final View child = viewPager.getChildAt(i);
            final ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) child.getLayoutParams();
            if (!layoutParams.isDecor && currentItem == layoutParams.position) {
                return child;
            }
        }
        return null;
    }

    @Nullable
    public static View getCurrentView(ViewPager2 viewPager) {
        final int currentItem = viewPager.getCurrentItem();
        RecyclerView recyclerView = findRecyclerView(viewPager);
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = viewPager.getChildAt(currentItem);
            if (child != null) {
                if (recyclerView.getChildLayoutPosition(child) == currentItem) {
                    return child;
                }
            }
        }
        return null;
    }


    /**
     * 找到recyclerview
     *
     * @param viewPager
     * @return
     */
    public static RecyclerView findRecyclerView(ViewPager2 viewPager) {
        if (viewPager.getChildCount() == 0) {
            return null;
        }
        return (RecyclerView) viewPager.getChildAt(0);
    }
}
