package com.xxf.arch.test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import by.kirich1409.viewbindingdelegate.internal.getRootView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonObject
import com.xxf.activityresult.startActivityForResultObservable
import com.xxf.arch.fragment.XXFBottomSheetDialogFragment
import com.xxf.json.JsonUtils
import com.xxf.arch.test.databinding.TestFragmentBinding
import com.xxf.utils.DensityUtil
import java.io.Serializable
import java.math.BigDecimal


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：6/28/21
 * Description :
 */
interface OnCallDataListener : Serializable {
    fun test(): Boolean
}

class TestDialogFragment : XXFBottomSheetDialogFragment<String?>(R.layout.test_fragment) {
    val binding by viewBinding(TestFragmentBinding::bind)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivityForResultObservable(Intent(requireContext(),TestActivity::class.java),1000)
            .subscribe {

            }
        val context = context
        println("==========>${context}")

        // val listener = arguments?.getSerializable("call") as OnCallDataListener?
        //println("===========>test  收到:" + listener!!.test() + "  " + listener.hashCode())

//        System.out.println("========>hello3:"+this);
//        DialogFragmentTestBinding biding = DialogFragmentTestBinding.inflate(getLayoutInflater());
//        setContentView(biding.getRoot());
//        biding.okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setComponentResult("TestDialogFragment yes event:" + System.currentTimeMillis());
//            }
//        });
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       getBehavior()?.state=BottomSheetBehavior.STATE_EXPANDED
        setWindowSize(ViewGroup.LayoutParams.MATCH_PARENT,DensityUtil.dip2px(300.0f))
//        val toJsonString = JsonUtils.toJsonString(Test().apply {
//            d = BigDecimal("8.8")
//        })

        val toBean = com.xxf.json.JsonUtils.toBean(   JsonObject().apply {
            this.addProperty("d",8.8)
        }, Test::class.java)
        println("============>toBean:$toBean")

        TestNumber.test()
        binding.btnTest.setOnClickListener {
            testPrint(it)
            // KeyboardUtils.hideSoftKeyBoard(this,true)
            // SystemUtils.hideSoftKeyBoard(requireActivity())
            //  hideInput()
        }
        test(binding.root);
    }

    private fun test(view: View?){
        view?.let {
            println("============>view:$it")
            if(it.parent is View){
                test(it.parent as View);
            }
        }
    }

    class Test{
        var d:BigDecimal?=null
        override fun toString(): String {
            return "Test(d=$d)"
        }

    }

    fun testPrint(view: View) {
        println("========>view:$view")
        if (view.parent is View) {
            testPrint(view.parent as View)
        } else {
            println("========>parent:${view.parent}")
        }
    }

    fun hideInput() {
        val imm: InputMethodManager? =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        val v: View? = dialog?.getWindow()?.peekDecorView()
        if (null != v) {
            //强制隐藏键盘
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
    }

//    override fun onStart() {
//        super.onStart()
//        val window = dialog!!.window
//        val params = window!!.attributes
//        params.gravity = Gravity.BOTTOM
//        params.width = 1000
//        params.height = 1000
//        window.attributes = params
//        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//    }

//    override fun onResume() {
//        super.onResume()
//        val window = dialog!!.window
//        val params = window!!.attributes
//        params.gravity = Gravity.BOTTOM
//        params.width = 1000
//        params.height = 1000
//        window.attributes = params
//        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        setComponentResult("TestDialogFragment yes event:" + System.currentTimeMillis())
//    }


    companion object {
        @JvmStatic
        fun newInstance(listener: OnCallDataListener?): TestDialogFragment {
            val testDialogFragment = TestDialogFragment()
            val args = Bundle()
            args.putSerializable("call", listener)
            testDialogFragment.arguments = args
            return testDialogFragment
        }
    }
}