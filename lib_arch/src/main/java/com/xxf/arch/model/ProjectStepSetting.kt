package com.xxf.arch.model

import android.content.Context
import android.content.Intent
import android.os.Parcelable


/**
 * 常用于设置步骤 每一步动态分发下一步(比如按类型 等等)
 * @param stepSetting 当前步骤数据
 * @param projectSetting 整个项目流程的数据
 */
abstract class ProjectStepSetting<G>(val stepSetting: Any, val projectSetting: G):Parcelable {

    /**
     * 当前步骤启动intent
     */
    abstract fun stepLauncher(context: Context): Intent

    /**
     * 校验数据 当前数据(val stepSetting) 是否可以进行下一步
     */
    abstract fun verifyStepSetting(): Boolean

    /**
     * 创建下一步,如果为空就没有下一步了 代表整个流程结束
     */
    abstract fun nextStepSetting(): ProjectStepSetting<G>?

    /**
     * 找寻到项目上次进行的最后一步
     */
    final fun findMostRecentStep(): ProjectStepSetting<G> {
        var step: ProjectStepSetting<G> = this
        while (step.verifyStepSetting()) {
            val temp = step.nextStepSetting()
            if (temp == null) {
                break
            } else {
                step = temp
            }
        }
        return step;
    }

    /**
     * 整个项目是否完成
     */
    final fun <G> isProjectCompleted(): Boolean {
        return findMostRecentStep().nextStepSetting() == null
    }
}