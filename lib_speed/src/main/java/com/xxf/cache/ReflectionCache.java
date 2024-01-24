

package com.xxf.cache;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReflectionCache {
    private static final ReflectionCache instance = new ReflectionCache();

    public static ReflectionCache getInstance() {
        return instance;
    }

    private final Map<Class<?>, Map<String, Field>> fields = new HashMap<>();
    private final Map<Class<?>, Map<String, Method>> methods = new HashMap<>();

    public synchronized Field getField(Class<?> clazz, String name) {
        Map<String, Field> fieldsForClass = fields.get(clazz);
        if (fieldsForClass == null) {
            fieldsForClass = new HashMap<>();
            fields.put(clazz, fieldsForClass);
        }
        Field field = fieldsForClass.get(name);
        if (field == null) {
            try {
                field = clazz.getDeclaredField(name);
                field.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new IllegalStateException(e);
            }
            fieldsForClass.put(name, field);
        }
        return field;
    }

    public synchronized Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        List<Class<?>> parameterClasses = Arrays.asList(parameterTypes);
        StringBuilder stringBuilder = new StringBuilder(methodName);
        for (Class<?> c : parameterClasses) {
            stringBuilder.append(c.getName());
        }
        String name = stringBuilder.toString();
        Map<String, Method> fieldsForClass = methods.get(clazz);
        if (fieldsForClass == null) {
            fieldsForClass = new HashMap<>();
            methods.put(clazz, fieldsForClass);
        }
        Method method = fieldsForClass.get(name);
        if (method == null) {
            try {
                method = clazz.getMethod(name, parameterTypes);
                method.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            fieldsForClass.put(name, method);
        }
        return method;
    }

}
