package com.xxf.arch.test.navigationdemo

import android.os.Bundle
import android.view.View
import com.xxf.viewbinding.viewBinding
import com.xxf.arch.fragment.XXFFragment
import com.xxf.arch.fragment.navigation.findNavController
import com.xxf.arch.fragment.navigation.isInNavController
import com.xxf.arch.test.R
import com.xxf.arch.test.databinding.FragmentFirstBinding
import com.xxf.arch.test.databinding.FragmentSecondBinding

/**
 * activity tao z
 */
class SecondFragment : XXFFragment<Unit>(R.layout.fragment_second) {

    val binding by viewBinding(FragmentSecondBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = TestAdapter()
            .apply {
                val list = arrayListOf<String>()
                for (i in 0..100) {
                    list.add("" + i)
                }
                bindData(true, list)
            }

        binding.back.setOnClickListener {
            if (isInNavController()) {
                //在导航控制器中
               // findNavController().navigationUp()

                findNavController().navigationUp(Int.MIN_VALUE)
            } else {
                //不在导航控制器中 比如activity 嵌套了一个此fragment 业务自己处理
            }
        }
    }
}