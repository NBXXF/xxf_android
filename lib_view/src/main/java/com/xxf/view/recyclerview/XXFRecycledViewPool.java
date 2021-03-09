package com.xxf.view.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description: 可以监听池子清理
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/14 10:33
 */
public class XXFRecycledViewPool extends RecyclerView.RecycledViewPool {
    final List<OnClearListener> listeners = new ArrayList<>();

    public interface OnClearListener {

        void onPrepareClear();

        void onFinishClear();
    }

    public void addClearListener(OnClearListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeClearListener(OnClearListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    @Override
    public final void clear() {
        Iterator<OnClearListener> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            OnClearListener next = iterator.next();
            if (next != null) {
                next.onPrepareClear();
            }
        }
        super.clear();

        iterator = listeners.iterator();
        while (iterator.hasNext()) {
            OnClearListener next = iterator.next();
            if (next != null) {
                next.onFinishClear();
            }
        }
    }
}
