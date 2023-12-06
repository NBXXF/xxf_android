package com.xxf.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxf.json.datastructure.ListOrEmpty;
import com.xxf.json.datastructure.ListOrSingle;
import com.xxf.json.exclusionstrategy.ExposeDeserializeExclusionStrategy;
import com.xxf.json.exclusionstrategy.ExposeSerializeExclusionStrategy;
import com.xxf.json.typeadapterfactory.SafeTypeAdapterFactory;


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
                //.enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                //不使用 与relam 插入更新违背
                //  .registerTypeAdapter(String.class, new StringNullAdapter())//将空字符串转换成""
                .registerTypeAdapterFactory(new SafeTypeAdapterFactory())
                .registerTypeAdapter(ListOrSingle.class, new ListOrSingle.ListOrSingleTypeAdapter())
                .registerTypeAdapter(ListOrEmpty.class, new ListOrEmpty.ListOrEmptyTypeAdapter())
                .addSerializationExclusionStrategy(new ExposeSerializeExclusionStrategy())
                .addDeserializationExclusionStrategy(new ExposeDeserializeExclusionStrategy());
        return newBuilder.create();
    }

}
