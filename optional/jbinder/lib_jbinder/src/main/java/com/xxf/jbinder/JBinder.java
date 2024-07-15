package com.xxf.jbinder;

import android.os.Binder;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public final class JBinder {
    static final String TAG = JBinder.class.getSimpleName();

    public static Binder create(Object remoteObject) {
        Class<?> jBinderInterface = getJBinderInterface(remoteObject);
        require(jBinderInterface != null,
                "Remote object must implement only one interface with @AIDL annotation");
        return create((Class<Object>) jBinderInterface, remoteObject);
    }

    public static <T> Binder create(Class<T> jBinderInterface, T remoteObject) {
        return getFactory(jBinderInterface).newBinder(jBinderInterface, remoteObject);
    }

    public static <T> T proxy(Class<T> serviceClass, IBinder binder) {
        if (binder instanceof JBinderFactory.BaseBinder) {
            return (T) ((JBinderFactory.BaseBinder) binder).getRemoteObject();
        }
        return (T) getFactory(serviceClass).newProxy(serviceClass, binder);
    }

    private static final Map<Class<?>, JBinderFactory> factories = new HashMap<>();
    private static final JBinderFactory defaultFactory = new ReflectionFactory();

    private static JBinderFactory getFactory(Class<?> serviceClass) {
        require(isJBinderInterface(serviceClass),
                "Service class must be an interface with @AIDL annotation");
        JBinderFactory factory = factories.get(serviceClass);
        if (factory != null) {
            return factory;
        }
        String factoryClassName = serviceClass.getName() + "Factory";
        try {
            ClassLoader classLoader = serviceClass.getClassLoader();
            Class<?> factoryClass = classLoader.loadClass(factoryClassName);
            factory = (JBinderFactory) factoryClass.newInstance();
        } catch (Throwable e) {
            Log.w(TAG, "Unable to load the factory class " + factoryClassName + ", " +
                    "the default factory will be used");
        }
        if (factory == null) {
            factory = defaultFactory;
        }
        factories.put(serviceClass, factory);
        return factory;
    }

    private static void require(boolean value, String message) {
        if (!value) throw new IllegalArgumentException(message);
    }

    static boolean isJBinderInterface(Class<?> cls) {
        return cls.isInterface() && cls.isAnnotationPresent(AIDL.class);
    }

    static Class<?> getJBinderInterface(Object object) {
        Class<?>[] interfaces = object.getClass().getInterfaces();
        List<Class<?>> jBinderInterfaces = new ArrayList<>();
        for (Class<?> anInterface : interfaces) {
            if (isJBinderInterface(anInterface)) {
                jBinderInterfaces.add(anInterface);
            }
        }
        if (jBinderInterfaces.size() != 1) return null;
        return jBinderInterfaces.get(0);
    }

    public static String getFunctionId(Method method) {
        StringBuilder functionId = new StringBuilder(method.getName());
        StringBuilder params = new StringBuilder();
        boolean isFirst = true;
        for (Class<?> paramType : method.getParameterTypes()) {
            params.append(isFirst ? "" : ",").append(paramType.getName());
            isFirst = false;
        }
        if (params.length() <= 24) {
            return functionId.append("(").append(params).append(")").toString();
        }
        try {
            byte[] bytes = params.toString().getBytes();
            byte[] md5 = MessageDigest.getInstance("MD5").digest(bytes);
            String base64 = Base64.encodeToString(md5, Base64.NO_WRAP);
            return functionId.append("(").append(base64).append(")").toString();
        } catch (Throwable e) {
            throw ErrorUtils.wrap(e);
        }
    }
}
