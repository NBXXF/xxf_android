package com.xxf.view.recyclerview.itemdecorations.section;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Description  分组选择
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/4/29
 * version 1.0.0
 * 只支持线性布局
 */
public abstract class SectionBaseItemDecoration extends RecyclerView.ItemDecoration {

    SectionProvider provider;

    public SectionBaseItemDecoration(SectionProvider provider) {
        this.provider = provider;
    }


    @Override
    public final void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        TreeMap<Integer, String> groupMap = provider.onProvideSection();
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) parent.getChildAt(i)
                        .getLayoutParams();
                //int position = params.getViewLayoutPosition();
                int adapterPosition = params.getViewAdapterPosition();
                if (groupMap.containsKey(adapterPosition)) {
                    final View child = parent.getChildAt(i);
                    String tag = groupMap.get(adapterPosition);
                    onDrawSection(c, parent, state, tag, child);
                }
            }
        }
    }

    /**
     * 会走section分组
     *
     * @param c
     * @param parent
     * @param state
     * @param section
     * @param child
     */
    abstract void onDrawSection(Canvas c, RecyclerView parent, RecyclerView.State state, String section, View child);

    @Override
    public final void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            TreeMap<Integer, String> groupMap = provider.onProvideSection();
            if (parent.getLayoutManager() instanceof LinearLayoutManager) {
                int position = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
                ArrayList<Integer> groupIndexList = new ArrayList<Integer>(groupMap.keySet());
                int groupIndex = -1;
                for (int i : groupIndexList) {
                    if (i > position) {
                        break;
                    }
                    groupIndex = i;
                }
                if (groupMap.containsKey(groupIndex)) {
                    String tag = groupMap.get(groupIndex);
                    onDrawSectionOver(c, parent, state, tag);
                }
            }
        }
    }

    /**
     * 绘制悬浮
     *
     * @param c
     * @param parent
     * @param state
     * @param section
     */
    abstract void onDrawSectionOver(@NonNull Canvas c, @NonNull RecyclerView
            parent, @NonNull RecyclerView.State state, String section);

    @Override
    public final void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        super.getItemOffsets(outRect, view, parent, state);
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        int adapterPosition = params.getViewAdapterPosition();
        TreeMap<Integer, String> groupMap = provider.onProvideSection();
        getItemOffsets(outRect, view, parent, state, groupMap.containsKey(adapterPosition));
    }

    /**
     * 设置分组距离
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     * @param isSection
     */
    abstract void getItemOffsets(Rect outRect, View view, RecyclerView
            parent, RecyclerView.State state, boolean isSection);
}
