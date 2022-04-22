package com.xxf.serialization.demo;

import android.os.Parcel;
import android.os.Parcelable;

import com.xxf.serialization.demo.model.ExampleObject;
import com.xxf.serialization.demo.model.ExampleObject3;

import java.util.ArrayList;
import java.util.List;

/**
 * 潘正炼创建于 2022/4/20 20:01
 */
public class ParcelableUtil {
    public static <T extends Parcelable> byte[]  marshall(T parceable) {
        Parcel parcel = Parcel.obtain();
        parceable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }


    public static byte[] marshall(List<ExampleObject3> parceable) {
        Parcel parcel = Parcel.obtain();
        parcel.writeTypedList(parceable);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static ArrayList<ExampleObject3> unmarshallList(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        ArrayList<ExampleObject3> formulaDTOS = parcel.createTypedArrayList(ExampleObject3.CREATOR);
        parcel.recycle();

        return formulaDTOS;
    }

    

    public static Parcel unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0); // This is extremely important!
        return parcel;
    }

    public static <T> T unmarshall(byte[] bytes, Parcelable.Creator<T> creator) {
        Parcel parcel = unmarshall(bytes);
        T result = creator.createFromParcel(parcel);
        parcel.recycle();
        return result;
    }
}
