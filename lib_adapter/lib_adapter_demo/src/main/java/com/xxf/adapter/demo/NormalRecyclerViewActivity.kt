package com.xxf.adapter.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.xxf.adapter.demo.databinding.ActivityMainBinding
import com.xxf.application.activity.bindExtra
import java.util.*

open class NormalRecyclerViewActivity : AppCompatActivity() {
    val uuid: String by bindExtra(defaultValue = "defaultXXX")

    val uuid2: String by bindExtra("KEY", defaultValue = "defaultXXX")

    val uuid3: String? by bindExtra("KEY")

    var uuid4: String? by bindExtra("KEY")

    var binding: ActivityMainBinding? = null
    var adapter = TestNormalAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        uuid4 = null;

        println("==========>get param:" + uuid);

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding!!.change.text = "diff"
        binding!!.change.setOnClickListener { v ->
            startActivity(
                Intent(
                    v.context,
                    MainActivity::class.java
                )
            )
        }
        setContentView(binding!!.root)
        println("==========>onChildViewAttachedToWindow  init")
        //adapter.setHasStableIds(true);
        binding!!.recyclerView.adapter = adapter
        //删除-> 改变焦点（上一个)  后删除当前
        // binding.recyclerView.find
        //创建第五条
        binding!!.recyclerView.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                val containingViewHolder = binding!!.recyclerView.findContainingViewHolder(view)
                if (containingViewHolder!!.adapterPosition == 5) {
                    //requestfoucs
                    binding!!.recyclerView.removeOnChildAttachStateChangeListener(this)
                }
                println("==========>onChildViewAttachedToWindow:AdapterPosition:" + containingViewHolder.adapterPosition + "  LayoutPosition:" + containingViewHolder.layoutPosition + "  hash:" + containingViewHolder.itemView)
            }

            override fun onChildViewDetachedFromWindow(view: View) {}
        })
        val list: MutableList<String> = ArrayList()
        val count = Random().nextInt(50)
        for (i in 0 until count) {
            //   list.add("i" + new Random().nextInt(100));
            list.add("i$i")
        }
        adapter.bindData(true, list)
        binding!!.refresh.setOnClickListener {
            val list: MutableList<String> = ArrayList()
            val count = Random().nextInt(50)
            for (i in 0 until count) {
                //  list.add("i" + new Random().nextInt(100));
                list.add("i$i")
            }
            adapter.bindData(true, list)
            Log.d("=======>list:", "" + list)
        }
        binding!!.insert.setOnClickListener { adapter.addItem(0, "hello") }
        binding!!.insertLast.setOnClickListener { adapter.addItem("hello foo") }
        binding!!.delete.setOnClickListener { adapter.removeItem(0) }
        binding!!.loadMore.setOnClickListener {
            val list: MutableList<String> = ArrayList()
            val count = Random().nextInt(50)
            for (i in 0 until count) {
                //  list.add("i" + new Random().nextInt(100));
                list.add("i$i")
            }
            adapter.addItems(list)
        }
    }
}