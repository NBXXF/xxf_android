package com.xxf.arch.model

import android.content.Context
import android.content.Intent
import android.os.Parcelable


/**
 * 常用于设置步骤 每一步动态分发下一步(比如按类型 等等)
 * @param stepSetting 当前步骤数据
 * @param projectSetting 整个项目流程的数据
 */
abstract class ProjectStepSetting<S>(open val stepSetting: S) : Parcelable {

    /**
     * 当前步骤启动intent
     */
    abstract fun stepLauncher(context: Context): Intent

    /**
     * 从总流程中合并之后的设置
     */
    abstract fun mergedStepSetting(setting: Any): S

    /**
     * 校验数据 当前数据(val stepSetting) 是否可以进行下一步
     */
    abstract fun verifyStepSetting(setting: S = stepSetting): Boolean

    /**
     * 创建下一步,如果为空就没有下一步了 代表整个流程结束
     */
    abstract fun nextStepSetting(setting: S = stepSetting): ProjectStepSetting<Any>?

}

class ProjectSetting internal constructor(
    private var firstStep: ProjectStepSetting<Any>,
    private var settings: Any
) {
    companion object {
        /**
         * @param settings 全局setting
         */
        fun of(firstStep: ProjectStepSetting<Any>, settings: Any): ProjectSetting {
            return ProjectSetting(firstStep, settings)
        }
    }

    /**
     * 找寻到项目已经完成的步骤
     */
    final fun findCompletedSteps(): List<ProjectStepSetting<Any>> {
        val completedStepList = mutableListOf<ProjectStepSetting<Any>>()
        var step: ProjectStepSetting<Any> = firstStep
        var mergedData = step.mergedStepSetting(settings)
        while (step.verifyStepSetting(mergedData)) {
            completedStepList.add(step)
            val temp = step.nextStepSetting(mergedData)
            if (temp == null) {
                break
            } else {
                step = temp
                mergedData = step.mergedStepSetting(settings)
            }
        }
        return completedStepList
    }

    /**
     * 找寻到项目上次进行的最后一步
     */
    final fun findCompletedStep(): ProjectStepSetting<Any>? {
        return findCompletedSteps().firstOrNull()
    }

    /**
     * 整个项目是否完成
     */
    final fun <G> isCompleted(): Boolean {
        return findCompletedStep()?.run {
            this.verifyStepSetting() && this.nextStepSetting() == null
        } ?: false
    }
}