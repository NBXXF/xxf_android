package com.xxf.objectbox.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.xxf.objectbox.demo.model.Teacher;
import com.xxf.objectbox.demo.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            UserDbService.INSTANCE.clearTable(this);
            UserDbService.INSTANCE.addAll(this,Arrays.asList(new User(0,"张三",10),
                    new User(0,"李四",10),
                    new User(0,"王五",10),
                    new User(0,"甲6",10)));


            List<User> users = UserDbService.INSTANCE.queryAll(this);
            Log.d("=======>","query:"+users);
        }catch (Throwable e)
        {
            Log.d("=======>","error:"+e);
        }

        test();
        test2();
    }

    private void test()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserDbService.INSTANCE.clearTable(MainActivity.this);
                List<User> users=new ArrayList<>();
                for(int i=0;i<10000;i++) {
                    users.add( new User(0,"李四:"+i,i));
                }
                UserDbService.INSTANCE.addAll(MainActivity.this,users);

                long start=System.currentTimeMillis();
                UserDbService.INSTANCE.query(MainActivity.this,"李");
                Log.d("====> objectbox:",""+(System.currentTimeMillis()-start));
            }
        }).start();
    }

    private void test2()
    {
        Teacher aa = new Teacher(1001, "aa");
        TeacherDbService.INSTANCE.add(this,Arrays.asList(aa));

        aa.setId(1002);
        TeacherDbService.INSTANCE.add(this,Arrays.asList(aa));
        List<Teacher> teachers = TeacherDbService.INSTANCE.queryAll(this);
        Log.d("====>teachers:",""+teachers);
    }
}