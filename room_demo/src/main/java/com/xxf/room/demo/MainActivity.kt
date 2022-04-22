package com.xxf.room.demo

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.room.Room
import com.xxf.arch.json.JsonUtils
import com.xxf.room.demo.dao.UserDao
import com.xxf.room.demo.database.UserDatabase
import com.xxf.room.demo.databinding.ActivityMainBinding
import com.xxf.room.demo.model.User
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers


class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater);
    }
    private val db by lazy {
        Room.databaseBuilder(applicationContext, UserDatabase::class.java, "test_db7")
            .allowMainThreadQueries()
                .allowMainThreadQueries()
                .build()
    }
    private val adapter by lazy {
        UserAdapter();
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()

        val testModel=TestColorModel("#80E5E5E5", 0x80E5E5E5.toInt());
        val toJsonString = JsonUtils.toJsonString(testModel);
        Log.d("=====>序列化颜色:",toJsonString);

        val deserlizeModel = JsonUtils.toBean(toJsonString,TestColorModel::class.java);
        Log.d("=====>反序列化颜色:",""+deserlizeModel);
        Log.d("=====>反序列化颜色2:","#"+Integer.toHexString(deserlizeModel.colorInt));
    }

    private fun initView() {
        binding.recyclerView.adapter = adapter;
        binding.btnInsert.setOnClickListener {
            if (TextUtils.isEmpty(binding.etInput.text)) {
                Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
            val user = User();
            user.id = System.currentTimeMillis().toString();
            user.name = binding.etInput.text.toString();
            user.releaseYear = 1970;
            db.userDao().insertAll(user);
            Log.d("=====>insert by rx2:", "" + user.id);
            Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
        }
        binding.btnDeleteAll.setOnClickListener {
            db.userDao().deleteAllUser();
        }
        binding.btnUpdate.setOnClickListener {
            if (TextUtils.isEmpty(binding.etInput.text)) {
                Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
            val get = db.userDao().loadAll()
                    .get(0);
            get.name = binding.etInput.text.toString();
            db.userDao().insertAll(get);
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
        }

        binding.btnQuery.setOnClickListener {
            if (TextUtils.isEmpty(binding.etInput.text)) {
                Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
            val loadByName = db.userDao().loadByName(binding.etInput.text.toString());
            adapter.bindData(true, loadByName);
        }
        val list= mutableListOf<String>()
        for(i in 0..5)
        {
            list.add(""+i);
            val user = User();
            user.id = ""+i;
            user.name = binding.etInput.text.toString();
            user.releaseYear = 1970;
            db.userDao().insertAll(user);
        }

        //监听数据库变化
        db.userDao().loadAllById2(*list.toTypedArray())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adapter.bindData(true, it);
                    Log.d("=====>data by rx", "" + it);
                }

        //监听数据库变化
        db.userDao().loadAllById3(*list.toTypedArray())
            .observe(this,object: Observer<List<User>> {
                override fun onChanged(t: List<User>?) {
                    Log.d("=====>data by rx2:", "" + t);
                }
            })
    }

    private fun yc(dao: UserDao) {
        Thread(Runnable {
            val list: MutableList<User> = mutableListOf<User>();
            for (index in 0..10000) {
                list.add(User(""+(index + 9999).toLong(), "测试:" + index, 1))
            }
            var start = System.currentTimeMillis();
            dao.insertAll2(list);
            Log.d("=====>", "take2:" + (System.currentTimeMillis() - start));

            start = System.currentTimeMillis();
            val loadByName = dao.loadByName2("测");
            Log.d("=====>", "like query room:" + (System.currentTimeMillis() - start) + "  " + loadByName);

        }).start();
    }
}