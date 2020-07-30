package com.xxf.view.recyclerview;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

/**
 * @Description: RecyclerViewView 工具类
 * @Author: XGod
 * @CreateDate: 2020/7/30 13:57
 */
public class RecyclerViewUtils {


    /**
     * 清除动画
     *
     * @param recyclerView
     */
    public static void clearItemAnimator(@Nullable RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        if (recyclerView.getItemAnimator() != null) {
            recyclerView.getItemAnimator().setAddDuration(0);
            recyclerView.getItemAnimator().setChangeDuration(0);
            recyclerView.getItemAnimator().setMoveDuration(0);
            recyclerView.getItemAnimator().setRemoveDuration(0);
        }
        if (recyclerView.getItemAnimator() instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }
    }
}
