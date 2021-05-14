package com.xxf.view.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.xxf.arch.XXF;
import com.xxf.arch.utils.EncryptUtils;
import com.xxf.arch.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
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


    public static int getIdByName(String name) {
        return getContext().getResources().getIdentifier(name, "id", getContext().getPackageName());
    }


    public static int getStringIdByName(String name) {
        return getContext().getResources().getIdentifier(name, "string", getContext().getPackageName());
    }


    public static int getColorIdByName(String name) {
        return getContext().getResources().getIdentifier(name, "color", getContext().getPackageName());
    }


    public static int getDimenIdByName(String name) {
        return getContext().getResources().getIdentifier(name, "dimen", getContext().getPackageName());
    }


    public static int getDrawableIdByName(String name) {
        return getContext().getResources().getIdentifier(name, "drawable", getContext().getPackageName());
    }


    public static int getMipmapIdByName(String name) {
        return getContext().getResources().getIdentifier(name, "mipmap", getContext().getPackageName());
    }


    public static int getLayoutIdByName(String name) {
        return getContext().getResources().getIdentifier(name, "layout", getContext().getPackageName());
    }


    public static int getStyleIdByName(String name) {
        return getContext().getResources().getIdentifier(name, "style", getContext().getPackageName());
    }


    public static int getAnimIdByName(String name) {
        return getContext().getResources().getIdentifier(name, "anim", getContext().getPackageName());
    }


    public static int getMenuIdByName(String name) {
        return getContext().getResources().getIdentifier(name, "menu", getContext().getPackageName());
    }


    public static boolean copyFileFromAssets(final String assetsFilePath, final String destFilePath) {
        boolean res = true;
        try {
            String[] assets = getContext().getAssets().list(assetsFilePath);
            if (assets != null && assets.length > 0) {
                for (String asset : assets) {
                    res &= copyFileFromAssets(assetsFilePath + "/" + asset, destFilePath + "/" + asset);
                }
            } else {
                res = FileUtils.writeFileFromIS(
                        new File(destFilePath),
                        getContext().getAssets().open(assetsFilePath), false
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
            res = false;
        }
        return res;
    }


    public static String readAssets2String(final String assetsFilePath) {
        return readAssets2String(assetsFilePath, null);
    }


    public static String readAssets2String(final String assetsFilePath, @Nullable final String charsetName) {
        try {
            InputStream is = getContext().getAssets().open(assetsFilePath);
            byte[] bytes = FileUtils.getFileBytes(is);
            if (bytes == null) return "";
            if (TextUtils.isEmpty(charsetName)) {
                return new String(bytes);
            } else {
                try {
                    return new String(bytes, charsetName);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 检查所有
     * 主module app R文件的内容是已经合并好的,但是有第三方依赖库里面的资源重复
     * 检查资源重复 第一步只检查字符串
     * 未来:id 颜色等等,请期待
     * 避免lint lint缺陷就是能忽略检查
     */
    public static void checkResources(List<Integer> ignoreIds) {
        Class<?> aClass = null;
        try {
            aClass = Class.forName(getContext().getPackageName() + ".R");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        if (aClass == null) {
            return;
        }
        List<Integer> stringResources = getStringResources(aClass);
        stringResources.removeAll(ignoreIds);
        /**
         * 字符串
         * key:string values:ids集合
         */
        Map<String, List<String>> stringRepeatMap = new HashMap<>();
        for (Integer id : stringResources) {
            String key = getString(id);
            List<String> idNames = stringRepeatMap.get(key);
            if (idNames == null) {
                idNames = new ArrayList<>();
            }
            idNames.add(getContext().getResources().getResourceEntryName(id));
            stringRepeatMap.put(key, idNames);
        }

        List<Integer> drawableResources = getDrawableResources(aClass);
        drawableResources.removeAll(ignoreIds);

        /**
         * 图片
         * key:drawable values:ids集合
         */
        Map<String, List<String>> drawableRepeatMap = new HashMap<>();
        for (Integer id : drawableResources) {
            /**
             * 会存在load 不出来的xml的图片
             */
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), id);
            if (bitmap != null) {
                bitmap = BitmapUtils.scale(bitmap, 32, 32);
                bitmap = BitmapUtils.grey(bitmap);
            } else {
                /**
                 * 加载xml中的drawable
                 */
                //bitmap = BitmapUtils.drawableToBitmap(getDrawable(id));
            }
            if (bitmap == null) {
                continue;
            }
            byte[] bytes = BitmapUtils.toByte(bitmap);
            BitmapUtils.recycle(bitmap);
            String key = null;
            if (bytes != null) {
                try {
                    key = EncryptUtils.encryptMD5ToString(bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bytes = null;
            }
            if (TextUtils.isEmpty(key)) {
                continue;
            }
            List<String> idNames = drawableRepeatMap.get(key);
            if (idNames == null) {
                idNames = new ArrayList<>();
            }
            idNames.add(getContext().getResources().getResourceEntryName(id));
            drawableRepeatMap.put(key, idNames);
        }
        stringRepeatMap = convertRepeatMap(stringRepeatMap);
        StringBuffer repeatBuilder = new StringBuffer("String重复" + stringRepeatMap.size() + "条:");
        for (Map.Entry<String, List<String>> entry : stringRepeatMap.entrySet()) {
            repeatBuilder.append(entry.getValue());
            repeatBuilder.append(";");
        }


        drawableRepeatMap = convertRepeatMap(drawableRepeatMap);
        repeatBuilder.append("image相似或重复" + drawableRepeatMap.size() + "条:");
        for (Map.Entry<String, List<String>> entry : drawableRepeatMap.entrySet()) {
            repeatBuilder.append(entry.getValue());
            repeatBuilder.append(";");
        }

        if (!stringRepeatMap.isEmpty() || !drawableRepeatMap.isEmpty()) {
            throw new RepeatResourcesException(repeatBuilder.toString());
        }
    }

    private static Map<String, List<String>> convertRepeatMap(Map<String, List<String>> map) {
        if (map != null) {
            Iterator<Map.Entry<String, List<String>>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<String>> next = iterator.next();
                if (next.getValue() == null || next.getValue().size() <= 1) {
                    iterator.remove();
                }
            }
        }
        return map;
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

    /**
     * 获取指定R文件下面的所有drawable 资源ids
     *
     * @param rClazz
     * @return
     */
    public static List<Integer> getDrawableResources(Class... rClazz) {
        List<Integer> idList = new ArrayList<>();
        if (rClazz != null) {
            for (Class aClass : rClazz) {
                Class innerClazz[] = aClass.getDeclaredClasses();
                for (Class claszInner : innerClazz) {
                    if (TextUtils.equals(claszInner.getSimpleName(), "drawable") || TextUtils.equals(claszInner.getSimpleName(), "mipmap")) {
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
        }
        return idList;
    }

    public static class RepeatResourcesException extends RuntimeException {
        public RepeatResourcesException(String message) {
            super(message);
        }
    }
}
