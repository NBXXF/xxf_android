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

    public final List<ProjectStepSetting<?>> findCompletedSteps() {
        List<ProjectStepSetting<?>> completedStepList = new ArrayList<ProjectStepSetting<?>>();
        ProjectStepSetting<?> step = firstStep;
        //进行合并
        step.mergedStepSetting(settings);
        while (step.verifyStepSetting()) {
            completedStepList.add(step);
            ProjectStepSetting<?> temp = step.nextStepSetting();
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
    public final ProjectStepSetting<?> findCompletedStep() {
        List<ProjectStepSetting<?>> list = findCompletedSteps();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    /**
     * 整个项目是否完成
     */
    public final Boolean isCompleted() {
        ProjectStepSetting<?> step = findCompletedStep();
        return step != null
                && step.verifyStepSetting()
                && step.nextStepSetting() == null;
    }
}
