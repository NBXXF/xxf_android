package com.xxf.arch.json.typeadapter.enums;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.xxf.arch.json.datastructure.LongEnum;
import com.xxf.speed.collections.LongHashMap;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * long 类型的枚举
 *   @SerializedName("1")  自动序列化成整数[框架默认序列化成字符串]
 *   内部提高速度
 *
 * @param <T>
 */
public class LongEnumTypeAdapter<T extends Enum<T> & LongEnum> extends TypeAdapter<T> {
    private final Map<String, T> nameToConstant = new HashMap<String, T>();
    private final LongHashMap<T> cacheMap =new LongHashMap<>();

    public LongEnumTypeAdapter(final Class<T> classOfT) {
        try {
            // Uses reflection to find enum constants to work around name mismatches for obfuscated classes
            // Reflection access might throw SecurityException, therefore run this in privileged context;
            // should be acceptable because this only retrieves enum constants, but does not expose anything else
            Field[] constantFields = AccessController.doPrivileged(new PrivilegedAction<Field[]>() {
                @Override
                public Field[] run() {
                    Field[] fields = classOfT.getDeclaredFields();
                    ArrayList<Field> constantFieldsList = new ArrayList<Field>(fields.length);
                    for (Field f : fields) {
                        if (f.isEnumConstant()) {
                            constantFieldsList.add(f);
                        }
                    }

                    Field[] constantFields = constantFieldsList.toArray(new Field[0]);
                    AccessibleObject.setAccessible(constantFields, true);
                    return constantFields;
                }
            });
            for (Field constantField : constantFields) {
                @SuppressWarnings("unchecked")
                T constant = (T) (constantField.get(null));
                String name = constant.name();
                SerializedName annotation = constantField.getAnnotation(SerializedName.class);
                if (annotation != null) {
                    name = annotation.value();
                    for (String alternate : annotation.alternate()) {
                        nameToConstant.put(alternate, constant);
                    }
                }
                nameToConstant.put(name, constant);
                cacheMap.put(constant.getValue(),constant);
            }
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public T read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }else if(in.peek()==JsonToken.NUMBER){
            return cacheMap.get(in.nextLong());
        }
        return nameToConstant.get(in.nextString());
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        out.value(value == null ? null : value.getValue());
    }
}