package com.xxf.view.recyclerview.itemdecorations;

import android.content.Context;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：6/25/21
 * Description ://分割线构建工厂
 */
public class DividerDecorationFactory {

    /**
     * 创建格子布局的分割线
     * @param spacingPx
     * @param hasEdge
     * @return
     */
    public static GridItemDecoration createGridDividerDecoration(Context context,int spacingPx, boolean hasEdge) {
        return new GridItemDecoration(spacingPx,hasEdge);
    }

    /**
     * 创建水平或者垂直的分割线
     * @param context
     * @param color
     * @param sizePx
     * @return
     */
    public static DividerDecoration createDividerDecoration(Context context,int color, int sizePx) {
        return new DividerDecoration(context,color,sizePx);
    }


}
