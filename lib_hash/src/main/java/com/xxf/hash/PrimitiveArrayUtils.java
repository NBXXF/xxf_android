package com.xxf.hash;


public abstract class PrimitiveArrayUtils {
    private volatile static PrimitiveArrayUtils instanceUnsafe;
    private final static PrimitiveArrayUtils instanceSafe = new SafeImpl();

    public static PrimitiveArrayUtils getInstance() {
        PrimitiveArrayUtils instance = PrimitiveArrayUtils.instanceUnsafe;
        if (instance == null) instance = instanceSafe;
        return instance;
    }


    public static PrimitiveArrayUtils getInstanceSafe() {
        return instanceSafe;
    }

    public abstract int getIntLE(byte[] bytes, int index);

    public abstract int getIntBE(byte[] bytes, int index);

    public abstract long getLongLE(byte[] bytes, int index);

    public abstract long getLongBE(byte[] bytes, int index);

    public abstract int getIntLE(char[] chars, int index);


    private static class SafeImpl extends PrimitiveArrayUtils {
        public int getIntLE(byte[] bytes, int index) {
            return (bytes[index] & 0xff) | ((bytes[index + 1] & 0xff) << 8) |
                    ((bytes[index + 2] & 0xff) << 16) | (bytes[index + 3] << 24);
        }

        public int getIntBE(byte[] bytes, int index) {
            return (bytes[index + 3] & 0xff) | ((bytes[index + 2] & 0xff) << 8) |
                    ((bytes[index + 1] & 0xff) << 16) | (bytes[index] << 24);
        }

        public long getLongLE(byte[] bytes, int index) {
            return (bytes[index] & 0xff) | ((bytes[index + 1] & 0xff) << 8) |
                    ((bytes[index + 2] & 0xff) << 16) | ((bytes[index + 3] & 0xffL) << 24) |
                    ((bytes[index + 4] & 0xffL) << 32) | ((bytes[index + 5] & 0xffL) << 40) |
                    ((bytes[index + 6] & 0xffL) << 48) | (((long) bytes[index + 7]) << 56);
        }

        public long getLongBE(byte[] bytes, int index) {
            return (bytes[index + 7] & 0xff) | ((bytes[index + 6] & 0xff) << 8) |
                    ((bytes[index + 5] & 0xff) << 16) | ((bytes[index + 4] & 0xffL) << 24) |
                    ((bytes[index + 3] & 0xffL) << 32) | ((bytes[index + 2] & 0xffL) << 40) |
                    ((bytes[index + 1] & 0xffL) << 48) | (((long) bytes[index]) << 56);
        }

        /** Little endian. */
        public int getIntLE(char[] chars, int index) {
            return (chars[index] & 0xffff) | ((chars[index + 1] & 0xffff) << 16);
        }

    }

}
