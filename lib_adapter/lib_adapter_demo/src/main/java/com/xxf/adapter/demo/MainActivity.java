package com.xxf.adapter.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xxf.adapter.demo.databinding.ActivityMainBinding;
import com.xxf.utils.DensityUtil;
import com.xxf.view.recyclerview.adapter.EdgeSpringEffectFactory;
import com.xxf.view.recyclerview.itemdecorations.section.SectionItemDecoration;
import com.xxf.view.recyclerview.itemdecorations.section.SectionProvider;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TestAdapter adapter = new TestAdapter();

    TreeMap<Integer, String> sectionMap = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.change.setText("normal");
        binding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), NormalRecyclerViewActivity.class).putExtra("xx", "77463"));
            }
        });
        setContentView(binding.getRoot());
        System.out.println("==========>onChildViewAttachedToWindow  init");
        //adapter.setHasStableIds(true);
        binding.recyclerView.setAdapter(adapter);
        //删除-> 改变焦点（上一个)  后删除当前
        // binding.recyclerView.find
        //创建第五条
        binding.recyclerView.setEdgeEffectFactory(new EdgeSpringEffectFactory(0.5f, 0.5f));
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
        sectionMap.clear();
        sectionMap.put(0, "第一个分组");
        sectionMap.put(3, "第2个分组");
        Paint paint = new Paint();
        paint.setTextSize(DensityUtil.sp2px(13));
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);

        Paint background = new Paint();
        background.setColor(Color.WHITE);
        binding.recyclerView.addItemDecoration(new SectionItemDecoration(new SectionProvider() {
            @NotNull
            @Override
            public TreeMap<Integer, String> onProvideSection() {
                return sectionMap;
            }
        }, paint, background, DensityUtil.sp2px(30), DensityUtil.sp2px(16)));

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