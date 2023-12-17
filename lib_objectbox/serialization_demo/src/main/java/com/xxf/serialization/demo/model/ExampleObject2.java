package com.xxf.serialization.demo.model;

import com.twitter.serial.serializer.CoreSerializers;
import com.twitter.serial.serializer.ObjectSerializer;
import com.twitter.serial.serializer.SerializationContext;
import com.twitter.serial.serializer.Serializer;
import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import com.xxf.serialization.demo.BinSerializers;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExampleObject2 {
    public static final ObjectSerializer<ExampleObject2> SERIALIZER = new ExampleObjectSerializer();
    public static Serializer<List<String>> listSerializer = BinSerializers.INSTANCE.getListSerializer(CoreSerializers.STRING);
    public static Serializer<Map<String, String>> mapSerializer = BinSerializers.INSTANCE.getMapSerializer(CoreSerializers.STRING, CoreSerializers.STRING);

    public int age;
    public String name;
    public int int1 = 0;
    public int int2 = 0;
    public int int3 = 0;
    public int int4 = 0;

    public String str1 = "x";
    public String str2 = "x";
    public String str3 = "x";
    public String str4 = "x";


    public boolean bool1 = true;
    public boolean bool2 = true;
    public boolean bool3 = true;
    public boolean bool4 = true;

    public List<String> subNodes;
    public Map<String, String> map;


    public ExampleObject2(int age, String name, List<String> subNodes, Map<String, String> map) {
        this.age = age;
        this.name = name;
        this.subNodes = subNodes;
        this.map = map;
    }

    @Override
    public String toString() {
        return "ExampleObject{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", subNodes=" + subNodes +
                ", map=" + map +
                '}';
    }

    private static final class ExampleObjectSerializer extends ObjectSerializer<ExampleObject2> {
        @Override
        protected void serializeObject(@NotNull SerializationContext context, @NotNull SerializerOutput output,
                                       @NotNull ExampleObject2 object) throws IOException {
            output
                    .writeInt(object.age) // first field
                    .writeString(object.name)
                    .writeInt(object.int1)
                    .writeInt(object.int2)
                    .writeInt(object.int3)
                    .writeInt(object.int4)
                    .writeString(object.str1)
                    .writeString(object.str2)
                    .writeString(object.str3)
                    .writeString(object.str4)
                    .writeBoolean(object.bool1)
                    .writeBoolean(object.bool2)
                    .writeBoolean(object.bool3)
                    .writeBoolean(object.bool4)
                    .writeObject(context, object.subNodes, listSerializer)
                    .writeObject(context, object.map, mapSerializer)
            ; // second field
        }

        @Override
        @NotNull
        protected ExampleObject2 deserializeObject(@NotNull SerializationContext context, @NotNull SerializerInput input,
                                                   int versionNumber) throws IOException, ClassNotFoundException {
            final int num = input.readInt(); // first field
            final String obj = input.readString(); // second field
            final int int1=input.readInt();
            final int int2=input.readInt();
            final int int3=input.readInt();
            final int int4=input.readInt();

            final String str1=input.readString();
            final String str2=input.readString();
            final String str3=input.readString();
            final String str4=input.readString();

            final boolean bool1=input.readBoolean();
            final boolean bool2=input.readBoolean();
            final boolean bool3=input.readBoolean();
            final boolean bool4=input.readBoolean();

            List<String> strings = input.readObject(context, listSerializer);
            Map<String, String> stringStringMap = input.readObject(context, mapSerializer);
            ExampleObject2 exampleObject = new ExampleObject2(num, obj, strings, stringStringMap);
            exampleObject.int1=int1;
            exampleObject.int2=int2;
            exampleObject.int3=int3;
            exampleObject.int4=int4;


            exampleObject.str1=str1;
            exampleObject.str2=str2;
            exampleObject.str3=str3;
            exampleObject.str4=str4;

            exampleObject.bool1=bool1;
            exampleObject.bool2=bool2;
            exampleObject.bool3=bool3;
            exampleObject.bool4=bool4;
            return exampleObject;
        }
    }
}