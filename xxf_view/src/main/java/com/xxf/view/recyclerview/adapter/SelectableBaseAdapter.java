package com.xxf.view.recyclerview.adapter;

import androidx.databinding.ObservableArrayList;
import androidx.annotation.CheckResult;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xxf.view.model.SelectableEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Description  可选择的适配器
 *
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/10/5
 * version 2.1.0
 */
public abstract class SelectableBaseAdapter<T extends SelectableEntity> extends BaseRecyclerAdapter<T> implements ISelectableAdapter {

    public static final int SELECT_TYPE_UNSELECTABLE = 200;
    public static final int SELECT_TYPE_SINGLE = 201;
    public static final int SELECT_TYPE_MULTIPLE = 202;

    @IntDef({SELECT_TYPE_UNSELECTABLE,
            SELECT_TYPE_SINGLE,
            SELECT_TYPE_MULTIPLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SelectType {
    }

    @SelectType
    private int selectType = SELECT_TYPE_SINGLE;//默认单选

    public SelectableBaseAdapter(@SelectType int selectType) {
        this.selectType = selectType;
    }

    public SelectableBaseAdapter(@SelectType int selectType, @NonNull ObservableArrayList<T> data) {
        super(data);
        this.selectType = selectType;
    }

    public int getSelectType() {
        return selectType;
    }

    public void setSelectType(@SelectType int selectType) {
        if (this.selectType != selectType) {
            this.selectType = selectType;
            clearSelectedItems();
            notifyDataSetChanged();
        }
    }

    /**
     * 是否开启选择模式
     *
     * @return
     */
    @Override
    public final boolean isSelectable() {
        return selectType == SELECT_TYPE_SINGLE || selectType == SELECT_TYPE_MULTIPLE;
    }

    @Override
    public final boolean isMultiSelect() {
        return selectType == SELECT_TYPE_MULTIPLE;
    }

    @Override
    public final boolean isSingleSelect() {
        return selectType == SELECT_TYPE_SINGLE;
    }

    /**
     * 清除选中的items
     */
    public void clearSelectedItems() {
        for (int i = 0; i < getItemCount(); i++) {
            T item = getItem(i);
            if (item != null) {
                item.setItemSelect(false);
            }
        }
        notifyDataSetChanged();
    }


    /**
     * 选中item
     *
     * @param select
     * @param index
     */
    public void setItemSelect(boolean select, int index) {
        switch (selectType) {
            case SELECT_TYPE_MULTIPLE: {
                T item = getItem(index);
                if (item != null
                        && item.isItemSelected() != select) {
                    item.setItemSelect(select);
                    updateItem(item);
                }
            }
            break;
            case SELECT_TYPE_SINGLE: {
                clearSelectedItems();
                T item = getItem(index);
                if (item != null
                        && item.isItemSelected() != select) {
                    item.setItemSelect(select);
                    notifyDataSetChanged();
                }
            }
            break;
        }
    }

    /**
     * 反选item
     *
     * @param index
     */
    public void toogleItemSelect(int index) {
        T item = getItem(index);
        if (item != null) {
            setItemSelect(!item.isItemSelected(), index);
        }
    }

    /**
     * 获取已经选择的item
     *
     * @return
     */
    @NonNull
    public List<T> getSelectedItems() {
        List<T> selectedItems = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            T item = getItem(i);
            if (item != null && item.isItemSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    /**
     * 获取第一个选中的item
     *
     * @return
     */
    @Nullable
    @CheckResult
    public T getSelectedItem() {
        List<T> selectedItems = getSelectedItems();
        if (!selectedItems.isEmpty()) {
            return selectedItems.get(0);
        }
        return null;
    }
}
