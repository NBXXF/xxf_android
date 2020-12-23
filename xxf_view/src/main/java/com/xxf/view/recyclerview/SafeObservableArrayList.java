package com.xxf.view.recyclerview;

import androidx.databinding.ObservableArrayList;
import androidx.annotation.CheckResult;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import java.util.Collection;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.0
 * @Description 特性: 更安全的 可监听的ArrayList
 * @date createTime：2017/12/18
 */
public class SafeObservableArrayList<T> extends ObservableArrayList<T> {
    @Override
    public boolean add(@Nullable T object) {
        return super.add(object);
    }

    @Override
    public void add(@IntRange(from = 0) int index, T object) {
        //IndexOutOfBoundsException
        if (index < 0 || index > size()) {
            return;
        }
        super.add(index, object);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        //NullPointerException  为空就不要刷新
        if (collection == null || collection.isEmpty()) {
            return false;
        }
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(@IntRange(from = 0) int index, Collection<? extends T> collection) {
        //IndexOutOfBoundsException
        //NullPointerException
        if (index < 0 || index > size() || collection == null || collection.isEmpty()) {
            return false;
        }
        return super.addAll(index, collection);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @CheckResult
    @Nullable
    @Override
    public T get(@IntRange(from = 0) int index) {
        if (index >= 0 && index < size()) {
            return super.get(index);
        }
        return null;
    }

    @CheckResult
    @Nullable
    @Override
    public T remove(@IntRange(from = 0) int index) {
        //IndexOutOfBoundsException
        if (index < 0 || index >= size()) {
            return null;
        }
        return super.remove(index);
    }

    @Override
    public boolean remove(Object object) {
        return super.remove(object);
    }

    @Override
    public T set(@IntRange(from = 0) int index, T object) {
        //IndexOutOfBoundsException
        if (index < 0 || index >= size()) {
            return null;
        }
        return super.set(index, object);
    }

    @Override
    protected void removeRange(@IntRange(from = 0) int fromIndex, @IntRange(from = 0) int toIndex) {
        //IndexOutOfBoundsException
        if (fromIndex < 0 || toIndex < 0 || toIndex < fromIndex) {
            return;
        }
        super.removeRange(fromIndex, toIndex);
    }

}
