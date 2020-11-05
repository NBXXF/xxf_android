package com.xxf.arch.test.http;

import com.google.gson.annotations.JsonAdapter;
import com.xxf.arch.json.typeadapter.format.NumberFormatObject;
import com.xxf.arch.json.typeadapter.format.extend.FractionNumberObjectFormatTypeAdapter;
import com.xxf.arch.json.typeadapter.format.extend.PercentNumberObjectFormatTypeAdapter;

/**
 * @Description: TODO @XGode
 * @Author: XGod
 * @CreateDate: 2020/11/5 20:45
 */
public class FormatTestModel {

    public class MyFractionNumberObjectFormatTypeAdapter extends FractionNumberObjectFormatTypeAdapter {

        public MyFractionNumberObjectFormatTypeAdapter() {
            super(2, 4);
        }
    }

    public class MyPercentNumberObjectFormatTypeAdapter extends PercentNumberObjectFormatTypeAdapter {

        public MyPercentNumberObjectFormatTypeAdapter() {
            super(3, 3);
        }
    }

    public class MyPercentNumberObjectFormatTypeAdapter2 extends PercentNumberObjectFormatTypeAdapter {

        public MyPercentNumberObjectFormatTypeAdapter2() {
            super(4, 5);
        }
    }

    @JsonAdapter(FractionNumberObjectFormatTypeAdapter.class)
    public NumberFormatObject v1;


    @JsonAdapter(MyFractionNumberObjectFormatTypeAdapter.class)
    public NumberFormatObject v2;


    @JsonAdapter(MyPercentNumberObjectFormatTypeAdapter.class)
    public NumberFormatObject v3;

    @JsonAdapter(MyPercentNumberObjectFormatTypeAdapter2.class)
    public NumberFormatObject v4;

    @Override
    public String toString() {
        return "TestModel{" +
                "v1=" + v1 +
                ", v2=" + v2 +
                ", v3=" + v3 +
                ", v4=" + v4 +
                '}';
    }
}
