package com.xxf.view.recyclerview.adapter;

import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;

import com.xxf.view.model.SelectableEntity;

import java.util.List;

/**
 * @Description: 选中控制
 * @Author: XGod
 * @CreateDate: 2020/7/25 18:25
 */
public class SelectableUtils {

    /**
     * 是否有选中项
     *
     * @param entities
     * @param <T>
     * @return
     */
    public <T extends SelectableEntity> boolean hasSelected(List<T> entities) {
        return indexOfFirstSelected(entities) >= 0;
    }


    /**
     * 获取第一个选中的
     *
     * @param entities
     * @param <T>
     * @return
     */
    @Nullable
    @CheckResult
    public static <T extends SelectableEntity> T getFirstSelected(List<T> entities) {
        int i = indexOfFirstSelected(entities);
        if (i >= 0) {
            return entities.get(i);
        }
        return null;
    }

    /**
     * 获取第一个选中的交标
     *
     * @param entities
     * @param <T>
     * @return
     */
    public static <T extends SelectableEntity> int indexOfFirstSelected(List<T> entities) {
        if (entities != null) {
            for (int i = 0; i < entities.size(); i++) {
                T item = entities.get(i);
                if (item != null && item.isItemSelected()) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 清楚所有选中
     *
     * @param entities
     * @param <T>
     * @return
     */
    public static <T extends SelectableEntity> List<T> clearSelected(List<T> entities) {
        if (entities != null) {
            for (T item : entities) {
                if (item != null) {
                    item.setItemSelect(false);
                }
            }
        }
        return entities;
    }

    /**
     * 单选
     *
     * @param entities
     * @param selected
     * @return
     */
    public static <T extends SelectableEntity> List<T> selectSignle(List<T> entities, T selected) {
        List<T> ts = clearSelected(entities);
        if (ts != null) {
            for (T item : ts) {
                if (item == selected) {
                    item.setItemSelect(true);
                }
            }
        }
        return ts;
    }

    /**
     * 多选
     *
     * @param entities
     * @param selected
     * @param <T>
     * @return
     */
    public static <T extends SelectableEntity> List<T> selectMultiple(List<T> entities, T... selected) {
        List<T> ts = clearSelected(entities);
        if (ts != null && selected != null) {
            for (T item : ts) {
                for (T s : selected) {
                    if (item == s) {
                        item.setItemSelect(true);
                    }
                }
            }
        }
        return ts;
    }
}
