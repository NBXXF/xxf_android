package com.xxf.arch.lint

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xxf.application.activity.SimpleActivityLifecycleCallbacks
import com.xxf.arch.activity.XXFActivity
import com.xxf.arch.fragment.XXFBottomSheetDialogFragment
import com.xxf.arch.fragment.XXFDialogFragment
import com.xxf.arch.fragment.XXFFragment
import io.reactivex.rxjava3.functions.BiConsumer
import java.lang.RuntimeException

/**
 * 组件检查伪插件
 */
object ComponentLintPlugin {
    /**
     * 全局拦截继承异常
     */
    var lintConsumer: BiConsumer<Any, Class<*>>? = null

    /**
     * 指定归属的类
     */
    internal val assignableMap = mutableMapOf<Class<*>, Class<*>>()
        .apply {
            this[Activity::class.java] = XXFActivity::class.java
            this[DialogFragment::class.java] = XXFDialogFragment::class.java
            this[BottomSheetDialogFragment::class.java] = XXFBottomSheetDialogFragment::class.java
            this[Fragment::class.java] = XXFFragment::class.java
        }
    private val checkPackages = mutableListOf<String>()

    /**
     * 初始化
     * @param packages 检查的包名
     */
    fun initPlugin(app: Application, packages: List<String>) {
        LintActivityLifecycleCallbacks.register(app)
        checkPackages.clear()
        checkPackages.addAll(packages)
    }

    /**
     * 注册归属
     */
    fun registerAssignable(target: Class<*>, from: Class<*>) {
        assignableMap.put(target, from)
    }

    /**
     * 取消注册归属
     */
    fun unregisterAssignable(target: Class<*>) {
        assignableMap.remove(target)
    }

    /**
     * 检查
     */
    internal fun check(obj: Any, assignable: Class<*>) {
        if (checkPackages.indexOfFirst {
                obj.javaClass.`package`?.name?.startsWith(it) == true
            } < 0) {
            return
        }
        if (!assignable.isAssignableFrom(obj::class.java)) {
            lintConsumer?.accept(obj, assignable)
                ?: throw RuntimeException("$obj must extends $assignable")
        }
    }
}


internal object LintActivityLifecycleCallbacks : SimpleActivityLifecycleCallbacks() {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityCreated(activity, savedInstanceState)
        ComponentLintPlugin.assignableMap.get(activity::class.java).let {
            ComponentLintPlugin.assignableMap.get(Activity::class.java)
        }?.let {
            ComponentLintPlugin.check(activity, it)
        }
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                LintFragmentLifecycleCallbacks,
                true
            )
        }
    }
}

internal object LintFragmentLifecycleCallbacks : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        super.onFragmentResumed(fm, f)
        when (f) {
            is DialogFragment -> {
                ComponentLintPlugin.assignableMap[f::class.java].let {
                    ComponentLintPlugin.assignableMap.get(DialogFragment::class.java)
                }?.let {
                    ComponentLintPlugin.check(f, it)
                }
            }
            is BottomSheetDialogFragment -> {
                ComponentLintPlugin.assignableMap[f::class.java].let {
                    ComponentLintPlugin.assignableMap.get(BottomSheetDialogFragment::class.java)
                }?.let {
                    ComponentLintPlugin.check(f, it)
                }
            }
            else -> {
                ComponentLintPlugin.assignableMap[f::class.java].let {
                    ComponentLintPlugin.assignableMap.get(Fragment::class.java)
                }?.let {
                    ComponentLintPlugin.check(f, it)
                }
            }
        }
    }
}

