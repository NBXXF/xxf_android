package com.xxf.arch.test.navigationdemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.xxf.viewbinding.viewBinding
import com.xxf.application.lifecycle.findViewLifecycleOwner
import com.xxf.arch.fragment.XXFFragment
import com.xxf.arch.test.R
import com.xxf.arch.test.databinding.FragmentFirstBinding

class FirstFragment : XXFFragment<Unit>(R.layout.fragment_first) {
    val binding by viewBinding(FragmentFirstBinding::bind)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val get = ViewTreeViewModelStoreOwner.get(view)
        binding.info.text = "life:${view.findViewLifecycleOwner()}"
        binding.recyclerView.adapter = TestAdapter()
            .apply {
                val list = arrayListOf<String>()
                for (i in 0..100) {
                    list.add("" + i)
                }
                bindData(true, list)
            }

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.jump.setOnClickListener {
            Toast.makeText(it.context, "xx:" + it.findViewLifecycleOwner(), Toast.LENGTH_LONG)
                .show()
            //跳转到下一个fragment中
           // findSafeNavController()?.navigation(SecondFragment())
            //  findNavController().navigation(SecondFragment())
        }
    }
}