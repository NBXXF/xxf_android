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
        Object mergedData = step.mergedStepSetting(settings);
        while (step.verifyStepSetting(mergedData)) {
            completedStepList.add(step);
            ProjectStepSetting<Object> temp = step.nextStepSetting(mergedData);
            if (temp == null) {
                break;
            } else {
                step = temp;
                mergedData = step.mergedStepSetting(settings);
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
                && step.verifyStepSetting(step.stepSettingData)
                && step.nextStepSetting(step.stepSettingData) != null;
    }
}
