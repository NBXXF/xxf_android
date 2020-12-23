package com.xxf.arch.json.datastructure;

import androidx.annotation.NonNull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 返回list对象 json null转换成空集合 而不是null对象
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/14 17:19
 */
@JsonAdapter(ListOrEmpty.ListOrEmptyTypeAdapter.class)
public final class ListOrEmpty<T> extends ArrayList<T> {

    public ListOrEmpty(List<T> asList) {
        super(asList);
    }

    public ListOrEmpty() {
    }

    public final static class ListOrEmptyTypeAdapter<T> implements JsonDeserializer<ListOrEmpty<T>> {

        @Override
        public ListOrEmpty<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
            T[] deserialize = null;
            if (json.isJsonArray()) {
                deserialize = context.deserialize(json, TypeToken.getArray(itemType).getType());
            }
            if (deserialize != null) {
                return new ListOrEmpty(Arrays.asList(deserialize));
            }
            return new ListOrEmpty();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
