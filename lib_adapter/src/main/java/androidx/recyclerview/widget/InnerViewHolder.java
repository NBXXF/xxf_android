package androidx.recyclerview.widget;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：6/26/21
 * Description : 链接内部非公开方法
 */
public class InnerViewHolder extends RecyclerView.ViewHolder {

    public RecyclerView getRecyclerView() {
        return this.mOwnerRecyclerView;
    }

    public InnerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void onViewRecycled() {
    }

    public void onViewAttachedToWindow() {
    }

    public void onViewDetachedFromWindow() {
    }
}
