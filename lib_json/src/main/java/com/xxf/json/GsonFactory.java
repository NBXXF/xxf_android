package com.xxf.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxf.json.datastructure.ListOrEmpty;
import com.xxf.json.datastructure.ListOrSingle;
import com.xxf.json.exclusionstrategy.ExposeDeserializeExclusionStrategy;
import com.xxf.json.exclusionstrategy.ExposeSerializeExclusionStrategy;
import com.xxf.json.typeadapter.bool.BooleanTypeAdapter;
import com.xxf.json.typeadapter.number.IntegerTypeAdapter;
import com.xxf.json.typeadapter.number.LongTypeAdapter;
import com.xxf.json.typeadapter.number.PercentageDoubleTypeAdapter;
import com.xxf.json.typeadapter.number.PercentageFloatTypeAdapter;
import com.xxf.json.typeadapter.orgJSON.JSONArrayAdapter;
import com.xxf.json.typeadapter.orgJSON.JSONObjectAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 此类不能转kotlin 否则int 兼容有问题
 */
public class GsonFactory {
    /**
     * 自定义gson
     *
     * @return
     */
    public static Gson createGson() {
        GsonBuilder newBuilder = new GsonBuilder()
                .setLenient()// json宽松
                .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                .disableHtmlEscaping() //默认是GSON把HTML 转义的
                //不使用 与relam 插入更新违背
                //  .registerTypeAdapter(String.class, new StringNullAdapter())//将空字符串转换成""
                .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter())
                .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
                .registerTypeAdapter(int.class, new IntegerTypeAdapter())
                .registerTypeAdapter(Long.class, new LongTypeAdapter())
                .registerTypeAdapter(Double.class, new PercentageDoubleTypeAdapter())
                .registerTypeAdapter(double.class, new PercentageDoubleTypeAdapter())
                .registerTypeAdapter(Float.class, new PercentageFloatTypeAdapter())
                .registerTypeAdapter(float.class, new PercentageFloatTypeAdapter())
                .registerTypeAdapter(ListOrSingle.class, new ListOrSingle.ListOrSingleTypeAdapter())
                .registerTypeAdapter(ListOrEmpty.class, new ListOrEmpty.ListOrEmptyTypeAdapter())
                .registerTypeAdapter(JSONObject.class, new JSONObjectAdapter())
                .registerTypeAdapter(JSONArray.class, new JSONArrayAdapter())
                .addSerializationExclusionStrategy(new ExposeSerializeExclusionStrategy())
                .addDeserializationExclusionStrategy(new ExposeDeserializeExclusionStrategy());
        return newBuilder.create();
    }

}
