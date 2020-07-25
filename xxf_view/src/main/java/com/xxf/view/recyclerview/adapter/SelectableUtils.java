package com.xxf.view.recyclerview.adapter;

import com.xxf.view.model.SelectableEntity;

import java.util.List;

/**
 * @Description: 选中控制
 * @Author: XGod
 * @CreateDate: 2020/7/25 18:25
 */
public class SelectableUtils {

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
