package com.xxf.objectbox;


import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.xxf.objectbox.id.MurmurHash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.objectbox.Box;


/**
 * @Description: objectBox 扩展
 * @Author: XGod
 * @CreateDate: 2020/7/25 22:55
 */
public class ObjectBoxUtils {

    private ObjectBoxUtils() {
    }

    /**
     * 生成id
     *
     * @param id
     * @return
     */
    public static long generateId(String id) {
        return MurmurHash.hash32(id);
    }

    /**
     * 替换合并
     *
     * @param box
     * @param insertData
     * @param mergeFunction
     * @param <T>
     * @throws Exception
     */
    @WorkerThread
    public static <T> void put(@NonNull Box<T> box, @NonNull List<T> insertData, @NonNull ObjectBoxMergeFunction<T> mergeFunction) throws Throwable {
        Objects.requireNonNull(box);
        Objects.requireNonNull(insertData);
        Objects.requireNonNull(mergeFunction);
        List<Long> ids = new ArrayList<>();
        for (T t : insertData) {
            ids.add(box.getId(t));
        }
        Map<Long, T> insertedMap = box.getMap(ids);
        List<T> apply = mergeFunction.apply(insertData, insertedMap);
        box.put(apply);
    }

    /**
     * 替换合并
     *
     * @param box
     * @param insertData
     * @param mergeFunction
     * @param <T>
     * @throws Exception
     */
    @WorkerThread
    public static <T> void put(@NonNull Box<T> box, @NonNull T insertData, @NonNull ObjectBoxMergeFunction<T> mergeFunction) throws Throwable {
        put(box, Arrays.asList(insertData), mergeFunction);
    }

    /**
     * 清除表所有数据 替换全部
     *
     * @param box
     * @param insertData
     * @param mergeFunction
     * @param <T>
     */
    @WorkerThread
    public static <T> void replaceAll(@NonNull Box<T> box, @NonNull List<T> insertData, @NonNull ObjectBoxMergeFunction<T> mergeFunction) throws Throwable {
        Objects.requireNonNull(box);
        Objects.requireNonNull(insertData);
        Objects.requireNonNull(mergeFunction);
        List<Long> ids = new ArrayList<>();
        for (T t : insertData) {
            ids.add(box.getId(t));
        }
        Map<Long, T> insertedMap = box.getMap(ids);
        List<T> apply = mergeFunction.apply(insertData, insertedMap);
        box.getStore().runInTx(new Runnable() {
            @Override
            public void run() {
                box.removeAll();
                box.put(apply);
            }
        });
    }
}
