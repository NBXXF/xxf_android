package com.xxf.adapter.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.xxf.adapter.demo.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TestAdapter adapter = new TestAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        System.out.println("==========>onChildViewAttachedToWindow  init");
        adapter.setHasStableIds(true);
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
        List<Integer> list = new ArrayList<>();
        int count = new Random().nextInt(100) + 2000;
        for (int i = 0; i < count; i++) {
            list.add(i);
        }
        adapter.bindData(true, list);
        binding.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
            }
        });


    }
}