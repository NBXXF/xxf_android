package com.xxf.arch.http.converter;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import okio.Buffer;

public interface OnGsonConvertFailListener {
    /**
     * 确保下游处理安全
     * <p>
     *
     * @param gson
     * @param adapter
     * @param json
     * @param e
     */
    void onResponseConvertFail(Gson gson, TypeAdapter adapter, @Nullable String json, Throwable e);
}
