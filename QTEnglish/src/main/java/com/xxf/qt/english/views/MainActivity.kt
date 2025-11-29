package com.xxf.qt.english.views

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.xxf.arch.bindErrorNotice
import com.xxf.ktx.startActivity
import com.xxf.log.LogUtils.logD
import com.xxf.log.logE
import com.xxf.qt.english.models.WordInfo
import com.xxf.qt.english.services.TTSService
import com.xxf.qt.english.services.WordDbService
import com.xxf.room.demo.R
import com.xxf.room.demo.databinding.ActivityMainBinding
import com.xxf.rxjava.bindLifecycle
import com.xxf.rxjava.observeOnMain
import com.xxf.rxjava.subscribeOnIO
import com.xxf.view.titlebar.OnTitleBarListener
import com.xxf.view.titlebar.TitleBar
import com.xxf.viewbinding.viewBinding
import io.reactivex.rxjava3.core.Observable
import java.util.Locale


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

    class MyFragmentStateAdapter(private val list: List<WordInfo>, ft: FragmentActivity) :
        FragmentStateAdapter(ft) {
        override fun getItemCount(): Int = list.size

        override fun createFragment(position: Int): Fragment {
            return WordFragment.newInstance(list[position % list.size])
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        ///初始化一下
        val tts = TTSService.toString()

        with(binding) {

            binding.titleBar.setOnTitleBarListener(object : OnTitleBarListener {
                override fun onRightClick(titleBar: TitleBar?) {
                    super.onRightClick(titleBar)
                    startActivity<SettingsActivity>()
                }
            })

            binding.viewPager2.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    TTSService.stop()
                }
            })

            Observable
                .fromCallable {
                    // 插入两个默认的
                    if (WordDbService.count { it } == 0L) {
                        WordDbService.insert(listOf(WordInfo("hello"), WordInfo("world")))
                    }
                    WordDbService.selectList { it }
                }.subscribeOnIO()
                .observeOnMain()
                .bindErrorNotice()
                .bindLifecycle(this@MainActivity)
                .subscribe {
                    viewPager2.adapter = MyFragmentStateAdapter(it, this@MainActivity)
                }
        }
    }
}