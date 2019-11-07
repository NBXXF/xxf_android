package com.xxf.arch.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
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

    public static <T> T toBean(String json, Type typeOfT) throws JsonParseException {
        return gson.fromJson(json, typeOfT);
    }

    public static <T> T toBean(JsonElement json, Type typeOfT) throws JsonParseException {
        return gson.fromJson(json, typeOfT);
    }

    public static <T> T toBean(String json, Class<T> tClass) throws JsonParseException {
        return gson.fromJson(json, tClass);
    }

    public static <T> T toBean(JsonElement json, Class<T> tClass) throws JsonParseException {
        return gson.fromJson(json, tClass);
    }

    public static <T> List<T> toBeanList(String json, TypeToken<List<T>> typeToken) throws JsonParseException {
        return gson.fromJson(json, typeToken.getType());
    }

    public static <T> List<T> toBeanList(String json, Class<T> classz) throws JsonParseException {
        T[] arr = gson.fromJson(json, TypeToken.getArray(classz).getType());
        return Arrays.asList(arr);
      /*  List<T> lst = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            lst.add(gson.fromJson(elem, classz));
        }
        return lst;*/
    }

    public static <T> Map<String, T> toMap(String json, TypeToken<Map<String, T>> typeToken) throws JsonParseException {
        return gson.fromJson(json, typeToken.getType());
    }

}

