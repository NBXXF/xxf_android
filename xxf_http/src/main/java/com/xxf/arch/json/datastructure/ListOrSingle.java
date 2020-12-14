package com.xxf.arch.json.datastructure;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
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
 * @Description: 返回list对象 如果json是jsonObject 那么久返回一个元素的list
 * @Author: XGod
 * @CreateDate: 2020/12/14 17:19
 */
@JsonAdapter(ListOrSingle.ListOrSingleTypeAdapter.class)
public final class ListOrSingle<T> extends ArrayList<T> {

    public ListOrSingle(List<T> asList) {
        super(asList);
    }

    public ListOrSingle() {
    }

    public static class ListOrSingleTypeAdapter<T> implements JsonDeserializer<ListOrSingle<T>> {

        @Override
        public ListOrSingle<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
            T[] deserialize = null;
            if (json.isJsonArray()) {
                deserialize = context.deserialize(json, TypeToken.getArray(itemType).getType());

            } else if (json.isJsonObject() || json.isJsonPrimitive()) {
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(json);
                deserialize = context.deserialize(jsonArray, TypeToken.getArray(itemType).getType());
            }
            if (deserialize != null) {
                return new ListOrSingle(Arrays.asList(deserialize));
            }
            return new ListOrSingle();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
