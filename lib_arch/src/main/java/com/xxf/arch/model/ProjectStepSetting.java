package com.xxf.arch.model;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

/**
 * 常用于设置步骤 每一步动态分发下一步(比如按类型 等等)
 */
public abstract class ProjectStepSetting<S> {

    @NonNull
    public S stepSettingData;

    /**
     * 常用于设置步骤 每一步动态分发下一步(比如按类型 等等)
     *
     * @param stepSettingData 当前步骤数据
     */
    public ProjectStepSetting(@NonNull S stepSettingData) {
        this.stepSettingData = stepSettingData;
    }

    /**
     * 当前步骤启动intent
     */
    public abstract Intent stepLauncher(@NonNull Context context);

    /**
     * 从总流程中合并之后的设置
     */
    public abstract S mergedStepSetting(@NonNull Object setting);

    /**
     * 校验数据 当前数据(val stepSetting) 是否可以进行下一步
     */
    public abstract Boolean verifyStepSetting(@NonNull S setting);

    /**
     * 创建下一步,如果为空就没有下一步了 代表整个流程结束
     */
    public abstract ProjectStepSetting nextStepSetting(@NonNull S setting);
}
