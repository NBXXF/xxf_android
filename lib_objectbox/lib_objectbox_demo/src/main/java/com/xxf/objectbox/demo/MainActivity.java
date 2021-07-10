package com.xxf.objectbox.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.xxf.objectbox.demo.model.User;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserDbService.INSTANCE.addAll(this,Arrays.asList(new User(1,"张三",10),
                new User(101,"李四",10),
                new User(5,"王五",10),
                new User(5,"甲6",10)));

        List<User> users = UserDbService.INSTANCE.queryAll(this);
        Log.d("=======>","query:"+users);
    }
}