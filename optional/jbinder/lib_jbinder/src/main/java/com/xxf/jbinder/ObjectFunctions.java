package com.xxf.jbinder;

final class ObjectFunctions {
    private static final String TO_STRING = "toString()";
    private static final String HASH_CODE = "hashCode()";
    private static final String EQUALS = "equals(java.lang.Object)";

    private static final JBinderFactory.Function toString = new JBinderFactory.Function() {
        @Override
        public Object invoke(Object obj, Object[] args) {
            return obj.toString();
        }
    };

    private static final JBinderFactory.Function hashCode = new JBinderFactory.Function() {
        @Override
        public Object invoke(Object obj, Object[] args) {
            return obj.hashCode();
        }
    };

    private static final JBinderFactory.Function equals = new JBinderFactory.Function() {
        @Override
        public Object invoke(Object obj, Object[] args) {
            return obj.equals(args[0]);
        }
    };

    public static void inject(JBinderFactory.BaseBinder binder) {
        binder.register(TO_STRING, toString);
        binder.register(HASH_CODE, hashCode);
        binder.register(EQUALS, equals);
    }

    public static String toString(JBinderFactory.BaseProxy proxy) {
        return (String) proxy.transact(0, TO_STRING);
    }

    public static int hashCode(JBinderFactory.BaseProxy proxy) {
        return (int) proxy.transact(0, HASH_CODE);
    }

    public static boolean equals(JBinderFactory.BaseProxy proxy, Object obj) {
        return (boolean) proxy.transact(0, EQUALS, obj);
    }
}
