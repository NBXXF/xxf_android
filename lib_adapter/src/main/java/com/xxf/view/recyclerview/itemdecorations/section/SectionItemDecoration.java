package com.xxf.view.recyclerview.itemdecorations.section;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description  聊天界面 时间分割线  间隔5分钟
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/4/29
 * version 1.0.0
 */
public class SectionItemDecoration extends SectionBaseItemDecoration {


    /**
     * 分组的画笔
     */
    protected Paint sectionTextPaint = new Paint();
    protected Paint sectionBackgroundPaint = new Paint();
    /**
     * 分组悬浮的画笔
     */
    protected Paint sectionOverTextPaint = new Paint();
    protected Paint sectionOverBackgroundPaint = new Paint();


    //整个分割线高度
    protected float dividerHeight;
    /**
     * 左边距
     */
    protected float paddingLeft;

    public SectionItemDecoration(SectionProvider provider, Paint sectionTextPaint, Paint sectionBackgroundPaint, Paint sectionOverTextPaint, Paint sectionOverBackgroundPaint, float dividerHeight, float paddingLeft) {
        super(provider);
        this.sectionTextPaint = sectionTextPaint;
        this.sectionBackgroundPaint = sectionBackgroundPaint;
        this.sectionOverTextPaint = sectionOverTextPaint;
        this.sectionOverBackgroundPaint = sectionOverBackgroundPaint;
        this.dividerHeight = dividerHeight;
        this.paddingLeft = paddingLeft;
    }

    public SectionItemDecoration(SectionProvider provider,
                                 Paint sectionTextPaint,
                                 Paint sectionBackgroundPaint,
                                 float dividerHeight,
                                 float paddingLeft) {
        this(provider,
                sectionTextPaint,
                sectionBackgroundPaint,
                sectionTextPaint,
                sectionBackgroundPaint,
                dividerHeight, paddingLeft);
    }

    @Override
    void onDrawSection(Canvas c, RecyclerView parent, RecyclerView.State state, String section, View child) {
        c.drawRect(child.getLeft(), child.getTop() - dividerHeight, child.getRight(), child.getTop(), sectionBackgroundPaint);

        float txtStartX = paddingLeft;
        float dividerCenterY = child.getTop() - dividerHeight / 2;
        c.drawText(section, txtStartX, dividerCenterY + sectionTextPaint.getTextSize() * 0.25f, sectionTextPaint);
    }

    @Override
    void onDrawSectionOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state, String section) {
        c.drawRect(parent.getLeft(), 0, parent.getRight(), dividerHeight, sectionOverBackgroundPaint);

        float txtStartX = paddingLeft;
        c.drawText(section, txtStartX, dividerHeight / 2 + sectionOverTextPaint.getTextSize() * 0.25f, sectionOverTextPaint);
    }

    @Override
    void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state, boolean isSection) {
        if (isSection) {
            outRect.set(0, (int) dividerHeight, 0, 0);
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }
}
