package com.xxf.arch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this,TestActivity.class);
        intent.putExtra("name","xxx");
        intent.putExtra("age","12");
        intent.putExtra("desc","124");
        startActivity(intent);
    }
}
