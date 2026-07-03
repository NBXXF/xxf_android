package com.xxf.arch.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import java.io.Serializable
import kotlin.reflect.KClass

/**
 * 常用于设置步骤 每一步动态分发下一步(比如按类型 等等)
 *
 * 继承自一个没有实现Serializable接口的父类的类序列化时, 应该注意哪些事项?
 * 在此种情况下, 父类必须要有一个显示的无参构造函数,而且其无参构造函数必须是能够对其子类可见的, 比如不能使用 private 修饰, 否则会抛出异常. 其原因在于, 在反序列化的过程中, 父类的变量会通过父类的无参构造函数进行初始化(==按照文档的意思, 似乎无参构造函数必须申明为public/protected, 但我实际试了下, 不使用也可以, 只要保证子类能正常访问就行==)
 */
abstract class ProjectStepSetting<S>
/**
 * 常用于设置步骤 每一步动态分发下一步(比如按类型 等等)
 *
 * @param stepSettingData 当前步骤数据
 */(open var stepSettingData: S) : Serializable {
    companion object {
        const val KEY_STEP_SETTING: String = "stepSetting";
    }

    /**
     * 当前步骤启动intent
     */
    abstract fun stepLauncher(context: Context): Intent

    /**
     * 从总流程中合并之后的设置
     */
    abstract fun mergedStepSetting(setting: Any)

    /**
     * 校验数据 当前数据(val stepSetting) 是否可以进行下一步
     */
    abstract fun verifyStepSetting(): Boolean

    /**
     * 创建下一步,如果为空就没有下一步了 代表整个流程结束
     */
    abstract fun nextStepSetting(): ProjectStepSetting<*>?
}

/**
 * 简化 传参 默认
 */
abstract class ProjectStepActivitySetting<T : Activity, S>(override var stepSettingData: S) :
    ProjectStepSetting<S>(stepSettingData) {

    override fun stepLauncher(context: Context): Intent {
        return fillSettingIntent(context, stepLauncherActivity())
    }

    /**
     * 当前步骤 activity class
     */
    abstract fun stepLauncherActivity(): Class<T>;
}


fun <S> ProjectStepSetting<S>.fillSettingIntent(
    context: Context,
    act: Class<*>,
): Intent = this.run {
    val value = this
    val key: String = ProjectStepSetting.KEY_STEP_SETTING
    Intent(context, act).apply {
        when (value) {
            //优先Parcelable
            is Parcelable -> {
                putExtra(key, value as Parcelable)
            }

            is Serializable -> {
                putExtra(key, value)
            }

            else -> {
                throw IllegalArgumentException("参数无法序列化")
            }
        }
    }
}

/**
 * 跳入下一步
 * @return 是否跳转成功
 */
fun <S> ProjectStepSetting<S>.jumpNextStepSetting(
    context: Context,
    block: Intent.() -> Unit = {}
): Boolean = this.run {
    val stepLauncher = this.nextStepSetting()?.stepLauncher(context) ?: return false
    stepLauncher.apply(block)
    val key: String = ProjectStepSetting.KEY_STEP_SETTING
    if (!stepLauncher.hasExtra(key)) {
        throw IllegalArgumentException("缺乏$key 参数")
    }
    context.startActivity(stepLauncher)
    true
}