package com.xxf.media.preview.ui


import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.xxf.media.preview.Config
import com.xxf.media.preview.databinding.XxfActivityPreviewBinding
import com.xxf.media.preview.model.PreviewParam

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/13
 * Description ://
 */
open class PreviewActivity : AppCompatActivity() {
    private lateinit var params: PreviewParam;
    private lateinit var imageViewPagerAdapter: FragmentStatePagerAdapter
    private val fragmentMap = hashMapOf<Int, Fragment>()
    private val binding by lazy {
        XxfActivityPreviewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //这个会报错 部分机型
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)
        supportPostponeEnterTransition()
        initView()
    }

    private fun initView() {
        params = intent.getSerializableExtra(Config.PREVIEW_PARAM) as PreviewParam
        imageViewPagerAdapter =
            object : FragmentStatePagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
                override fun getCount(): Int {
                    return params.urls.size
                }

                override fun getItem(position: Int): Fragment {
                    val fragment = fragmentMap[position]
                    return if (fragment == null) {
                        lateinit var imageFragment: Fragment;
                        /**
                         * 处理自定义问题
                         */
                        if (!TextUtils.isEmpty(params.userFragmentClass)) {
                            imageFragment =
                                Class.forName(params.userFragmentClass).newInstance() as Fragment
                        } else {
                            imageFragment = PreviewFragment()
                        }
                        imageFragment.arguments = Bundle().apply {
                            putSerializable(Config.PREVIEW_PARAM, params.urls.get(position))
                        }
                        fragmentMap[position] = imageFragment
                        imageFragment
                    } else {
                        fragment
                    }
                }
            }

        binding.viewPager.adapter = imageViewPagerAdapter
        if (params.currentIndex >= 0 && params.currentIndex < params.urls.size) {
            binding.viewPager.setCurrentItem(params.currentIndex, false)
            binding.viewPager.transitionName = params.sharedElementName
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            val lp = window.attributes
            lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
            window.attributes = lp
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            supportActionBar?.hide()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}