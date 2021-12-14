package com.xxf.adapter.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.xxf.adapter.demo.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NormalRecyclerViewActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TestNormalAdapter adapter = new TestNormalAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.change.setText("diff");
        binding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), MainActivity.class));
            }
        });
        setContentView(binding.getRoot());
        System.out.println("==========>onChildViewAttachedToWindow  init");
        //adapter.setHasStableIds(true);
        binding.recyclerView.setAdapter(adapter);
        //删除-> 改变焦点（上一个)  后删除当前
        // binding.recyclerView.find
        //创建第五条
        binding.recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                RecyclerView.ViewHolder containingViewHolder = binding.recyclerView.findContainingViewHolder(view);
                if (containingViewHolder.getAdapterPosition() == 5) {
                    //requestfoucs
                    binding.recyclerView.removeOnChildAttachStateChangeListener(this);
                }
                System.out.println("==========>onChildViewAttachedToWindow:AdapterPosition:" + containingViewHolder.getAdapterPosition() + "  LayoutPosition:" + containingViewHolder.getLayoutPosition() + "  hash:" + containingViewHolder.itemView);
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
            }
        });
        List<String> list = new ArrayList<>();
        int count = new Random().nextInt(50);
        for (int i = 0; i < count; i++) {
            //   list.add("i" + new Random().nextInt(100));
            list.add("i" + i);
        }
        adapter.bindData(true, list);
        binding.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                int count = new Random().nextInt(50);
                for (int i = 0; i < count; i++) {
                    //  list.add("i" + new Random().nextInt(100));
                    list.add("i" + i);
                }
                adapter.bindData(true, list);
                Log.d("=======>list:", "" + list);
            }
        });

        binding.insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(0, "hello");
            }
        });
        binding.insertLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem("hello foo");
            }
        });
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeItem(0);
            }
        });
        binding.loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                int count = new Random().nextInt(50);
                for (int i = 0; i < count; i++) {
                    //  list.add("i" + new Random().nextInt(100));
                    list.add("i" + i);
                }
                adapter.addItems(list);
            }
        });

    }
}