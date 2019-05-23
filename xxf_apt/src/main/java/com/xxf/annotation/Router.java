package com.xxf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 路由
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Router {

    /**
     * 路由地址
     *
     * @return
     */
    String path();

    /**
     * 注释或者描述目的
     *
     * @return
     */
    String desc() default "";
}
