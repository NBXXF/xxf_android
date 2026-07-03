package com.xxf.arch.model;


import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProjectSetting {
public static ProjectSetting of(ProjectStepSetting firstStep, Object settings) {
return new ProjectSetting(firstStep, settings);
}

    ProjectStepSetting firstStep;
    Object settings;

    private ProjectSetting(ProjectStepSetting firstStep, Object settings) {
        this.firstStep = firstStep;
        this.settings = settings;
    }

    public final List<ProjectStepSetting<Object>> findCompletedSteps() {
        List<ProjectStepSetting<Object>> completedStepList = new ArrayList<ProjectStepSetting<Object>>();
        ProjectStepSetting<Object> step = firstStep;
        //进行合并
        step.mergedStepSetting(settings);
        while (step.verifyStepSetting()) {
            completedStepList.add(step);
            ProjectStepSetting<Object> temp = step.nextStepSetting();
            if (temp == null) {
                break;
            } else {
                step = temp;
            }
        }
        return completedStepList;
    }


    /**
     * 找寻到项目上次进行的最后一步
     */
    @Nullable
    public final ProjectStepSetting<Object> findCompletedStep() {
        List<ProjectStepSetting<Object>> list = findCompletedSteps();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    /**
     * 整个项目是否完成
     */
    public final Boolean isCompleted() {
        ProjectStepSetting<Object> step = findCompletedStep();
        return step != null
                && step.verifyStepSetting()
                && step.nextStepSetting() == null;
    }
}


package com.xxf.arch.model;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
      public abstract void mergedStepSetting(@NonNull Object setting);

  /**
    * 校验数据 当前数据(val stepSetting) 是否可以进行下一步
      */
      public abstract Boolean verifyStepSetting();

  /**
    * 创建下一步,如果为空就没有下一步了 代表整个流程结束
      */
      @Nullable
      public abstract ProjectStepSetting nextStepSetting();
      }



package com.xxf.arch.model

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import java.io.Serializable

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