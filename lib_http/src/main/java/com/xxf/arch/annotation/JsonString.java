package com.xxf.arch.annotation;

import java.lang.annotation.ElementType;

/**
 * 由于@Query 不支持json 而是支持value.toString
 * 我们再这里拓展一下
 * 支持自定义模型传递为json
 */

/**
 * 支持自定义对象 @QueryMap @Field @FieldMap @HeaderMap @Path 转换成string
 *
 * @GET("telematics/v3/weather")
 * Observable<JsonObject> getCity(@JsonString @Query("test") TestQueryJsonField queryJsonField);
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface JsonString {
}
