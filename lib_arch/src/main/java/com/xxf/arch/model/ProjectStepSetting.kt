package com.xxf.arch.model

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import java.io.Serializable

/**
 * 常用于设置步骤 每一步动态分发下一步(比如按类型 等等)
 */
abstract class ProjectStepSetting<S>
/**
 * 常用于设置步骤 每一步动态分发下一步(比如按类型 等等)
 *
 * @param stepSettingData 当前步骤数据
 */(open var stepSettingData: S) {
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

fun <S> ProjectStepSetting<S>.fillSettingIntent(
    context: Context,
    act: Class<*>,
    key: String = "stepSetting"
): Intent = this.run {
    val value = this
    Intent(context, act).apply {
        when (value) {
            is Parcelable -> {
                putExtra(key, value)
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