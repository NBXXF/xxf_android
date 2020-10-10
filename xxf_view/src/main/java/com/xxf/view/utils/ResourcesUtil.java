package com.xxf.view.utils;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.SparseArray;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.xxf.arch.XXF;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 资源文件工具类
 */
public class ResourcesUtil {

    private static Context getContext() {
        return XXF.getApplication();
    }

    public static String getString(@StringRes int resId) {
        return getContext().getString(resId);
    }

    public static String getString(@StringRes int resId, Object... formatArgs) {
        return getContext().getString(resId, formatArgs);
    }

    public static Drawable getDrawable(@DrawableRes int resId) {
        return ContextCompat.getDrawable(getContext(), resId);
    }

    @ColorInt
    public static int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    /**
     * 检查资源重复 第一步只检查字符串
     * 未来:id 颜色等等,请期待
     * 避免lint lint缺陷就是能忽略检查
     */
    public static void checkResources(List<Integer> ignoreIds, Class... rClazz) throws RepeatResourcesException, ClassNotFoundException {
        if (rClazz != null) {
            Map<String, List<String>> stringIntegerMap = new HashMap<>();
            for (Class aClass : rClazz) {
                Class innerClazz[] = aClass.getDeclaredClasses();
                for (Class claszInner : innerClazz) {
                    /**
                     * find string.class
                     */
                    if (!TextUtils.equals(claszInner.getSimpleName(), "string")) {
                        continue;
                    }
                    Field[] fields = claszInner.getDeclaredFields();
                    for (Field field : fields) {

                        try {
                            int id = field.getInt(claszInner);
                            /**
                             * 忽略检查的id
                             */
                            if (ignoreIds != null && ignoreIds.contains(id)) {
                                continue;
                            }
                            String value = getString(id);
                            List<String> idNames = stringIntegerMap.get(value);
                            if (idNames == null) {
                                idNames = new ArrayList<>();
                            }
                            idNames.add(field.getName());
                            stringIntegerMap.put(value, idNames);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            boolean hasRepeat = false;
            StringBuffer repeatBuilder = new StringBuffer("String[内容重复]");
            for (Map.Entry<String, List<String>> entry : stringIntegerMap.entrySet()) {
                if (entry.getValue() != null && entry.getValue().size() > 1) {
                    hasRepeat = true;
                    repeatBuilder.append(entry.getValue());
                    repeatBuilder.append(";");
                }
            }
            if (hasRepeat) {
                throw new RepeatResourcesException(repeatBuilder.toString());
            }
        }
    }

    /**
     * 检查所有
     * 主module app R文件的内容是已经合并好的,但是有第三方依赖库里面的资源重复
     * 检查资源重复 第一步只检查字符串
     * 未来:id 颜色等等,请期待
     * 避免lint lint缺陷就是能忽略检查
     */
    public static void checkResources(List<Integer> ignoreIds) throws RepeatResourcesException, ClassNotFoundException {
        Class<?> aClass = Class.forName(getContext().getPackageName() + ".R");
        checkResources(ignoreIds, aClass);
    }


    /**
     * 获取r文件的所有字符串资源ids
     *
     * @param rClazz
     * @return
     */
    public static List<Integer> getStringResources(Class... rClazz) {
        List<Integer> idList = new ArrayList<>();
        if (rClazz != null) {
            for (Class aClass : rClazz) {
                Class innerClazz[] = aClass.getDeclaredClasses();
                for (Class claszInner : innerClazz) {
                    /**
                     * find string.class
                     */
                    if (!TextUtils.equals(claszInner.getSimpleName(), "string")) {
                        continue;
                    }
                    Field[] fields = claszInner.getDeclaredFields();
                    for (Field field : fields) {
                        try {
                            int id = field.getInt(claszInner);
                            idList.add(id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return idList;
    }

    private static class RepeatResourcesException extends Exception {
        public RepeatResourcesException(String message) {
            super(message);
        }
    }
}
