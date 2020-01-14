package com.xxf.arch.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * ClassName JsonUtils
 * Description  json处理工具类，暂时核心封装Gson解析方式
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date 创建时间：2015/6/17 9:43
 * version
 */
public class JsonUtils {

    private JsonUtils() {
    }

    private static Gson gson = null;

    static {
        gson = GsonFactory.createGson();
    }

    public static Gson getGson() {
        return gson;
    }


    public static JsonObject toJsonObject(Object object) throws JsonParseException {
        return (JsonObject) gson.toJsonTree(object);
    }

    public static <T> JsonArray toJsonArray(List<T> object) throws JsonParseException {
        return (JsonArray) gson.toJsonTree(object);
    }

    public static JsonElement toJsonElement(Object object) throws JsonParseException {
        return gson.toJsonTree(object);
    }

    public static String toJsonString(Object object) throws JsonParseException {
        //⚠️ 有转义字符了 增加了引号,不建议用gson.toJson(object);
        return toJsonElement(object).toString();
    }

    public static String toJsonStringSafe(Object object) {
        try {
            return toJsonString(object);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toType(String json, Type typeOfT) throws JsonParseException {
        return gson.fromJson(json, typeOfT);
    }

    public static <T> T toType(JsonElement json, Type typeOfT) throws JsonParseException {
        return gson.fromJson(json, typeOfT);
    }

    public static <T> T toBean(String json, Class<T> tClass) throws JsonParseException {
        return gson.fromJson(json, tClass);
    }

    /**
     * 解析范型类  eg. User<T> ; you can use JsonUtils.toBean(json,User.class,T.class)
     * <p>
     * public static class User<T> {
     * private T t;
     * <p>
     * public User(T t) {
     * this.t = t;
     * }
     * }
     *
     * @param json
     * @param tClass        注意不能是成员类或者方法类,可以是静态内部类或者直接声明.java文件的类
     * @param argumentsType 范型列表
     * @param <T>
     * @return
     * @throws JsonParseException 解析范型类  eg. User<T> ; you can use JsonUtils.toBean(json,User.class,T.class)
     */
    public static <T> T toBean(String json, Class<T> tClass, Class<?>... argumentsType) throws JsonParseException {
        Type type = TypeToken.getParameterized(tClass, argumentsType).getType();
        return toType(json, type);
    }


    public static <T> T toBean(JsonElement json, Class<T> tClass) throws JsonParseException {
        return gson.fromJson(json, tClass);
    }

    /**
     * 解析范型类  eg. User<T> ; you can use JsonUtils.toBean(json,User.class,T.class)
     * <p>
     * public static class User<T> {
     * private T t;
     * <p>
     * public User(T t) {
     * this.t = t;
     * }
     * }
     *
     * @param json
     * @param tClass        注意不能是成员类或者方法类,可以是静态内部类或者直接声明.java文件的类
     * @param argumentsType 范型列表
     * @param <T>
     * @return
     * @throws JsonParseException 解析范型类  eg. User<T> ; you can use JsonUtils.toBean(json,User.class,T.class)
     */
    public static <T> T toBean(JsonElement json, Class<T> tClass, Class<?>... argumentsType) throws JsonParseException {
        Type type = TypeToken.getParameterized(tClass, argumentsType).getType();
        return toType(json, type);
    }

    public static <T> List<T> toBeanList(String json, ListTypeToken<T> typeToken) throws JsonParseException {
        return gson.fromJson(json, typeToken.getType());
    }

    /**
     * 解析集合
     * List<T> lst = new ArrayList<T>();
     * JsonArray array = new JsonParser().parse(json).getAsJsonArray();
     * for (final JsonElement elem : array) {
     * lst.add(gson.fromJson(elem, classz));
     * }
     * return lst;
     *
     * @param json
     * @param classz
     * @param <T>
     * @return
     * @throws JsonParseException
     */

    public static <T> List<T> toBeanList(String json, Class<T> classz) throws JsonParseException {
        T[] arr = gson.fromJson(json, TypeToken.getArray(classz).getType());
        if (arr == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(arr);

    }

    public static <K, V> Map<K, V> toMap(String json, MapTypeToken<K, V> typeToken) throws JsonParseException {
        return gson.fromJson(json, typeToken.getType());
    }

}

