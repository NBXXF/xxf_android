package com.xxf.objectbox;

import android.app.Application;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.objectbox.BoxStore;
import io.objectbox.BoxStoreBuilder;

/**
 * @Description: objectBox
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2018/7/16 17:34
 */
public class ObjectBoxFactory {
    private static volatile Map<String, BoxStore> boxStoreMap = new ConcurrentHashMap<>();

    /**
     * 创建box
     *
     * @param boxStoreBuilder
     * @param objectStoreDirectory 路径
     * @return
     */
    public static synchronized BoxStore getBoxStore(BoxStoreBuilder boxStoreBuilder,
                                                    File objectStoreDirectory) {
        BoxStore boxStore = null;
        try {
            boxStore = boxStoreMap.get(objectStoreDirectory.getAbsolutePath());
            if (boxStore == null) {
                boxStoreMap.put(objectStoreDirectory.getAbsolutePath(), boxStore = buildBox(boxStoreBuilder, objectStoreDirectory));
            }
        } catch (Exception e) {
            try {
                /**
                 * fix https://github.com/objectbox/objectbox-java/issues/610
                 */
                BoxStore.deleteAllFiles(objectStoreDirectory);
                boxStoreMap.put(objectStoreDirectory.getAbsolutePath(), boxStore = buildBox(boxStoreBuilder, objectStoreDirectory));
            } catch (Exception retryEx) {
                retryEx.printStackTrace();
            }
        }
        return boxStore;
    }

    /**
     * 创建box
     *
     * @param application
     * @param boxStoreBuilder
     * @param dbName          数据库名字 非路径
     * @return
     */
    public static synchronized BoxStore getBoxStore(Application application,
                                                    BoxStoreBuilder boxStoreBuilder,
                                                    String dbName) {
        return getBoxStore(boxStoreBuilder, new File(application.getCacheDir(), dbName));
    }


    /**
     * 构建数据库
     *
     * @param objectStoreDirectory
     * @return
     * @throws io.objectbox.exception.DbException
     */
    private static synchronized BoxStore buildBox(BoxStoreBuilder boxStoreBuilder, @NonNull File objectStoreDirectory) throws io.objectbox.exception.DbException {
        return boxStoreBuilder
                .directory(objectStoreDirectory)
                .build();
    }


}
