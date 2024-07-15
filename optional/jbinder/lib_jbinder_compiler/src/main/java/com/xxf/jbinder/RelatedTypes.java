package com.xxf.jbinder;

import com.squareup.javapoet.ClassName;

final class RelatedTypes {
    private static final String BINDER = "android.os";
    ClassName Binder = ClassName.get(BINDER, "Binder");
    ClassName IBinder = ClassName.get(BINDER, "IBinder");
    ClassName Override = ClassName.get(Override.class);
    ClassName Throwable = ClassName.get(Throwable.class);

    private static final String OK_BINDER = "com.xxf.jbinder";
    ClassName jBinderFactory = ClassName.get(OK_BINDER, "JBinderFactory");
    ClassName Function = jBinderFactory.nestedClass("Function");
    ClassName BaseBinder = jBinderFactory.nestedClass("BaseBinder");
    ClassName BaseProxy = jBinderFactory.nestedClass("BaseProxy");

    ClassName String = ClassName.get(String.class);
    ClassName Class = ClassName.get(Class.class);
}
