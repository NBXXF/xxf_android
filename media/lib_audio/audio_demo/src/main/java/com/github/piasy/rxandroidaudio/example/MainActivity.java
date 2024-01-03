package com.github.piasy.rxandroidaudio.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.mBtnFileMode)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fileMode();
                        ;
                    }
                });
        findViewById(R.id.mBtnStreamMode)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        streamMode();
                    }
                });
    }


    public void fileMode() {
        startActivity(new Intent(this,FileActivity.class));
    }


    public void streamMode() {
        startActivity(new Intent(this, StreamActivity.class));
    }
}
